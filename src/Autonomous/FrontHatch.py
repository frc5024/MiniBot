from wpilib.command import TimedCommand
from raiderrobotics.Motion.TankProfile import drawTrajectory

class FrontHatch(TimedCommand):
    def __init__(self, robot, timeout):
        super().__init__("Profiles to front cargo station", timeout)
        self.robot = robot

        self.requires(self.robot.DriveTrain)

        self.profiles = []
        pathfiles = self.robot.DriveTrain.Ungroup(self.robot.deploy_path + "/pathgroups/Rocket.json")

        for path in pathfiles:
            self.profiles.append(self.robot.DriveTrain.GeneratePath(self.robot.deploy_path + "/output/" + path["file"]))

    def initialize(self):
        self.robot.DriveTrain.ResetGyro()
        drawTrajectory(self.profiles[0])
        pass
    
    def execute(self):
        if len(self.profiles) < 1:
            print("Ran out of profiles to follow")
            self.isFinished = lambda: True
            return
        
        # If we are at the end of a path, stop drivetrain and reset
        if self.profiles[0].left_follower.finished:
            self.robot.DriveTrain.Stop()
            
            print(f"Finished profile: {self.profiles[0].name}")
            self.profiles = self.profiles[1:]

            # Draw the next path in the simulator
            try:
                drawTrajectory(self.profiles[0])
            except:
                print("")
            return
        
        self.robot.DriveTrain.FollowPath(self.profiles[0])

    def end(self):
        pass
    