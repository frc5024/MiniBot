package frc.robot;

import edu.wpi.first.wpilibj.Filesystem;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class Constants {
  public static final String deploy_path = Filesystem.getDeployDirectory().getPath() + "/";

  /* DriveTrain */
  public static final int leftFrontMotor = 1;
  public static final int leftRearMotor = 2;
  public static final int rightFrontMotor = 3;
  public static final int rightRearMotor = 4;

  public static final int drivetrainPeakCurrent = 35;
  public static final int drivetrainHoldCurrent = 33;
  public static final int drivetrainCurrentTimeout = 30;

  /* TriggerDrive */
  public static final double accelerationStep = 0.15;
  public static final double gearshiftZone = 0.75;

  /* CameraServer */
  public class MainCamera {
    public static final String name = "Main Camera";
    public static final int http_port = 1181;
  }

  /* PCM */
  public static final int pcmCanId = 11;
}
