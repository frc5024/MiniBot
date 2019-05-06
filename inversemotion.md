# Inverse Motion Profiling
Pathfinder is not capable of following a path backwards. This leads to some problems.

## The Problem
Let's say that you want the robot to back up during a motion profile. Due to pathfinder's "feature", this is impossible. 

So here is how we did it

## The Solution
In order to follow a path backwards, it should be drawn as normal in pathweaver, then the `invert_path` flag should be set in `PathingHelper`.

This will do the following to the path:
 - Flip left for right
 - Invert all motors
 - Reverse the sensor phase of all encoders

When the path has been finished, or the `stop()` function has been called, all motors and encoders will be set back to normal settings.
