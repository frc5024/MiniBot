package frc.common.statebase;

import frc.common.utils.RobotLogger;

import frc.robot.subsystems.Superstructure.SystemState;
import frc.robot.subsystems.Superstructure.WantedState;

public abstract class State {
    private RobotLogger logger = RobotLogger.getInstance();

    public abstract void run(double timestamp, boolean isNew);
    
    public abstract SystemState handleStateChange(WantedState want);

    public abstract boolean isFinished();

    public abstract SystemState handleTransition();
    
}