package frc.robot.autonomous.actions;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.command.Command;
import frc.common.utils.RobotLogger;
import frc.common.wrappers.EncoderPair;
import frc.robot.Constants;
import frc.robot.Robot;
import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.Waypoint;
import jaci.pathfinder.modifiers.TankModifier;

/**
 * This is based off Jaci's Pathfinder v1 library.
 */
public class Follow extends Command {
    private RobotLogger logger = RobotLogger.getInstance();

    private AHRS gyro;
    private Trajectory path;
    private TankModifier tank_path;
    private EncoderPair encoders;
    private boolean reverse;

    public Follow(Waypoint... waypoints) {
        this(false, waypoints);
    }

    public Follow(boolean isReverse, Waypoint[] waypoints) {
        this.gyro = Robot.mDriveTrain.getGyro();
        this.reverse = isReverse;

        // Generate path
        Trajectory.Config config = new Trajectory.Config(Trajectory.FitMethod.HERMITE_CUBIC,
                Trajectory.Config.SAMPLES_HIGH, 0.02, Constants.Robot.max_velocity, Constants.Robot.max_acceleration,
                Constants.Robot.max_jerk);

        this.path = Pathfinder.generate(waypoints, config);
        this.tank_path = new TankModifier(path).modify(Constants.Robot.wheelbase);
        
        this.encoders = new EncoderPair(tank_path);

    }

    @Override
    protected void initialize() {
        // Set encoder zeros
        encoders.left.configureEncoder(Robot.mDriveTrain.getLeftGearboxTicks(), Constants.EncoderInfo.ticks_per_rev,
                Constants.Robot.wheel_diameter / 100);
        encoders.right.configureEncoder(Robot.mDriveTrain.getRightGearboxTicks(), Constants.EncoderInfo.ticks_per_rev,
                Constants.Robot.wheel_diameter / 100);

        // Set PIDVA for both encoders
        encoders.setPIDVA(Constants.PathingPIDA.kP, Constants.PathingPIDA.kI, Constants.PathingPIDA.kD,
                1 / Constants.Robot.max_velocity, Constants.PathingPIDA.kA);
    }

    @Override
    protected void execute() {
        // Get Left and Right speeds
        double l = encoders.left
                .calculate(Robot.mDriveTrain.getLeftGearboxTicks() / Constants.EncoderInfo.ticks_per_tick);
        double r = encoders.right
                .calculate(Robot.mDriveTrain.getRightGearboxTicks() / Constants.EncoderInfo.ticks_per_tick);

        // Get current and desired heading
        double current_heading = gyro.getAngle();
        double desired_heading = Pathfinder.r2d(encoders.left.getHeading());

        // Wrap gyro values
        double angle_difference = Pathfinder.boundHalfDegrees(desired_heading - current_heading);
        angle_difference = angle_difference % 360.0;
        if (Math.abs(angle_difference) > 180.0) {
            angle_difference = (angle_difference > 0) ? angle_difference - 360 : angle_difference + 360;
        }

        // Calculate turning power
        double turn = 0.8 * (-1.0 / 80.0) * angle_difference;

        // Let the Talons do the rest
        Robot.mDriveTrain.tankDrive(l + turn, r - turn);
    }

    @Override
    protected boolean isFinished() {
        return encoders.isFinished();
    }

    @Override
    protected void end() {
        Robot.mDriveTrain.tankDrive(0, 0);
        encoders.reset();
    }

}