package frc.robot.autonomous;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import frc.robot.autonomous.sequences.HoldPosition;

public class AutoChooser {
    SendableChooser<CommandGroup> chooser = new SendableChooser<CommandGroup>();

    public AutoChooser() {
        chooser.setDefaultOption("Do Nothing", new HoldPosition());
    }

    /**
     * Get the selected CommandGroup
     * 
     * @return Driver's selection
     */
    public CommandGroup getSelection() {
        return chooser.getSelected();
    }
}