package engine.Input;

import org.lwjgl.glfw.GLFWKeyCallback;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.glfwGetKey;

public class KeyBoard {
    long window;
    private GLFWKeyCallback keyCallback;

    public KeyBoard(long window) {
        this.window = window;
        keyCallback = new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {
                if (key == GLFW_KEY_ESCAPE && action == GLFW_PRESS) {
                    glfwSetWindowShouldClose(window, true);
                }
            }
        };
        glfwSetKeyCallback(window, keyCallback);
    }

    public boolean isPressed(int keyCode) {

        return glfwGetKey(window, keyCode) == GLFW_PRESS;
    }

    public void clean() {
        keyCallback.free();
    }
}
