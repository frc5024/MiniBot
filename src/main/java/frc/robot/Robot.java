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

public class Robot extends TimedRobot {

  /* Sybsystems */
  public static DriveTrain mDriveTrain;
  public static OI mOI;

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

  @Override
  public void robotPeriodic() {
    
  }


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
