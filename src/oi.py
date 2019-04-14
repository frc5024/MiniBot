import wpilib

from RobotMap import controllers

class OI:
    def __init__(self):
        self.driver_controller = wpilib.XboxController(controllers["Driver"])
        self.operator_controller = wpilib.XboxController(controllers["Operator"])
