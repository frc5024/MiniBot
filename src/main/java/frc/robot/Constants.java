package frc.robot;

import frc.common.utils.FileUtils;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class Constants {
	public static final String deploy_path = FileUtils.getDeployPath();

	/* DriveTrain */
	public class DriveTrain {
		public static final int leftFrontMotor = 1;
		public static final int leftRearMotor = 2;
		public static final int rightFrontMotor = 3;
		public static final int rightRearMotor = 4;

		public static final int peakCurrent = 35;
		public static final int holdCurrent = 33;
		public static final int currentTimeout = 30;
	}

	public class EncoderInfo {
		public static final int ticks_per_rev = 360 * 4;//360;
		public static final int ticks_per_tick = 1;//4;  // Leave this at 4 unless something breaks // Something broke
	}

	public class MotionProfile {
		public static final double kP = 1.8;
		public static final double kI = 0.0;
		public static final double kD = 0.0;
		public static final double kA = 0.0;
	}

	public class Robot {
		public static final double max_velocity = 3.9; // m/s
		public static final double max_jerk = 70; // m/s
		public static final double max_acceleration = 2.552; // m/s2
		public static final double wheelbase = 0.5; // m
		public static final double wheel_diameter = 20.32; // cm
		public static final double wheel_circ = Math.PI * wheel_diameter; // cm
	}

	public class PeriodicTiming {
		public static final double robot_period = 0.02;
		public static final double field_period = 0.04;
		public static final double logging_period = 0.02;
		public static final double vision_thread = 0.01;
		public static final double net_monitor_period = 0.01;
    public static final double nt_period = 0.01;
    public static final double subsystem_looper = 0.02;
	}

	public class Deadbands {
		public static final double rotation_deadband = 0.1;
		public static final double roataion_percision = 0.2;
		public static final double speed_percision = 0.1;
		public static final double slider_deadband = 0.1;
	}

	/* DriveControl */
	public static final double accelerationStep = 0.2;

	/* CameraServer */
	public class MainCamera {
		public static final String name = "Main Camera";
		public static final int http_port = 1184;
		public static final double fov = 60; // degrees
	}

	/* PCM */
	public class PCM {
		public static final int can_id = 11;
		public static final int ledring = 0;
	}

  /**
   * The RoboRIO's DIO ports
   */
	public class DIO {
	}

	// How many cycles per strobe of the Ledring
	public static final int ledring_strobe_rate = 25;

	public class PathingPIDA {
		public static final double kP = 1.0;
		public static final double kI = 0.0;
		public static final double kD = 0.0;
		public static final double kA = 0.0;
	}

}
