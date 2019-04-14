class SlewLimiter:
    def __init__(self, limit):
        self.limit = limit
        self.output = 0.0
    
    def Feed(self, value):
        error = value - self.output
        if error > self.limit:
            error = self.limit
        elif error < (self.limit * -1):
            error = self.limit * -1
        self.output += error
        return self.output
