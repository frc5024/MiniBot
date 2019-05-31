package frc.robot.states;

import frc.common.statebase.State;

import frc.robot.subsystems.Superstructure.SystemState;
import frc.robot.subsystems.Superstructure.WantedState;

public class Idle extends State {
    
    @Override
    public void run(double timestamp, boolean isNew) {
        if (isNew) {
            // Set ledring to OFF
        }
    }

    /** 
     * If the user has requested a new state for the robot, 
     * set the next SystemState required to get the job done.
     * 
     * If the current state is only able to transition to certain other states,
     * they should be the only options here
     * 
     * The default state is nothing
     */
    @Override 
    public SystemState handleStateChange(WantedState want){
        switch (want) {
        default:
            return SystemState.kIdle;
        }
    }

    /**
     * This should return true if the state
     * is done it's work
     */
    @Override
    public boolean isFinished() {
        return false;
    }

    /**
     * This code will be run when it is time to move to the next state
     * it must also return the state to move to next
     */
    @Override
    public SystemState handleTransition() {
        
    }
}