package frc.robot.autonomous.actions;

import frc.common.wrappers.OneShotCommand;
import frc.robot.Robot;

public class Hold extends OneShotCommand {

    @Override
    protected void doOnce() {
        Robot.mDriveTrain.stop();
    }
    
}