package engine.View;

import engine.Interface.InputProperty;
import engine.Util.Raw;

public class CameraUI extends Camera2d {
    public CameraUI(InputProperty<Raw> input) {
        super(input);
    }

    @Override
    public void buildPMatrix() {
        projectMatrix.identity();
        right=(float)canvas.width;
        bottom=(float)canvas.height;
        projectMatrix.setOrtho(left, right, bottom, top, near, far);
        matrix.set(projectMatrix).mul(viewMatrix);
        
    }
}
