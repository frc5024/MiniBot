package frc.robot.common;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.SpeedController;

/**
 * A GearBox is a wrapper for any pair of WPI_TalonSRX motor controllers where the first controller has an encoder attached
 */
public class GearBox{
    public WPI_TalonSRX front, rear;

    public SpeedControllerGroup speedcontroller;

    public GearBox(WPI_TalonSRX front, WPI_TalonSRX rear){
        /* Store both Talons */
        this.front = front;
        this.rear = rear;

        /* Configure the Talons */
        this.front.configFactoryDefault();
        this.rear.follow(this.front);

        /* Group the Talons together */
        // Currently broken. The internet is of no help.
        // this.speedcontroller = SpeedControllerGroup(front, rear);
    }

    public void limitCurrent(int peakCurrent, int holdCurrent, int peakDuration){
        int timeout = 0;
        this.front.configPeakCurrentLimit(peakCurrent, timeout);
        this.front.configPeakCurrentDuration(peakDuration, timeout);
        this.front.configContinuousCurrentLimit(holdCurrent, timeout);

        this.rear.configPeakCurrentLimit(peakCurrent, timeout);
        this.rear.configPeakCurrentDuration(peakDuration, timeout);
        this.rear.configContinuousCurrentLimit(holdCurrent, timeout);
    }
}