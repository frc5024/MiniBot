package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import java.lang.Math;

import frc.common.control.CubicDeadband;
import frc.common.utils.RobotLogger;
import frc.robot.Constants;
import frc.robot.Robot;
import frc.robot.OI;

public class DriveControl extends Command {
  private RobotLogger logger = RobotLogger.getInstance();
  OI oi;

  CubicDeadband rotation_deadband;
  CubicDeadband speed_deadband;

  public DriveControl() {

    this.oi = OI.getInstance();

    this.rotation_deadband = new CubicDeadband(Constants.Deadbands.rotation_deadband, Constants.Deadbands.roataion_percision);
    this.speed_deadband = new CubicDeadband(0.0, Constants.Deadbands.speed_percision);
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    logger.log("[DriveControl] Starting");
  }  

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {

    /* Get Speed from triggers */
    double speed = oi.getThrottle();

    /* Get rotation from joystick */
    double rotation = oi.getTurn();
    // Deadzone the turning
    rotation = rotation_deadband.feed(rotation);

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
