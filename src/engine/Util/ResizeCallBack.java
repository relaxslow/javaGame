package engine.Util;


import engine.Objs.Canvas;
import org.lwjgl.glfw.GLFWFramebufferSizeCallback;

import static org.lwjgl.opengl.GL11.glViewport;

public class ResizeCallBack extends GLFWFramebufferSizeCallback {
    Canvas canvas;

    public ResizeCallBack(Canvas canvas) {

        this.canvas = canvas;

    }

    @Override
    public void invoke(long window, int width, int height) {
        canvas.width = width;
        canvas.height = height;
        glViewport(0, 0, width, height);
        canvas.camera.buildPMatrix();
        canvas.UICamera.buildPMatrix();
    }
}