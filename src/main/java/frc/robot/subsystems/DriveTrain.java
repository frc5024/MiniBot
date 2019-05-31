package frc.robot.subsystems;

import frc.common.utils.RobotLogger;
import frc.common.utils.RobotLogger.Level;

import edu.wpi.first.wpilibj.command.Subsystem;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.SPI.Port;
import com.kauailabs.navx.frc.AHRS;

import frc.robot.commands.TriggerDrive;
import frc.common.wrappers.GearBox;
import frc.robot.Constants;
import frc.common.control.SlewLimiter;
import frc.common.wrappers.PathingHelper;
import frc.common.wrappers.TankTrajectory;

/**
 * The Subsystem in control of the robot's drivebase.
 */
public class DriveTrain extends Subsystem {
    private static DriveTrain instance = null;

    private final RobotLogger logger = RobotLogger.getInstance();

    GearBox mLeftGearbox;
    GearBox mRightGearbox;

    DifferentialDrive mDrivebase;

    SlewLimiter accelerator = new SlewLimiter(Constants.accelerationStep);

    AHRS gyro;

    protected boolean is_moving, is_turning = false;

    public DriveTrain(){
        /* Create both gearbox objects */
        logger.log("[DriveTrain] Constructing GearBoxes out of motor pairs", Level.kRobot);
        mLeftGearbox = new GearBox(new WPI_TalonSRX(Constants.DriveTrain.leftFrontMotor), new WPI_TalonSRX(Constants.DriveTrain.leftRearMotor));
        mRightGearbox = new GearBox(new WPI_TalonSRX(Constants.DriveTrain.rightFrontMotor), new WPI_TalonSRX(Constants.DriveTrain.rightRearMotor));

        /* Enable current limiting on each gearbox */
        logger.log("[DriveTrain] Limiting current on both gearboxes. Peak: " + Constants.DriveTrain.peakCurrent + "A, Hold: "
                + Constants.DriveTrain.holdCurrent + "A, Timeout: " + Constants.DriveTrain.holdCurrent + "ms", Level.kRobot);
        mLeftGearbox.limitCurrent(Constants.DriveTrain.peakCurrent, Constants.DriveTrain.holdCurrent, Constants.DriveTrain.holdCurrent);
        mRightGearbox.limitCurrent(Constants.DriveTrain.peakCurrent, Constants.DriveTrain.holdCurrent, Constants.DriveTrain.currentTimeout);

        /* Create a DifferentialDrive out of each gearbox */
        mDrivebase = new DifferentialDrive(mLeftGearbox.getMaster(), mRightGearbox.getMaster());
        mDrivebase.setSafetyEnabled(false); // Make sure the robot doesn't lock up
        logger.log("[DriveTrain] Drivebase has been set to: Unsafe", Level.kRobot);

        /* Set up the gyro */
        logger.log("[DriveTrain] Loading gyro for drivetrain", Level.kRobot);
        this.gyro = new AHRS(Port.kMXP);
        this.gyro.reset();
        logger.log("[DriveTrain] Gyro has been reset to: " + this.gyro.getAngle(), Level.kRobot);

    }

    @Override
    /**
     * Called by WPIlib's Scheduler during initalization
     */
    public void initDefaultCommand() {
        // setDefaultCommand(new TriggerDrive());
    }

    public static DriveTrain getInstance() {
        if (instance == null) {
            instance = new DriveTrain();
        }

        return instance;
    }

    /**
     * The standard wrapper around WPIlib's joystick-based drive function
     * 
     * @param speed Forward speed (from -1.0 to 1.0)
     * @param rotation Rotation of robot (from -1.0 to 1.0)
     */
    public void arcadeDrive(double speed, double rotation) {
        this.is_moving = (speed != 0.0);
        this.is_turning = (rotation != 0.0);

        mDrivebase.arcadeDrive(speed, rotation, false);
    }

    /**
     * Drive the robot with artificial acceleration and gear shifting
     * 
     * @param speed Forward speed (from -1.0 to 1.0)
     * @param rotation Rotation of robot (from -1.0 to 1.0)
     */
    public void raiderDrive(double speed, double rotation) {
        this.is_moving = (speed != 0.0);
        this.is_turning = (rotation != 0.0);

        /* Feed the accelerator */
        speed = this.accelerator.feed(speed);

        /* Send motor data to the mDrivebase */
        mDrivebase.arcadeDrive(speed, rotation, false);
    }

    /**
     * The standard wrapper around WPIlib's joystick-based drive function with optional input scaling
     * 
     * @param speed Forward speed (from -1.0 to 1.0)
     * @param rotation Rotation of robot (from -1.0 to 1.0)
     * @param is_inputs_squared Should WPIlib try to scale the inputs
     */
    public void arcadeDrive(double speed, double rotation, boolean is_inputs_squared) {
        this.is_moving = (speed != 0.0);
        this.is_turning = (rotation != 0.0);

        mDrivebase.arcadeDrive(speed, rotation, is_inputs_squared);
    }

    /**
     * Wrapper around 254's drive interface.
     * 
     * The "turning" stick controls the curvature of the robot's path rather than its rate of heading change. 
     * This helps make the robot more controllable at high speeds. Also handles the robot's quick turn 
     * functionality - "quick turn" overrides constant-curvature turning for turn-in-place maneuvers.
     * 
     * @param speed Forward speed (from -1.0 to 1.0)
     * @param rotation Rotation of robot (from -1.0 to 1.0)
     * @param is_quick_turn Is quick turn functionality enabled?
     */
    public void cheesyDrive(double speed, double rotation, boolean is_quick_turn) {
        this.is_moving = (speed != 0.0);
        this.is_turning = (rotation != 0.0);
        mDrivebase.curvatureDrive(speed, rotation, is_quick_turn);
    }

    /**
     * Enables or disables brake mode on all drivebase talons
     * 
     * @param on Should the brakes be enabled?
     */
    public void setBrakes(boolean on){
        NeutralMode mode = on ? NeutralMode.Brake : NeutralMode.Coast;
        String mode_string = on ? "Brake" : "Coast";

        logger.log("[DriveTrain] NeutralMode has been set to: " + mode_string);

        this.mLeftGearbox.front.setNeutralMode(mode);
        this.mLeftGearbox.rear.setNeutralMode(mode);
        this.mRightGearbox.front.setNeutralMode(mode);
        this.mRightGearbox.rear.setNeutralMode(mode);
    }

    /**
     * Get the number of ticks recorded by the left GearBox's encoder
     * 
     * @return Number of ticks
     */
    public int getLeftGearboxTicks(){
        return this.mLeftGearbox.getTicks();
    }

    /**
     * Get the number of ticks recorded by the right GearBox's encoder
     * 
     * @return Number of ticks
     */
    public int getRightGearboxTicks() {
        return this.mRightGearbox.getTicks();
    }

    /**
     * Sends Subsystem telemetry data to SmartDashboard
     */
    public void outputTelemetry() {
        SmartDashboard.putNumber("DriveTrin Left Gearbox Ticks", getLeftGearboxTicks());
        SmartDashboard.putNumber("DriveTrin Right Gearbox Ticks", getRightGearboxTicks());
    }
    
    /**
     * Use DriveTrain components to load a pre-generated motiont profile
     * 
     * @param filename Motion profile filename / path
     * @param swap_paths Should left and right be swapped?
     * @param invert_gyro Is the gyro backwards?
     * @param invert_path Should this path be loaded in inverse mode?
     * 
     * @return The outpputted TankProfile
     */
    public TankTrajectory getProfile(String filename, boolean swap_paths, boolean invert_gyro, boolean invert_path) {
        logger.log("[DriveTrain] Loading motion profile: " + filename);
        return PathingHelper.loadTankProfile(filename, this.mLeftGearbox, this.mRightGearbox, this.gyro, swap_paths,
                invert_gyro, invert_path);
    }

    public void reset() {
        // Reset encoders, gyro
        // Set all outputs to 0.0
        this.mLeftGearbox.getMaster().set(0.0);
        this.mRightGearbox.getMaster().set(0.0);
    }
}
