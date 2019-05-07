package frc.robot.commands;
//  _    _                       _ _     _    
// | |  | |                     | (_)   | |   
// | |__| |_   _ _ __   ___ _ __| |_ ___| | __
// |  __  | | | | '_ \ / _ \ '__| | / __| |/ /
// | |  | | |_| | |_) |  __/ |  | | \__ \   < 
// |_|  |_|\__, | .__/ \___|_|  |_|_|___/_|\_\
//         __/ | |                            
//        |___/|_|                            


import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Constants;
import frc.robot.Robot;
import frc.robot.common.control.DDRController;

public class DanceDrive extends Command {
    DDRController danceController = Robot.mOI.danceController;

    public DanceDrive() { requires(Robot.mDriveTrain);}

    @Override
    protected void initialize() {
    }

    @Override
    protected void execute() {
        double speed = 0.0;
        double rotation = 0;
        double speedMult = 0;

        if (danceController.getUp()) {
            speed = speedMult;
        }
        if (danceController.getDown()) {
            speed = speedMult * -1;
        }

        if (danceController.getLeft()) {
            rotation -= 0.3;
        }

        if (danceController.getRight()) {
            rotation += 0.3;
        }

        if(danceController.getPlus()) {
            if (speed < 1.0 - Constants.speedInc) {
                speedMult += Constants.speedInc;
            }
        }


        if(danceController.getMinus()) {
            if (speed > 0 + Constants.speedInc) {
                speedMult -= Constants.speedInc;
            }
        }



        Robot.mDriveTrain.raiderDrive(speed, rotation);

    }

    @Override
    protected boolean isFinished() {
        return false;
    }

    @Override
    protected void end() {
    }


    @Override
    protected void interrupted() {
    }

}
