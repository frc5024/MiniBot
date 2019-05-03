package frc.robot.common;

import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.PathfinderFRC;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.followers.EncoderFollower;
import edu.wpi.first.wpilibj.Notifier;
import com.kauailabs.navx.frc.AHRS;

import frc.robot.common.TankTrajectory;
import frc.robot.common.GearBox;

public class PathingHelper{
    public TankTrajectory loadTankProfile(String filename, GearBox left_gearbox, GearBox right_gearbox, AHRS gyro, boolean swap_paths, boolean invert_gyro){
        /* Load file into a left and right path */
        Trajectory left_trajectory;
        Trajectory right_trajectory;

        try{
            if (swap_paths){
                left_trajectory = PathfinderFRC.getTrajectory(filename + ".right");
                right_trajectory = PathfinderFRC.getTrajectory(filename + ".left");
            } else {
                left_trajectory = PathfinderFRC.getTrajectory(filename + ".left");
                right_trajectory = PathfinderFRC.getTrajectory(filename + ".right");
            }
        }  catch (Exception e) {
            System.out.println("FATAL: Unable to load trajectory "+ filename +"!");
            return new TankTrajectory(left_gearbox, right_gearbox, gyro, 0.02, false);
        }

        /* Create the object to be outputted */
        TankTrajectory output = new TankTrajectory(left_gearbox, right_gearbox, gyro, left_trajectory.get(0).dt, invert_gyro);

        /* Initialize the encoder followers */
        output.left_encoderfollower = new EncoderFollower(left_trajectory);
        output.right_encoderollower = new EncoderFollower(right_trajectory);

        return output;
    }
}