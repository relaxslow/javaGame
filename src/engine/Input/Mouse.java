package engine.Input;

import org.joml.Vector2d;
import org.joml.Vector2f;
import org.lwjgl.glfw.GLFWCursorEnterCallback;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.glfwSetCursorEnterCallback;
import static org.lwjgl.glfw.GLFW.glfwSetMouseButtonCallback;

public class Mouse {
    Vector2d previousPos = new Vector2d();
    Vector2d currentPos = new Vector2d();
    boolean inWindow;
    public boolean leftButtonPressed;
    public boolean rightButtonPressed;

    public Vector2f delta = new Vector2f();
    GLFWCursorPosCallback cursorPosCallback;
    GLFWCursorEnterCallback cursorEnterCallback;
    GLFWMouseButtonCallback mouseButtonCallBack;

   public  Mouse(long window) {
        cursorPosCallback = new GLFWCursorPosCallback() {
            @Override
            public void invoke(long window, double xpos, double ypos) {
                currentPos.x = xpos;
                currentPos.y = ypos;
            }
        };
        cursorEnterCallback = new GLFWCursorEnterCallback() {
            @Override
            public void invoke(long window, boolean entered) {
                inWindow = entered;
            }
        };
        mouseButtonCallBack = new GLFWMouseButtonCallback() {
            @Override
            public void invoke(long window, int button, int action, int mods) {
                leftButtonPressed = button == GLFW_MOUSE_BUTTON_1 && action == GLFW_PRESS;
                rightButtonPressed = button == GLFW_MOUSE_BUTTON_2 && action == GLFW_PRESS;
            }
        };
        glfwSetCursorPosCallback(window, cursorPosCallback);
        glfwSetCursorEnterCallback(window, cursorEnterCallback);
        glfwSetMouseButtonCallback(window, mouseButtonCallBack);
    }

    public void update(float interval) {

     
    }

    public void input() {
        double dx = currentPos.x - previousPos.x;
        double dy = currentPos.y - previousPos.y;

        delta.x = (float) dx;
        delta.y = (float) dy;

        previousPos.x = currentPos.x;
        previousPos.y = currentPos.y;
//        engine.Debug.log(delta.x + "|" + delta.y);

    }

    public void clean() {
        mouseButtonCallBack.free();
        cursorEnterCallback.free();
        cursorPosCallback.free();
    }
}