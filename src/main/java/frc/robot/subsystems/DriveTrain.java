package frc.robot.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import frc.robot.commands.TriggerDrive;
import frc.robot.common.GearBox;
import frc.robot.Constants;
import frc.robot.common.SlewLimiter;
import frc.robot.common.VirtualGearShifter;

/**
 * The Subsystem in control of the robot's drivebase.
 */
public class DriveTrain extends Subsystem {
    GearBox mLeftGearbox;
    GearBox mRightGearbox;

    DifferentialDrive mDrivebase;

    private static DriveTrain mInstance = new DriveTrain();

    SlewLimiter accelerator = new SlewLimiter(Constants.accelerationStep);
    VirtualGearShifter gearShifter = new VirtualGearShifter(Constants.gearshiftZone, Constants.accelerationStep);


    public DriveTrain(){
        /* Create both gearbox objects */
        mLeftGearbox = new GearBox(new WPI_TalonSRX(Constants.leftFrontMotor), new WPI_TalonSRX(Constants.leftRearMotor));
        mRightGearbox = new GearBox(new WPI_TalonSRX(Constants.rightFrontMotor), new WPI_TalonSRX(Constants.rightRearMotor));

        /* Enable current limiting on each gearbox */
        mLeftGearbox.limitCurrent(Constants.drivetrainPeakCurrent, Constants.drivetrainHoldCurrent, Constants.drivetrainCurrentTimeout);
        mRightGearbox.limitCurrent(Constants.drivetrainPeakCurrent, Constants.drivetrainHoldCurrent, Constants.drivetrainCurrentTimeout);

        /* Create a DifferentialDrive out of each gearbox */
        mDrivebase = new DifferentialDrive(mLeftGearbox.front, mRightGearbox.front);
        mDrivebase.setSafetyEnabled(false); // Make sure the robot dosn't lock up
    }

    @Override
    /**
     * Called by WPIlib's Scheduler during initalization
     */
    public void initDefaultCommand() {
        setDefaultCommand(new TriggerDrive());
    }

    /**
     * Access the current instance of the DriveTrain
     * 
     * @return DriveTrain instance
     */
    public static DriveTrain getInstance(){
        return mInstance;
    }

    /**
     * The standard wrapper around WPIlib's joystick-based drive function
     * 
     * @param speed Forward speed (from -1.0 to 1.0)
     * @param rotation Rotation of robot (from -1.0 to 1.0)
     */
    public void arcadeDrive(double speed, double rotation) {
        mDrivebase.arcadeDrive(speed, rotation, false);
    }

    /**
     * Drive the robot with artificial acceleration and gear shifting
     */
    public void raiderDrive(double speed, double rotation) {
        /* Feed the accelerator and gearshifter */
        speed = this.gearShifter.feed(speed);
        speed = this.accelerator.feed(speed);

        /* Deal with coasting during a shift */
        setBrakes(!this.gearShifter.shouldCoast());

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
    public void arcadeDrive(double speed, double rotation, boolean is_inputs_squared){
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
    public void cheesyDrive(double speed, double rotation, boolean is_quick_turn){
        mDrivebase.curvatureDrive(speed, rotation, is_quick_turn);
    }

    /**
     * Enables or disables brake mode on all drivebase talons
     * 
     * @param on Should the brakes be enabled?
     */
    public void setBrakes(boolean on){
        NeutralMode mode = on ? NeutralMode.Brake : NeutralMode.Coast;

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
     * Shifts to high gear and emulates a clutch
     * 
     * @param high Should the gearing be set high?
     */
    public void gearShift(boolean high) {
        this.accelerator.reset();
        this.gearShifter.shift(high);
    }

    /**
     * Sends Subsystem telemetry data to SmartDashboard
     */
    public void outputTelemetry(){
        SmartDashboard.putNumber("DriveTrin Left Gearbox Ticks", getLeftGearboxTicks());
        SmartDashboard.putNumber("DriveTrin Right Gearbox Ticks", getRightGearboxTicks());
    }
}
