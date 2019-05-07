package frc.robot;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class Constants {

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
  public static final int cameraserverPort = 1181;

  /* Speed Increment for Dance Controller */
  public static final double speedInc = 0.1;


  /* PCM */
  public static final int pcmCanId = 11;
}
