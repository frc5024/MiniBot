import ctre
import wpilib

from wpilib.command.subsystem import Subsystem
from wpilib.drive import DifferentialDrive
from wpilib.robotbase import RobotBase
from ctre.wpi_talonsrx import WPI_TalonSRX
import pathfinder as pf
from pathfinder.followers import EncoderFollower
from navx import AHRS

from RobotMap import drivetrain, robot, pathfinder_pid
from Commands.TriggerDrive import TriggerDrive

from raiderrobotics.GearBoxes.TalonGearBox import TalonGearBox
from raiderrobotics.Motion.TankProfile import TankProfile

class DriveTrain(Subsystem):
    def __init__(self, robot):
        # Initalize the superclass
        super().__init__()

        self.robot = robot

        # Robot gyro
        self.gyro = AHRS.create_spi()
        self.gyro.reset()

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

        self.left_gearbox.front.setInverted(True)
        self.right_gearbox.front.setInverted(True)

        # WPILib drive
        self.drive = wpilib.drive.DifferentialDrive(self.left_gearbox.speedcontroller, self.right_gearbox.speedcontroller)
        self.drive.setSafetyEnabled(False)
    
    def ArcadeDrive(self, speed, rotation):
        self.drive.arcadeDrive(speed, rotation, False)

    def initDefaultCommand(self):
        self.setDefaultCommand(TriggerDrive(self.robot))
    
    def GetLeftEncoder(self):
        return self.left_gearbox.front.getSelectedSensorPosition()
    
    def GetRightEncoder(self):
        return self.right_gearbox.front.getSelectedSensorPosition()
    
    def GeneratePath(self, file: str) -> TankProfile:
        # Load the profile 
        trajectory = pf.deserialize_csv(file)

        # Create the tank drive modifier
        modifier = pf.modifiers.TankModifier(trajectory).modify(float(robot["wheelbase_width"]))

        # Create class to return
        output = TankProfile()

        # Store each path
        output.left_trajectory = modifier.getLeftTrajectory()
        output.right_trajectory = modifier.getRightTrajectory()

        # Configure left encoder
        output.left_follower = pf.followers.EncoderFollower(output.left_trajectory)
        output.left_follower.configureEncoder(
            self.GetLeftEncoder(), drivetrain["ticks_per_rev"], robot["wheel_diameter"]
        )
        output.left_follower.configurePIDVA(pathfinder_pid["p"] + 0.0, pathfinder_pid["i"] + 0.0, pathfinder_pid["d"] + 0.0, 1 / robot["max_velocity"], 0)

        # Configure right encoder
        output.right_follower = pf.followers.EncoderFollower(output.right_trajectory)
        output.right_follower.configureEncoder(
            self.GetRightEncoder(), drivetrain["ticks_per_rev"], robot["wheel_diameter"]
        )
        output.right_follower.configurePIDVA(pathfinder_pid["p"] + 0.0, pathfinder_pid["i"] + 0.0, pathfinder_pid["d"] + 0.0, 1 / robot["max_velocity"], 0)

        return output
        
    
    def FollowPath(self, profile):

        # Get desired encoder location
        l = profile.left_follower.calculate(self.GetLeftEncoder())
        r = profile.right_follower.calculate(self.GetRightEncoder())

        # Get current gyro position
        gyro_heading = -self.gyro.getAngle()

        # Get desired gyro position
        desired_heading = pf.r2d(profile.left_follower.getHeading())

        angleDifference = pf.boundHalfDegrees(desired_heading - gyro_heading)
        turn = 5 * (-1.0 / 80.0) * angleDifference

        l = l + turn
        r = r - turn

        print(l)
        
        self.drive.tankDrive(-l, -r)
    
    def Stop(self):
        self.drive.tankDrive(0, 0)
    
    def ResetGyro(self):
        self.gyro.reset()