package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID;
import java.lang.Math;

import frc.robot.Robot;
import frc.robot.Constants;

public class TriggerDrive extends Command {
  XboxController driverController = Robot.mOI.driverController;

  public TriggerDrive() {
    requires(Robot.mDriveTrain);
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
  }

  private double limitTrigger(double value){
    if (value <= 0.0){
      return 0.0;
    }
    return value;
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    double speed = 0.0;
    double rotation = 0.0;

    /* Get Speed from triggers */
    speed += limitTrigger(this.driverController.getTriggerAxis(GenericHID.Hand.kRight));
    speed -= limitTrigger(this.driverController.getTriggerAxis(GenericHID.Hand.kLeft));

    /* Get rotation from joystick */
    rotation += driverController.getX(GenericHID.Hand.kLeft);
    rotation = (Math.abs(rotation) < 0.1) ? 0.0 : rotation;

    // Robot.mDriveTrain.arcadeDrive(speed, rotation);
    Robot.mDriveTrain.raiderDrive(speed, rotation);
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return false;
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
  }
}