#!/usr/bin/env python3

import wpilib
from commandbased import CommandBasedRobot
from wpilib.command import Scheduler
import os.path
from robotpy_ext.autonomous import AutonomousModeSelector

from raiderrobotics.webview.httpserver import HTTPServer

import Subsystems
import Commands
import Autonomous

from RobotMap import webview

from oi import OI

class Robot(CommandBasedRobot):
    def robotInit(self):
        """
        This function is called before any other part of the robot code and should initalize subsystems
        """

        # Initalize the superclass
        super().robotInit()

        # deploy location
        self.deploy_path = os.path.dirname(__file__) + "/deploy"
        print(f"Deploy Path: {self.deploy_path}")

        # Start webserver
        try:
            self.webview = HTTPServer(webview["port"])
            self.webview_enabled = True
        except:
            print("Unable to start webserver. The port is probably in use")
            self.webview_enabled = False
        
        if self.webview_enabled:
            self.webview.Start()

        # Subsystems
        self.DriveTrain = Subsystems.DriveTrain(self)
        self.OI = OI()
        

    def autonomousInit(self):
        # self.FrontHatch = Autonomous.PathGroup(self, "/pathgroups/Rocket.json")
        # self.FrontHatch.start()
        pass
        
    def autonomousPeriodic(self):
        Scheduler.getInstance().run()

    
    def teleopInit(self):
        self.TriggerDrive = Commands.TriggerDrive(self)
        return super().teleopInit()

    def teleopPeriodic(self):
        Scheduler.getInstance().run()

# Start the robot code if file is run as standalone script
if __name__ == "__main__":
    wpilib.run(Robot)