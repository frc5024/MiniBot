package frc.robot.autonomous.sequences;

import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.robot.autonomous.actions.Hold;

public class HoldPosition extends CommandGroup {
    public HoldPosition() {
        addSequential(new Hold());
    }
}