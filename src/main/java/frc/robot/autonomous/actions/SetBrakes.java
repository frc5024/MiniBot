package frc.robot.autonomous.actions;

import frc.common.control.PID;
import frc.common.utils.RobotLogger;
import frc.common.wrappers.OneShotCommand;
import frc.robot.Constants;
import frc.robot.Robot;

public class SetBrakes extends OneShotCommand {
    private RobotLogger logger = RobotLogger.getInstance();

    private boolean enabled;

    public SetBrakes(boolean enabled) {
        this.enabled = enabled;

        logger.log("[SetBrakes] Set to change brakes to: "+ enabled);
    }
    
    @Override
    protected void doOnce() {
        Robot.mDriveTrain.setBrakes(enabled);
    }
}