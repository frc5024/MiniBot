import ctre
import wpilib

from wpilib.command.subsystem import Subsystem
from wpilib.drive import DifferentialDrive
from wpilib.robotbase import RobotBase
from ctre.wpi_talonsrx import WPI_TalonSRX

from RobotMap import drivetrain
from Commands.TriggerDrive import TriggerDrive

from raiderrobotics.GearBoxes.TalonGearBox import TalonGearBox

class DriveTrain(Subsystem):
    def __init__(self, robot):
        # Initalize the superclass
        super().__init__()

        self.robot = robot

        # Create gearboxes for each side of robot
        self.left_gearbox = TalonGearBox(drivetrain["front_left"], drivetrain["back_left"])
        self.right_gearbox = TalonGearBox(drivetrain["front_right"], drivetrain["back_right"])

        # Enable the breaks on the motors by default
        self.left_gearbox.front.setNeutralMode(ctre.NeutralMode.Brake)
        self.left_gearbox.back.setNeutralMode(ctre.NeutralMode.Brake)
        self.right_gearbox.front.setNeutralMode(ctre.NeutralMode.Brake)
        self.right_gearbox.back.setNeutralMode(ctre.NeutralMode.Brake)

        # Configure encoders
        if RobotBase.isReal():
            self.left_gearbox.front.configFactoryDefault()
            self.right_gearbox.front.configFactoryDefault()

        self.left_gearbox.front.setSensorPhase(True)
        self.right_gearbox.front.setSensorPhase(True)

        # WPILib drive
        self.drive = wpilib.drive.DifferentialDrive(self.left_gearbox.speedcontroller, self.right_gearbox.speedcontroller)
    
    def ArcadeDrive(self, speed, rotation):
        self.drive.arcadeDrive(speed, rotation, False)

    def initDefaultCommand(self):
        self.setDefaultCommand(TriggerDrive(self.robot))
    
    def GetLeftEncoder(self):
        return self.left_gearbox.front.getSensorCollection().getQuadraturePosition()
    
    def GetLeftEncoder(self):
        return self.right_gearbox.front.getSensorCollection().getQuadraturePosition()