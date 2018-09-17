package engine.View;

import engine.Interface.INeedCreate;
import engine.Interface.InputProperty;
import engine.Util.Raw;

public class Camera3d extends Camera implements INeedCreate {


    public Camera3d(InputProperty<Raw> input) {
        input.run(raw);
        name = set("name", "camera" + cameraIndex);
    }

    @Override
    public void create()   {
        keyBoard = canvas.keyBoard;
        mouse = canvas.mouse;


        initCallBack = raw.get("init");
        inputCallBack = raw.get("input");
        updateCallBack = raw.get("update");

        if (initCallBack != null)
            initCallBack.init(this);
        
        FOV_In_Radian = (float) Math.toRadians(FOV);
        buildPMatrix();
        buildViewMatrix();

    }

    public float FOV;
    float FOV_In_Radian;
    public float near;
    public float far;
    float aspectRatio;

    @Override
    public void buildPMatrix() {
        aspectRatio = (float) canvas.width / canvas.height;
        projectMatrix.identity();
        projectMatrix.perspective(FOV_In_Radian, aspectRatio, near, far);
        matrix.set(projectMatrix).mul(viewMatrix);

    }


}