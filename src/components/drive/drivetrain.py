import wpilib
import ctre
from wpilib.drive import DifferentialDrive

from .gearbox import GearBox
from robotmap import config

class DriveTrain:
    def __init__(self):
        self.left_gearbox = GearBox(
            [
                ctre.wpi_talonsrx.WPI_TalonSRX(config["drivetrain"]["motors"]["lf"]), 
                ctre.wpi_talonsrx.WPI_TalonSRX(config["drivetrain"]["motors"]["lr"])
            ], 
                encoded_motor_index=0, 
                leader_motor_index=0
            )
        self.right_gearbox = GearBox(
            [
                ctre.wpi_talonsrx.WPI_TalonSRX(config["drivetrain"]["motors"]["rf"]), 
                ctre.wpi_talonsrx.WPI_TalonSRX(config["drivetrain"]["motors"]["rr"])
            ], 
                encoded_motor_index=0, 
                leader_motor_index=0
            )
        
        self.left_gearbox.speedcontroller.setInverted(True)
        self.right_gearbox.speedcontroller.setInverted(True)
        
        self.drivebase = DifferentialDrive(self.left_gearbox.speedcontroller, self.right_gearbox.speedcontroller)
        self.drivebase.setSafetyEnabled(False)
    
    def arcadeDrive(self, speed, rotation, is_squared = False):
        self.drivebase.arcadeDrive(speed, rotation, is_squared)
        