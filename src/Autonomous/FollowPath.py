from wpilib.command import TimedCommand
from raiderrobotics.Motion.TankProfile import drawTrajectory
from raiderrobotics.webview.components import register, unregister, ComponentType

class FollowPath(TimedCommand):
    def __init__(self, robot, path, timeout):
        super().__init__("FollowPath", timeout)
        self.robot = robot
        register(f"FollowPath ({path['name']})", ComponentType["command"])
        self.file_path = path

        self.requires(self.robot.DriveTrain)

        self.path = self.robot.DriveTrain.GeneratePath(self.robot.deploy_path + "/output/" + path["file"])

    def initialize(self):
        self.robot.DriveTrain.ResetGyro()
        drawTrajectory(self.path)
        pass
    
    def execute(self):        
        # If we are at the end of a path, stop drivetrain and reset
        if self.path.left_follower.finished and self.path.right_follower.finished:
            self.robot.DriveTrain.Stop()
            
            print(f"Finished profile: {self.path.name}")
            self.isFinished = lambda: True
            return
        
        self.robot.DriveTrain.FollowPath(self.path)

    def end(self):
        self.robot.DriveTrain.gyro.reset()
        self.robot.DriveTrain.ZeroEncoders()
        unregister(f"FollowPath ({self.file_path['name']})")