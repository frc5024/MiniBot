import ctre
import wpilib

from ctre.wpi_talonsrx import WPI_TalonSRX

class TalonGearBox:
    def __init__(self, front, back):
        # Create Talons
        self.front = WPI_TalonSRX(front)
        self.back = WPI_TalonSRX(back)

        # Force back Talon to follow front
        self.back.follow(self.front)

        # SpeedController with control over the entire gearbox
        self.speedcontroller = wpilib.SpeedControllerGroup(self.front, self.back)