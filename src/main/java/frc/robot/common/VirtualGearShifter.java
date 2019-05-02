package frc.robot.common;

public class VirtualGearShifter{
    boolean isHighGear = false;
    double lowGearLimit;

    public VirtualGearShifter(double lowGearLimit){
        this.lowGearLimit = lowGearLimit;
    }

    public double feed(double value){
        if (this.isHighGear){
            return value;
        } else {
            return value * this.lowGearLimit;
        }
    }

    public void shift(boolean doShift){
        this.isHighGear = doShift;
    }

}