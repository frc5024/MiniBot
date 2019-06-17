package frc.robot;

import edu.wpi.first.networktables.NetworkTable;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import frc.common.wrappers.Camera;
import frc.common.utils.FileUtils;
import frc.common.field.FieldStatusThread;
import frc.common.loopers.SubsystemLooper;
import frc.common.utils.RobotLogger;
import frc.common.utils.RobotLogger.Level;
import frc.common.network.NetworkTables;
import frc.common.network.ConnectionMonitor;

import frc.robot.commands.DriveControl;

import frc.robot.subsystems.DriveTrain;
import frc.robot.subsystems.Ledring;

// Robot design ideas:
// Any internal movement and actions must be fully autonomous.
// The drivers should only be able to control setpoints

public class Robot extends TimedRobot {
	public double last_timestamp = Timer.getFPGATimestamp();

	/* Telemetry */
	NetworkTables nt_inst = NetworkTables.getInstance();

	ShuffleboardTab driver_view = Shuffleboard.getTab("Driver View");
	FieldStatusThread field_status = new FieldStatusThread();

	/* Vision */
	Camera main_camera;

	/* Networking and Logging */
	ConnectionMonitor net_monitor = ConnectionMonitor.getInstance();
	RobotLogger logger = RobotLogger.getInstance();

	/* Sybsystems */
	private SubsystemLooper mSubsystemLooper;
	public static DriveTrain mDriveTrain;
	public static OI mOI;
	public static Ledring mLedring;

	/* Commands */
	public DriveControl mDriveControl;

	@Override
	public void robotInit() {
		System.out.println("Robot starting...\nWelcome 5024!");
		logTimestamp();
		this.m_period = Constants.PeriodicTiming.robot_period;

		/* Start the CameraServer for the default USBCamera */
		logger.log("Starting CameraServer", Level.kRobot);
		this.main_camera = new Camera(Constants.MainCamera.name, Constants.MainCamera.http_port);
		main_camera.loadJsonConfig(FileUtils.constructDeployPath("maincamera.json"), Camera.ConfigType.kPrimary);
		main_camera.keepCameraAwake(true);

		// Push camera to shuffleboard
		nt_inst.getEntry("vision", "camera_fov").setNumber(Constants.MainCamera.fov);
		driver_view.add(main_camera.getCameraSever()).withSize(4, 4);

		/* Construct all Subsystems */
		logger.log("Constructing Subsystems", Level.kRobot);
		mOI = OI.getInstance();
		mDriveTrain = DriveTrain.getInstance();
		mLedring = new Ledring();

		/* Create the SubsystemLooper */
		logger.log("Setting up the Subsystem Looper", Level.kRobot);
		mSubsystemLooper = new SubsystemLooper();

		/* Create the SubsystemLooper */
		logger.log("Registering subsystems with the Subsystem Looper", Level.kRobot);
		mSubsystemLooper.register(mDriveTrain);
		mSubsystemLooper.register(mLedring);

		/* Initalize Subsystems if required */
		logger.log("Initializing Subsystems", Level.kRobot);
		mDriveTrain.setBrakes(false);

		/* Construct Commands */
		logger.log("Constructing Commands", Level.kRobot);
		this.mDriveControl = new DriveControl();

		/* Set up notifiers */
		logger.log("Setting up Notifiers", Level.kRobot);
		this.field_status.start(Constants.PeriodicTiming.field_period);
		this.logger.start(Constants.PeriodicTiming.logging_period);
		this.net_monitor.start(Constants.PeriodicTiming.net_monitor_period);

		/* Set NT period */
		logger.log("Setting NetworkTables period time to: " + Constants.PeriodicTiming.nt_period, Level.kRobot);
		this.nt_inst.setPeriod(Constants.PeriodicTiming.nt_period);

		/* Start the SubsystemLooper */
		logger.log("Setting up the Subsystem Looper", Level.kRobot);
		mSubsystemLooper.start(Constants.PeriodicTiming.subsystem_looper);
	}


	@Override
	public void robotPeriodic() {

		// Tell the Ledring to start blinking if the bot looses connection
		if (net_monitor.justDisconnected()) {
			mLedring.setWantedState(Ledring.WantedState.kStrobe);
		} else if (net_monitor.justConnected()) {
			mLedring.setWantedState(Ledring.WantedState.kOff);
		}

		// Run the superstructure's periodic function
		updateTimestamp();

	}


	@Override
	public void disabledInit() {
		SmartDashboard.putString("Robot Mode", "DISABLED");
		logger.log("Robot Disabled", Level.kRobot);
		logTimestamp();

		// Stop all recordings
		Shuffleboard.stopRecording();

		// Set robot to coast
		// This allows for "clutch saves" in games
		// along with allowing us to easily push our bots around.
		// This will also force a CAN packet to all talons
		mDriveTrain.setBrakes(false);
	}

	@Override
	public void disabledPeriodic() {
		Scheduler.getInstance().run();

		/* Run all updaters */
		updateSmartdashboard();
		updateTimestamp();
	}

	@Override
	public void autonomousInit() {
		SmartDashboard.putString("Robot Mode", "AUTO");
		logger.log("Autonomous Started with GSM: " + this.field_status.getCurrentMatch().getGSM(), Level.kRobot);
		logTimestamp();

		// Start recording video on driverstation computer
		Shuffleboard.startRecording();
	}

	@Override
	public void autonomousPeriodic() {
		Scheduler.getInstance().run();

		/* Run all updaters */
		updateSmartdashboard();
		updateTimestamp();
	}

	@Override
	public void teleopInit() {
		SmartDashboard.putString("Robot Mode", "TELEOP");
		logger.log("Teleop started", Level.kRobot);
		logTimestamp();

		// Start recording video on driverstation computer
		Shuffleboard.startRecording();

		// Enable brakes
		mDriveTrain.setBrakes(true);

		/* Start commands */
		this.mDriveControl.start();

	}

	@Override
	public void teleopPeriodic() {
		Scheduler.getInstance().run();

		/* Run all updaters */
		updateSmartdashboard();
		updateTimestamp();
	}

	@Override
	public void testPeriodic() {
	}

	/**
	 * Asks all Subsystems to push their telemetry data to SmartDashboard
	 */
	private void updateSmartdashboard() {
		mDriveTrain.outputTelemetry();
	}

	/**
	 * Asks the FPGA how long it has been powered on, then stores the value in last_timestamp
	 */
	private void updateTimestamp() {
		this.last_timestamp = Timer.getFPGATimestamp();
	}

	/**
	 * Prints the current timestamp to the log
	 */
	private void logTimestamp() {
		System.out.println("Current time: " + this.last_timestamp);
	}
}

