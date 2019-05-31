package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Timer;
import frc.common.utils.RobotLogger;

/**
 * Any information about the robot's current status should be accessed through the superstructure.
 * This class can be thought of as an interface for getting high-level data about the robot 
 * as well as high-level methods for controlling components of the robot that interact with 
 * eachother. 
 * 
 * ex. An elevator and arm could be both controlled with a setIntakeKinematicPosition(x,y);
 * this function would then follow the correct set of states to get the job done. To move the elevator,
 * it might call a setSetpoint(), then a feed() untill the error is within the threshold. This could be checked with a 
 * isAtThreshold()
 * 
 * This file is in the subsystem packge so that it can access protected information from each subsystem.
 * 
 * This is a state machine
 */
public class Superstructure {
    // State that the user wants
    public enum WantedState {
        kIdle
    }
    
    // Internal state of robot
    public enum SystemState {
        kIdle
    }

    // Wanted method of controlling the drivetrain
    public enum WantedDriveMethod {
        kDefault,
        kArcade,
        kCurvature
    }

    // Static var for holding the current instance
    private static Superstructure instance = null;
    private RobotLogger logger = RobotLogger.getInstance();

    // States
    public WantedState mWantedState;
    public SystemState mSystemState;

    // Other wants
    public WantedDriveMethod mDriveMethod;

    // Data about state
    private boolean mStateChanged = true;

    // All subsystems involved in, or accessed by the superstructure
    private DriveTrain mDriveTrain = DriveTrain.getInstance();
    
    public static Superstructure getInstance() {
        if (instance == null) {
            instance = new Superstructure();
        }

        return instance;
    }

    /**
     * This is run periodically while the robot is running
     * 
     * This is not to be confused with the conventional subsystem interface.
     * For simple tasks, the usual interface must be used. But for any task that 
     * can be controlled by more than one command, or requires multiple subsystems 
     * to work together, that code should be spun off here.
     */
    public void periodic(double timestamp) {
        synchronized (this) {
            SystemState newState = mSystemState;

            // Run the correct handler for new state
            switch (newState) {
            case kIdle:
                newState = handleIdle(mStateChanged);
            }

            // Deal with a state change
            if (newState != mSystemState) {
                logger.log("Superstructure state " + mSystemState + " to " + newState + " Timestamp: "
                        + Timer.getFPGATimestamp());
                mSystemState = newState;
                mStateChanged = true;
            } else {
                mStateChanged = false;
            }
        }
    }
    
    /* States */
    private SystemState handleIdle(boolean stateChanged) {
        // Do required work for this state
        if (stateChanged) {
            // Set ledring to OFF
        }
        
        /** 
         * If the user has requested a new state for the robot, 
         * set the next SystemState required to get the job done.
         * 
         * The default state is nothing
         */
        switch (mWantedState) {
        default:
            return SystemState.kIdle;
        }
    
    }
    
    /* Getters */

    /**
     * Check if the robot is currently driving
     * 
     * @return Is the robot driving
     */
    public boolean isDriving() {
        return (mDriveTrain.is_moving || mDriveTrain.is_turning);
    }

    /* State-independant control */

    /**
     * Control the robot's drivetrain
     */
    public void drive(double throttle, double rotation, boolean mode) {
        // Call the appropriate function for wanted drive method
        switch (mDriveMethod) {
        case kArcade:
            mDriveTrain.arcadeDrive(throttle, rotation, mode);
            break;
        case kCurvature:
            mDriveTrain.cheesyDrive(throttle, rotation, mode);
            break;
        default:
            mDriveTrain.raiderDrive(throttle, rotation);
            break;
        }
    }



}