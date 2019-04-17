
class Toggle:
    def __init__(self, pressed, arg):
        self.callback = pressed
        self.toggle = False
        self.arg = arg
    
    def Feed(self):
        if self.callback(self.arg):
            self.toggle = not self.toggle
    
    def Check(self, if_true, if_false):
        return if_true if self.toggle else if_false