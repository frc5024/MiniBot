import ctre

class SimpleEncoder_TalonSRX:
    def __init__(self, talon: ctre.wpi_talonsrx.WPI_TalonSRX, phase=True):
        self.talon = talon

        self.talon.configFactoryDefault()
        self.talon.setSensorPhase(phase)

        self.offset = 0
    
    def get(self):
        return self.talon.getSelectedSensorPosition() - self.offset
    
    def reset(self):
        self.offset = self.get()