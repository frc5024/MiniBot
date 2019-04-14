#!/usr/bin/env python3

import wpilib
from commandbased import CommandBasedRobot
from wpilib.command import Scheduler

import Subsystems
import Commands

from oi import OI

class Robot(CommandBasedRobot):
    def robotInit(self):
        """
        This function is called before any other part of the robot code and should initalize subsystems
        """

        # Initalize the superclass
        super().robotInit()

        # Subsystems
        self.DriveTrain = Subsystems.DriveTrain(self)
        self.OI = OI()

        # Commands
        self.TriggerDrive = Commands.TriggerDrive(self)

    
    def teleopInit(self):
        return super().teleopInit()

    def teleopPeriodic(self):
        Scheduler.getInstance().run()

# Start the robot code if file is run as standalone script
if __name__ == "__main__":
    wpilib.run(Robot)