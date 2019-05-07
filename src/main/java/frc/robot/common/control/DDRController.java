package frc.robot.common.control;
//  _    _                       _ _     _    
// | |  | |                     | (_)   | |   
// | |__| |_   _ _ __   ___ _ __| |_ ___| | __
// |  __  | | | | '_ \ / _ \ '__| | / __| |/ /
// | |  | | |_| | |_) |  __/ |  | | \__ \   < 
// |_|  |_|\__, | .__/ \___|_|  |_|_|___/_|\_\
//         __/ | |                            
//        |___/|_|                            


import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

public class DDRController {

    Joystick joystick;


    // Directional Controls
    private JoystickButton up_button = new JoystickButton(joystick, 13);
    private JoystickButton down_button = new JoystickButton(joystick, 15 );
    private JoystickButton left_button = new JoystickButton(joystick, 16);
    private JoystickButton right_button = new JoystickButton(joystick, 14);

    // Plus and Minus Buttons
    private JoystickButton plus_button = new JoystickButton(joystick, 10);
    private JoystickButton minus_button = new JoystickButton(joystick, 8);

    // A and B Buttons
    private JoystickButton a_button = new JoystickButton(joystick, 2);
    private JoystickButton b_button = new JoystickButton(joystick, 3);


    public DDRController(int joyId) {
        this.joystick = new Joystick(joyId);
    }

    public boolean getUp(){
        return up_button.get();
    }


    public boolean getDown() {
        return down_button.get();
    }

    public boolean getLeft() {
        return left_button.get();
    }

    public boolean getRight() {
        return right_button.get();
    }

    public boolean getMinus() {
        return minus_button.get();
    }

    public boolean getPlus() {
        return plus_button.get();
    }

    public boolean getA() {
        return a_button.get();
    }

    public boolean getB() {
        return b_button.get();
    }

}
