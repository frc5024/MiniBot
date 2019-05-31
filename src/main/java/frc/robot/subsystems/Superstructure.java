package frc.robot.subsystems;

/**
 * Any information about the robot's current status should be accessed through the superstructure.
 * This class can be thought of as an interface for getting high-level data about the robot 
 * as well as high-level methods for controlling components of the robot that interact with 
 * eachother. (ex. An elevator and arm could be both controlled with a setIntakeKinematicPosition(x,y); )
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

            switch (newState) {
            case kIdle:
                newState = handleIdle(mStateChanged);
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

    /**
     * Check if the superstructure is not currently assigned to a state
     * 
     * @return Is the superstructure ready for a new state?
     */
    public boolean isStateless() {
        return mSystemState == SystemState.kIdle;
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