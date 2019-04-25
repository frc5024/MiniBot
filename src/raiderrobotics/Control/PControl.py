
class PControl:
    def __init__(self, deg_per_sec = 800, kp = 1.750):
        self.kSt = (1 / deg_per_sec)* -1
        self.kP = kp
        self.turn = 0
    
    def Feed(self, yaw):
        self.input = yaw * self.kSt
        self.turn = self.kP * (self.turn - self.input)
        return self.turn

