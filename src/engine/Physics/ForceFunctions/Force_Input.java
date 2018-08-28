package engine.Physics.ForceFunctions;

import engine.Input.KeyBoard;
import engine.Input.Mouse;
import engine.Interface.IAffectByForce_Input;
import engine.Interface.IInput;
import engine.Interface.IPhysics;
import engine.Util.Constant;
import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.*;

public class Force_Input extends ForceFunction implements IInput {
   
    public static float speed = 2f;
    Vector3f inputSpeed=new Vector3f();
    public static Vector3f RIGHT = new Vector3f(1, 0f, 0f);
    public static Vector3f LEFT = new Vector3f(-1, 0f, 0f);
    public static Vector3f UP = new Vector3f(0f, 1, 0f);
    public static Vector3f DOWN = new Vector3f(0f, -1, 0f);

    public Force_Input() {
       setName();
    }


    @Override
    public void apply(IPhysics obj,float interval) {
//        Vector3f inputDirection = ((IAffectByForce_Input) obj).getInputDirection();
        
        inputDirection.mul(speed,inputSpeed);
        Vector3f speed = obj.getSpeed();
      
        speed.add(inputSpeed);
//        Debug.log(speed.toString());
    }
    Vector3f inputDirection =new Vector3f() ;
    @Override
    public void input(KeyBoard key, Mouse mouse, Object obj) {
    
//        if (obj instanceof IAffectByForce_Input) {
//            inputDirection = ((IAffectByForce_Input) obj).getInputDirection();
//        }

        inputDirection.set(Constant.ZERO3f);
        if (key.isPressed(GLFW_KEY_D)) {
            inputDirection.add(Force_Input.RIGHT);
        }
        if (key.isPressed(GLFW_KEY_A)) {
            inputDirection.add(Force_Input.LEFT);
        }
        if (key.isPressed(GLFW_KEY_W)) {
            inputDirection.add(Force_Input.UP);
        }
        if (key.isPressed(GLFW_KEY_S)) {
            inputDirection.add(Force_Input.DOWN);
        }
        if (!inputDirection.equals(Constant.ZERO3f))
            inputDirection.normalize();
    }
}
