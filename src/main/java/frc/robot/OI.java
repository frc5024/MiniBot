/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {
  //// CREATING BUTTONS
  // One type of button is a joystick button which is any button on a
  //// joystick.
  // You create one by telling it which joystick it's on and which button
  // number it is.
  // Joystick stick = new Joystick(port);
  // Button button = new JoystickButton(stick, buttonNumber);

  // There are a few additional built in buttons you can use. Additionally,
  // by subclassing Button you can create custom triggers and bind those to
  // commands the same as any other Button.

  //// TRIGGERING COMMANDS WITH BUTTONS
  // Once you have a button, it's trivial to bind it to a button in one of
  // three ways:

  // Start the command when the button is pressed and let it run the command
  // until it is finished as determined by it's isFinished method.
  // button.whenPressed(new ExampleCommand());

  // Run the command while the button is being held down and interrupt it once
  // the button is released.
  // button.whileHeld(new ExampleCommand());

  // Start the command when the button is released and let it run the command
  // until it is finished as determined by it's isFinished method.
  // button.whenReleased(new ExampleCommand());

  // Instance
  private static OI instance = null;


  // Constrollers
  public XboxController driverController = new XboxController(0);

  /**
   * Get the current OI instance
   * 
   * @return Current OI instance
   */
  public static OI getInstance() {
    if (instance == null) {
      instance = new OI();
    }

    return instance;
  }

  /**
   * Limits a trigger to positive values only. This allows the use of a steam controller for driving
   */
  private double limitTrigger(double value){
    if (value <= 0.0){
      return 0.0;
    }
    return value;
  }

  /**
   * Get the DriveTrain throttle value from driverstation
   * 
   * @return Throttle (from -1.0 to 1.0)
   */
  public double getThrottle() {
    double speed = 0.0;

    speed += limitTrigger(this.driverController.getTriggerAxis(GenericHID.Hand.kRight));
    speed -= limitTrigger(this.driverController.getTriggerAxis(GenericHID.Hand.kLeft));

    return speed;
  }

  /**
   * Get the DriveTrain turn rate value from driverstation
   * 
   * @return Turn rate (from -1.0 to 1.0)
   */
  public double getTurn() {
    return this.driverController.getX(GenericHID.Hand.kLeft);
  }

  /**
   * Get the Gyro-based pivoting input value from driverstation
   * 
   * @return Pivot rate (from -1.0 to 1.0)
   */
  public double getPivot() {
    return this.driverController.getX(GenericHID.Hand.kRight);
  }
}
