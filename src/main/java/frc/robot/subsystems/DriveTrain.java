package frc.robot.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

import frc.robot.commands.TriggerDrive;
import frc.robot.common.GearBox;
import frc.robot.Constants;

/**
 * An example subsystem.  You can replace me with your own Subsystem.
 */
public class DriveTrain extends Subsystem {
  GearBox mLeftGearbox;
  GearBox mRightGearbox;

  DifferentialDrive mDrivebase;

  private static DriveTrain mInstance = new DriveTrain();


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
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    setDefaultCommand(new TriggerDrive());
  }

  public void arcadeDrive(double speed, double rotation){
    mDrivebase.arcadeDrive(speed, rotation, false);
  }

  public void arcadeDrive(double speed, double rotation, boolean isInputsSquared){
    mDrivebase.arcadeDrive(speed, rotation, isInputsSquared);
  }
}
