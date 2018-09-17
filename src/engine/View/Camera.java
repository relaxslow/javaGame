package engine.View;

import engine.Input.KeyBoard;
import engine.Input.Mouse;
import engine.Interface.*;
import engine.Objs.Canvas;
import engine.Util.Raw;
import engine.Util.Res;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public abstract class Camera extends Res implements
        ISceneCamera,
        ICanInput {
    static int cameraIndex;
    public Matrix4f matrix = new Matrix4f();


    public Matrix4f projectMatrix = new Matrix4f();//truncated pyramid
    Matrix4f viewMatrix = new Matrix4f();//

    Vector3f oldPosition = new Vector3f();
    Vector3f oldRotation = new Vector3f();
    public Vector3f position = new Vector3f();
    public Vector3f rotation = new Vector3f();


    public static Canvas canvas;
    KeyBoard keyBoard;
    Mouse mouse;
    Raw raw = new Raw("camera raw");

    Camera() {

    }

    Camera(InputProperty<Raw> input)  {
        input.run(raw);
    }


    public <T> T set(String name, T defultValue) {
        T result = raw.get(name);
        if (result == null) result = defultValue;
        return result;
    }


    public void buildViewMatrix() {

        viewMatrix.identity();
        viewMatrix.rotate((float) Math.toRadians(rotation.x), new Vector3f(1, 0, 0))
                .rotate((float) Math.toRadians(rotation.y), new Vector3f(0, 1, 0));
        viewMatrix.translate(-position.x, -position.y, -position.z);
        matrix.set(projectMatrix).mul(viewMatrix);
    }

    public IInit initCallBack;
    public IUpdate updateCallBack;
    public  IInput inputCallBack;

    public void update(float interval) {
        if (updateCallBack != null)
            updateCallBack.update(interval, this);
    }

    public float moveSpeed = 1f;

    public boolean moved = false;
    public boolean rotated = false;

    public void move(float x, float y, float z) {
        if (x == 0f && y == 0f && z == 0f) {
            moved = false;
            return;
        } else {
            oldPosition.set(position);
            moved = true;
        }

        if (z != 0) {
            position.x += (float) Math.sin(Math.toRadians(rotation.y)) * -1.0f * z;
            position.z += (float) Math.cos(Math.toRadians(rotation.y)) * z;
        }
        if (x != 0) {
            position.x += (float) Math.sin(Math.toRadians(rotation.y - 90)) * -1.0f * x;
            position.z += (float) Math.cos(Math.toRadians(rotation.y - 90)) * x;
        }
        position.y += y;

    }

    public void rotate(float x, float y, float z) {
        if (x == 0 && y == 0 && z == 0) {
            rotated = false;
            return;
        } else {
            oldRotation.set(rotation);
            rotated = true;
        }
        rotation.x += x;
        rotation.y += y;
        rotation.z += z;

    }

    public Vector3f deltaPos = new Vector3f();

    public Vector2f deltaRot = new Vector2f();

    public void input() {
        inputCallBack.input(keyBoard, mouse, this);
    }

    @Override
    public IInput getInputCallBack() {
        return inputCallBack;
    }
    
  
}