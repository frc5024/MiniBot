/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.cscore.MjpegServer;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.cscore.VideoMode;

import frc.robot.commands.TriggerDrive;
import frc.robot.subsystems.DriveTrain;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {

  /* Sybsystems */
  public static DriveTrain mDriveTrain;
  public static OI mOI;

  /**
   * This function is run when the robot is first started up and should be
   * used for any initialization code.
   */
  @Override
  public void robotInit() {
    System.out.print("Starting CameraServer... ");
    UsbCamera camera = CameraServer.getInstance().startAutomaticCapture();
    camera.setVideoMode(VideoMode.PixelFormat.kMJPEG, 320, 240, 15);
    MjpegServer cameraServer = new MjpegServer("Main camera", Constants.cameraserverPort);
    cameraServer.setSource(camera);
    System.out.println("DONE");

    mOI = new OI();
    mDriveTrain = new DriveTrain();
  }

  /**
   * This function is called every robot packet, no matter the mode. Use
   * this for items like diagnostics that you want ran during disabled,
   * autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before
   * LiveWindow and SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
    
  }

  /**
   * This function is called once each time the robot enters Disabled mode.
   * You can use it to reset any subsystem information you want to clear when
   * the robot is disabled.
   */
  @Override
  public void disabledInit() {
    SmartDashboard.putString("Robot Mode", "DISABLED");
  }

  @Override
  public void disabledPeriodic() {
    Scheduler.getInstance().run();
  }

  @Override
  public void autonomousInit() {
    SmartDashboard.putString("Robot Mode", "AUTO");
  }

  @Override
  public void autonomousPeriodic() {
    Scheduler.getInstance().run();
  }

  @Override
  public void teleopInit() {
    SmartDashboard.putString("Robot Mode", "TELEOP");
    
  }

  @Override
  public void teleopPeriodic() {
    Scheduler.getInstance().run();
    updateSmartdashboard();
  }

  @Override
  public void testPeriodic() {
  }

  private void updateSmartdashboard(){
    mDriveTrain.outputTelemetry();
  }

}
