/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

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


  /* TriggerDrive */
  public static final double accLimit = 0.15;

  /* CameraServer */
  public static final int cameraserverPort = 1181;

  /* PCM */
  public static final int pcmCanId = 11;
}
