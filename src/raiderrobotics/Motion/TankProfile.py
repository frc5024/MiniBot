import wpilib

class TankProfile:
    left_follower = None
    right_follower = None
    left_trajectory = None
    right_trajectory = None


def drawTrajectory(profile: TankProfile):
    left = profile.left_trajectory
    right = profile.right_trajectory

    if wpilib.RobotBase.isSimulation():
            from pyfrc.sim import get_user_renderer

            renderer = get_user_renderer()
            if renderer:
                renderer.draw_pathfinder_trajectory(
                    left, color="#0000ff", offset=(-1, 0)
                )
                # renderer.draw_pathfinder_trajectory(
                #     modifier.source, color="#00ff00", show_dt=1.0, dt_offset=0.0
                # )
                renderer.draw_pathfinder_trajectory(
                    right, color="#0000ff", offset=(1, 0)
)