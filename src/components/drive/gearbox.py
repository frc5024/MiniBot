import ctre
import wpilib

from .simpleencoder import SimpleEncoder_TalonSRX

class GearBox:
    def __init__(self, motors: list, encoded_motor_index = None, encoder = None, leader_motor_index = 0):
        self.motors = motors

        self.leader = self.motors[leader_motor_index]

        self.speedcontroller = wpilib.SpeedControllerGroup(*tuple(motor for motor in self.motors))

        if encoded_motor_index:
            self.encoder = SimpleEncoder_TalonSRX(self.motors[encoder_motor_index])
        else:
            self.encoder = encoder
