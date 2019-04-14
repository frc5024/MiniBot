# Robot Configuration
import wpilib
from pyfrc.physics.units import units

## DriveTrain ##
drivetrain = {
    "front_left": 1,
    "back_left": 2,
    "front_right": 3,
    "back_right": 4,
    "ticks_per_rev":360
}

## Robot Metrics ##
robot = {
    "wheel_diameter":0.5, #feet
    "max_velocity": 5,  #ft/s
    "max_acceleration": 7,
    "wheelbase_width": 1.08333 # feet
}

## Motion Profiling PID ##
pathfinder_pid = {
    "p": 0.08,
    "i": 0.0,
    "d": 0.0
}

## Xbox Controllers ##
controllers = {
    "Driver": 0,
    "Operator": 1
}

## Drive Tuning ##
drive = {
    "acceleration_limit": 0.2
}