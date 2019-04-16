from wpilib.command.commandgroup import CommandGroup
from wpilib.command.waitcommand import WaitCommand

import Autonomous

import json

class PathGroup(CommandGroup):
    def __init__(self, robot, path_config):
        super().__init__("Grouped Paths")

        paths = json.load(open(robot.deploy_path + path_config, "r"))["paths"]

        for path in paths:
            self.addSequential(Autonomous.FollowPath(robot, path, 15))
            self.addSequential(WaitCommand(timeout=path["pause"]))
    
    def end(self):
        print("Finished following path group")