package frc.robot;

import java.util.logging.Logger;

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

import frc.robot.commands.TriggerDrive;
import frc.robot.subsystems.DriveTrain;

public class Robot extends TimedRobot {
  private final Logger logger = Logger.getLogger(this.getClass().getName());
  
  public double last_timestamp = Timer.getFPGATimestamp();

  /* Telemetry */
  ShuffleboardTab driver_view = Shuffleboard.getTab("Driver View");
  FieldStatusThread field_status;
  Camera main_camera;

  /* Sybsystems */
  public static DriveTrain mDriveTrain;
  public static OI mOI;

  /* Commands */
  public TriggerDrive mTriggerDrive;

  @Override
  public void robotInit() {
    logger.info("Robot starting\nWelcome 5024!");
    logTimestamp();

    /* Start the CameraServer for the default USBCamera */
    logger.info("Starting CameraServer");
    this.main_camera = new Camera(Constants.MainCamera.name, Constants.MainCamera.http_port);
    this.main_camera.loadJsonConfig(FileUtils.constructDeployPath("maincamera.json"));
    this.main_camera.keepCameraAwake(true);

    /* Construct all Subsystems */
    logger.info("Constructing Subsystems");
    mOI = new OI();
    mDriveTrain = new DriveTrain();

    /* Initalize Subsystems if required */
    logger.info("Initializing Subsystems");
    mDriveTrain.setBrakes(true);

    /* Construct Commands */
    logger.info("Constructing Commands");
    this.mTriggerDrive = new TriggerDrive();

    /* Set up notifiers */
    logger.info("Setting up Notifiers");
    this.m_period = Constants.PeriodicTiming.robot_period;
    this.field_status = new FieldStatusThread();
    this.field_status.start(Constants.PeriodicTiming.field_period);
  }

  @Override
  public void robotPeriodic() {
  }


  @Override
  public void disabledInit() {
    SmartDashboard.putString("Robot Mode", "DISABLED");
    logger.info("Robot Disabled");
    logTimestamp();
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
    logger.info("Autonomous Started with GSM: " + this.field_status.getCurrentMatch().getGSM());
    logTimestamp();
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
    logger.info("Teleop started");
    logTimestamp();

    /* Start commands */
    this.mTriggerDrive.start();
    
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
    logger.info("Current time: " + this.last_timestamp);
  }

}
