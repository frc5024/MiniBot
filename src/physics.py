from pyfrc.physics.drivetrains import four_motor_drivetrain

from pyfrc.physics import motor_cfgs, tankmodel
from pyfrc.physics.units import units

from RobotMap import drivetrain

class PhysicsEngine:

    def __init__(self, controller):
        # Initialize the Sim and Gyro.
        self.controller = controller
        self.controller.add_device_gyro_channel('navxmxp_spi_4_angle')        
        # self.drivetrain = tankmodel.TankModel.theory(
        #     motor_cfgs.MOTOR_CFG_CIM,           # motor configuration
        #     70 * units.lbs,                    # robot mass
        #     10,                              # drivetrain gear ratio
        #     2,                                  # motors per side
        #     13 * units.inch,                # robot wheelbase
        #     32 * units.inch,                    # robot width
        #     32 * units.inch,                  # robot length
        #     6 * units.inch                  # wheel diameter
        # )
    """
        Keyword arguments:
        hal_data -- Data about motors and other components.
        now -- Current time in milliseconds
        tm_diff -- Difference between current time and time when last checked
    """

    def update_sim(self, hal_data, now, tm_diff):

        # Simulate the drivetrain motors.
        lf_motor = hal_data['CAN'][drivetrain["front_left"]]['value']
        lr_motor = hal_data['CAN'][drivetrain["back_left"]]['value']
        rf_motor = hal_data['CAN'][drivetrain["front_right"]]['value']
        rr_motor = hal_data['CAN'][drivetrain["back_right"]]['value']

        # Simulate movement.
        speed, rotation = four_motor_drivetrain(lr_motor, rr_motor, lf_motor, rf_motor, speed=10)
        self.controller.drive(speed, rotation / 1.5, tm_diff)

        # x, y, angle = self.drivetrain.get_distance(lf_motor, rf_motor, tm_diff)
        # self.controller.distance_drive(x, y, angle)

        # Simulate encoders (NOTE: These values have not been calibrated yet.)
        hal_data['CAN'][drivetrain["front_left"]]['quad_position'] -= int(lf_motor / 5 * drivetrain["ticks_per_rev"])
        hal_data['CAN'][drivetrain["front_right"]]['quad_position'] += int(rf_motor /5 * drivetrain["ticks_per_rev"])