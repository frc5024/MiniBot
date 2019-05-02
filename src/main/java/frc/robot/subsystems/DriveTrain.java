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
  GearBox leftGearbox;
  GearBox rightGearbox;

  DifferentialDrive drivebase;


  public DriveTrain(){
    /* Create both gearbox objects */
    leftGearbox = new GearBox(new WPI_TalonSRX(Constants.leftFrontMotor), new WPI_TalonSRX(Constants.leftRearMotor));
    rightGearbox = new GearBox(new WPI_TalonSRX(Constants.rightFrontMotor), new WPI_TalonSRX(Constants.rightRearMotor));

    /* Create a DifferentialDrive out of each gearbox */
    drivebase = new DifferentialDrive(leftGearbox.front, rightGearbox.front);
    drivebase.setSafetyEnabled(false); // Make sure the robot dosn't lock up
  }

  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    setDefaultCommand(new TriggerDrive());
  }

  public void arcadeDrive(double speed, double rotation){
    drivebase.arcadeDrive(speed, rotation, false);
  }

  public void arcadeDrive(double speed, double rotation, boolean isInputsSquared){
    drivebase.arcadeDrive(speed, rotation, isInputsSquared);
  }
}
