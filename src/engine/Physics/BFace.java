package engine.Physics;

import engine.Interface.ICollidable;
import engine.Util.Constant;
import org.joml.Vector3f;

//dedicate for Obj
public class BFace {
    public int index;
    public String name;//name include info of its belongTo objs
    public BVertex p0;//right point
    public BVertex p1;//left point
    public Vector3f direct = new Vector3f();
    public Vector3f normal = new Vector3f();
    public float length;
    public ICollidable belongTo;

    BFace(int i, BVertex v1, BVertex v2, String name) {
        this.name = name;
        index = i;
        belongTo = v1.belongTo;
        p0 = v1;
        p1 = v2;

        updateDirect();
        updateNormal();
        length = direct.length();

        p0.leftFace = this;
        p1.rightFace = this;


    }

    public void update() {
        p0.update();
        p1.update();

        updateDirect();
        updateNormal();
    }

    void updateDirect() {
        p1.sub(p0, direct);
        direct.normalize();

    }

    void updateNormal() {
        direct.rotateZ(Constant.Radian_Negetive_90, normal);
        normal.normalize();
    }

}
