package engine.Objs.Helper;

import engine.Objs.Canvas;
import engine.Objs.Obj;

public class OriginIndicator extends Obj {
    public OriginIndicator() {
        name = "originIndicator";
        mesh = canvas.allRes.getX("originIndicatorMesh");
        attachParent(canvas);
        camera = canvas.camera;
        matrix.scale(0.4f, 0.4f, 0.4f);
        calculateMatrix();
        addToRenderGroups();

    }
}
