/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.command.TimedCommand;
import frc.robot.Robot;
import frc.common.wrappers.TankTrajectory;
import frc.robot.Constants;

/**
 * Follow a basic motion profile
 */
public class BasicPath extends TimedCommand {
  TankTrajectory path;

  /**
   * Add your docs here.
   */
  public BasicPath(String filename, boolean is_inverse, double timeout) {
    super(timeout);
    requires(Robot.mDriveTrain);

    /* Load the profile */
    this.path = Robot.mDriveTrain.getProfile(filename, false, false, is_inverse);

    /* Configure the follower */
    this.path.configure(Robot.mDriveTrain.getLeftGearboxTicks(), Robot.mDriveTrain.getRightGearboxTicks(),
        Constants.EncoderInfo.ticks_per_rev, (int) Constants.EncoderInfo.wheel_diameter, Constants.Robot.max_velocity);
    this.path.setPIDA(Constants.MotionProfile.kP, Constants.MotionProfile.kI, Constants.MotionProfile.kD, Constants.MotionProfile.kA);
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    this.path.start();
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
  }

  // Called once after timeout
  @Override
  protected void end() {
    this.path.stop();
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
    this.path.stop();
  }

  @Override
  protected boolean isFinished() {
    return isTimedOut() || this.path.isFinished();
  }
}
