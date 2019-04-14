from wpilib.command import Command
from wpilib.interfaces import GenericHID
from scipy.interpolate import interp1d

from RobotMap import controllers, drive

from raiderrobotics.Control.SlewLimiter import SlewLimiter

simulation_map = interp1d([-1,1], [0, 1])

class TriggerDrive(Command):
    def __init__(self, robot):
        # Configure and init the command and required subsystems
        super().__init__("TriggerDrive")
        self.robot = robot
        self.requires(self.robot.DriveTrain)

        # Set up xbox controller
        self.driver_controller = self.robot.OI.driver_controller

        # Set acceleration limiter
        self.smoother = SlewLimiter(drive["acceleration_limit"])

    def execute(self):
        # Reset speed and rotation for safety
        rotation = 0.0
        speed = 0.0

        # Store trigger readings in seprate values for easy use in simulation edge case checks
        right_trigger = self.driver_controller.getTriggerAxis(GenericHID.Hand.kRight)
        left_trigger = self.driver_controller.getTriggerAxis(GenericHID.Hand.kLeft)

        # Saftey checks for simulations on @ewpratten's laptop with a steam controller
        if left_trigger < 0:
            left_trigger = 0.0

        # Get speed from differential of triggers
        speed += right_trigger
        speed -= left_trigger

        # Pass speed through the smoother
        speed = self.smoother.Feed(speed)

        # Get rotation from joystick
        rotation += self.driver_controller.getX(GenericHID.Hand.kLeft) * -1
        
        print(self.robot.DriveTrain.GetLeftEncoder())

        self.robot.DriveTrain.ArcadeDrive(speed, rotation)