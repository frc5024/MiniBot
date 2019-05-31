package frc.robot.subsystems;

import frc.common.utils.RobotLogger;
import frc.common.utils.RobotLogger.Level;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Solenoid;

import frc.robot.Constants;

/**
 * The Subsystem in control of the robot's ringlight
 * 
 * Usage:
 *  To control the Ledring, a state must be requested via setWantedState();
 *  this will be completed via the update() function that is called in the robot's periodic loop
 */
public class Ledring extends Subsystem {
    public enum WantedState {
        kOff,
        kSolid
    }

    private static Ledring instance = null;
    private final RobotLogger logger = RobotLogger.getInstance();

    private WantedState mWantedState;

    private Solenoid led;

    protected boolean isEnabled = false;

    public Ledring(){
        logger.log("[Ledring] Constructing \"solenoid\" for led control", Level.kRobot);
        this.led = new Solenoid(Constants.PCM.ledring);
    }

    @Override
    /**
     * Called by WPIlib's Scheduler during initalization
     */
    public void initDefaultCommand() {
        // setDefaultCommand(new TriggerDrive());
    }

    public static Ledring getInstance() {
        if (instance == null) {
            instance = new Ledring();
        }

        return instance;
    }

    public void setWantedState(WantedState state) {
        mWantedState = state;
        logger.log("[Ledring] Wanted state set to: " + state);
    }

    /**
     * When called, will update to display the wanted state. 
     * The reason this subsystem is updated, and not directly 
     * controlled, is that it is still used while the robot is disabled.
     */
    public void update() {
        switch (mWantedState) {
        case kSolid:
            led.set(true);
            break;
        default:
            led.set(false);
            break;
        }
    }

    /**
     * Sends Subsystem telemetry data to SmartDashboard
     */
    public void outputTelemetry() {
        SmartDashboard.putBoolean("Ledring enabled", isEnabled);
    }
    
    public void reset() {
        
    }
}
