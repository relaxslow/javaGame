package engine.View;

import engine.Interface.InputProperty;
import engine.Interface.INeedCreate;
import engine.Util.Raw;

public class Camera2d extends Camera implements INeedCreate {
    public Camera2d(InputProperty<Raw> input)  {
        input.run(raw);
        name = set("name", "camera" + cameraIndex);
    }


    float ratio;

    public float left;
    public float right;
    public float bottom;
    public float top;
    float near;
    float far;

    @Override
    public void create()  {
        keyBoard = canvas.keyBoard;
        mouse = canvas.mouse;


        initCallBack = raw.get("init");
        if (initCallBack != null)
            initCallBack.init(this);
        
        left = raw.getX("left");
        right = raw.getX("right");
        bottom = raw.getX("bottom");
        top = raw.getX("top");
        near = raw.getX("near");
        far = raw.getX("far");
        Float r = raw.get("ratio");
        if (r == null)
            ratio = bottom / right;
        else
            ratio = r;

        buildPMatrix();


//
        buildViewMatrix();
        inputCallBack = raw.get("input");
        updateCallBack = raw.get("update");
    }

    float zoomSpeed = 0.1f;
    float minW = 0.1f;

    public boolean zoomed=false;
    public void zoomIn() {
        float zoomSpeedY = zoomSpeed * ratio;
        left += zoomSpeed;
        if (left >= -minW)
            left = -minW;
        right -= zoomSpeed;
        if (right <= minW)
            right = minW;
        float minH = minW * ratio;
        bottom += zoomSpeedY;
        if (bottom >= -minH)
            bottom = -minH;
        top -= zoomSpeedY;
        if (top <= minH)
            top = minH;
        buildPMatrix();
    }

    public void zoomOut() {
        float zoomSpeedY = zoomSpeed * ratio;
        left -= zoomSpeed;
        right += zoomSpeed;
        bottom -= zoomSpeedY;
        top += zoomSpeedY;
        buildPMatrix();

    }

    @Override
    public void buildPMatrix() {
        projectMatrix.identity();
        top=right*canvas.getRatio();
        bottom=-top;
        projectMatrix.setOrtho(left, right, bottom, top, near, far);
        matrix.set(projectMatrix).mul(viewMatrix);
    }


}
