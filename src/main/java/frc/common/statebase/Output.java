package frc.common.statebase;

import edu.wpi.first.wpilibj.command.Subsystem;
import frc.common.utils.RobotLogger;

public abstract class Output extends Subsystem {
    private RobotLogger logger = RobotLogger.getInstance();
    
    /** 
     * Everything should be reset here
     */
    protected void stop(){}
}