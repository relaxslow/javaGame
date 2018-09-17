package engine.Objs.Helper;

import engine.Interface.InputProperty;
import engine.Objs.Canvas;
import engine.Objs.Obj;
import engine.Util.Raw;

public class Border extends Obj {
    public float wid;
    public float hei;

    public Border(String name,float wid, float hei) {
        this.name=name ;
        attachParent(canvas);
        mesh = canvas.allRes.getX("rectangleMesh");
        camera=canvas.camera;
        this.wid = wid/2;
        this.hei = hei/2;
        matrix.translate(0f, 2f, 0f);
        matrix.scale(wid, hei, 1f);
        calculateMatrix();
        addToRenderGroups();
    }
    
//
//    @Override
//    public void create() {
//        super.create();
//        canvas.eliminateBorder = this;
//        wid = raw.getX("wid");
//        hei = raw.getX("hei");
//        matrix.translate(0f, 2f, 0f);
//        matrix.scale(wid * 2, hei * 2, 1f);
//    }
}
