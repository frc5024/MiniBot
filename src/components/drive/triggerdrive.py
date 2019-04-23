import wpilib

from robotmap import config
from .drivetrain import DriveTrain

def limitTrigger(x):
    return x if x>=0 else 0

class TriggerDrive:
    drivetrain: DriveTrain

    def __init__(self):
        self.xboxcontroller = wpilib.XboxController(config["xbox_controllers"]["driver"])
    
    def execute(self):
        speed = 0
        speed += limitTrigger(self.xboxcontroller.getTriggerAxis(wpilib.interfaces.GenericHID.Hand.kRight))
        speed -= limitTrigger(self.xboxcontroller.getTriggerAxis(wpilib.interfaces.GenericHID.Hand.kLeft))
        
        rotation = self.xboxcontroller.getX(wpilib.interfaces.GenericHID.Hand.kLeft)

        self.drivetrain.arcadeDrive(speed, rotation)