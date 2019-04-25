import wpilib

from wpilib.command import Command
from wpilib.interfaces import GenericHID
from scipy.interpolate import interp1d

from RobotMap import controllers, drive

from raiderrobotics.Control.SlewLimiter import SlewLimiter
from raiderrobotics.Control.PControl import PControl
from raiderrobotics.Control.EncoderRecorder import Recorder, Player
from raiderrobotics.webview.components import register, unregister, ComponentType
from raiderrobotics.Toggle import Toggle

simulation_map = interp1d([-1,1], [0, 1])

zeroLimit = lambda x: x if x >= 0 else 0

class TriggerDrive(Command):
    def __init__(self, robot):
        # Configure and init the command and required subsystems
        super().__init__("TriggerDrive")
        self.robot = robot
        self.requires(self.robot.DriveTrain)

        # Register command in components list
        register("TriggerDrive", ComponentType["command"])

        # Set up xbox controller
        self.driver_controller = self.robot.OI.driver_controller

        # Set acceleration limiter
        self.smoother = SlewLimiter(drive["acceleration_limit"])

        # Speed multiplier (used to change direction on button press)
        self.speed_multiplier = 1
        self.rotation_multiplier = 1

        self.movement_limit = Toggle(self.driver_controller.getBumperReleased, GenericHID.Hand.kRight)

    def execute(self):
        # Reset speed and rotation for safety
        rotation = 0.0
        speed = 0.0

        # Feed the movement_limiter Toggle
        self.movement_limit.Feed()

        # Store trigger readings in seprate values for easy use in simulation edge case checks
        speed += zeroLimit(self.driver_controller.getTriggerAxis(GenericHID.Hand.kRight))
        speed -= zeroLimit(self.driver_controller.getTriggerAxis(GenericHID.Hand.kLeft))

        # Get rotation from joystick
        rotation += self.driver_controller.getX(GenericHID.Hand.kLeft) * -1

        # Saftey checks for simulations on @ewpratten's laptop with a steam controller
        # if left_trigger < 0:
        #     left_trigger = 0.0

        # Get speed from differential of triggers
        # speed += right_trigger
        # speed -= left_trigger

        # Switch directions with x button
        if self.driver_controller.getXButtonReleased():
            self.speed_multiplier *= - 1

        # Pass speed through the smoother
        speed = self.smoother.Feed(speed)

        # Limit the rotation based on speed (on the simulator)
        if wpilib.RobotBase.isSimulation() and not self.driver_controller.getBumper(GenericHID.Hand.kLeft):
            if speed > 0.8:
                rotation *= 0.8
            elif speed > 0.3:
                rotation *= 0.6
            else:
                rotation *= 0.4

        # Switch to high-percision driving at low speeds
        # if abs(speed) > 0.9:
        #     self.robot.DriveTrain.CheesyDrive(speed*self.speed_multiplier*-1, rotation, False )
        # else:
        self.robot.DriveTrain.ArcadeDrive(speed * self.speed_multiplier * -1 * self.movement_limit.Check(0.7, 1), rotation * self.movement_limit.Check(0.6, 1))
    
    def end(self):
        unregister("TriggerDrive")