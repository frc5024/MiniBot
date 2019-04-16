from wpilib.command import Command
from wpilib.interfaces import GenericHID
from scipy.interpolate import interp1d

from RobotMap import controllers, drive

from raiderrobotics.Control.SlewLimiter import SlewLimiter
from raiderrobotics.Control.EncoderRecorder import Recorder, Player

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

        # Speed multiplier (used to change direction on button press)
        self.speed_multiplier = 1

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

        # Switch directions with x button
        if self.driver_controller.getXButtonReleased():
            self.speed_multiplier *= - 1

        # Pass speed through the smoother
        speed = self.smoother.Feed(speed)

        # Get rotation from joystick
        rotation += self.driver_controller.getX(GenericHID.Hand.kLeft) * -1

        # rotation *= abs(speed*0.6) + 0.5
        if speed > 0.8:
            rotation *= 0.9
        elif speed > 0.3:
            rotation *= 0.7
        else:
            rotation *= 0.5

        self.robot.DriveTrain.ArcadeDrive(speed*self.speed_multiplier*-1, rotation )