package frc.robot.subsystems;

/**
 * Any information about the robot's current status should be accessed through the superstructure.
 * This class can be thought of as an interface for getting high-level data about the robot 
 * as well as high-level methods for controlling components of the robot that interact with 
 * eachother. (ex. An elevator and arm could be both controlled with a setIntakeKinematicPosition(x,y); )
 */
public class Superstructure {
    public class WantedState {
        boolean vision_enabled = false;
    }

    // Static var for holding the current instance
    private static Superstructure instance = null;

    // Wanted state
    public WantedState mWantedState = new WantedState();

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
    public void periodic() {
        
    }

    /**
     * Check if the robot is currently driving
     * 
     * @return Is the robot driving
     */
    public boolean isDriving() {
        return (mDriveTrain.is_moving || mDriveTrain.is_turning);
    }




}