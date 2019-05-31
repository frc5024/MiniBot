package frc.common.statebase;

import edu.wpi.first.wpilibj.command.Command;
import frc.common.utils.RobotLogger;

public abstract class Input extends Command {
    private RobotLogger logger = RobotLogger.getInstance();
    
    /**
     * This is called at the end of the Input's final loop
     * 
     * Everything should be reset here
     */
    protected void neutural(){}
}