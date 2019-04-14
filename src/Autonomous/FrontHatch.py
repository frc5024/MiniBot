from wpilib.command import TimedCommand
from raiderrobotics.Motion.TankProfile import drawTrajectory

class FrontHatch(TimedCommand):
    def __init__(self, robot, timeout):
        super().__init__("Profiles to front cargo station", timeout)
        self.robot = robot

        self.requires(self.robot.DriveTrain)

        self.profile = self.robot.DriveTrain.GeneratePath(self.robot.deploy_path + "/output/fronthatch.pf1.csv")

    def initialize(self):
        self.robot.DriveTrain.ResetGyro()
        drawTrajectory(self.profile)
        pass
    
    def execute(self):
        if not self.profile.left_follower.finished:
            self.robot.DriveTrain.FollowPath(self.profile)
        else:
            self.robot.DriveTrain.Stop()

    def end(self):
        pass