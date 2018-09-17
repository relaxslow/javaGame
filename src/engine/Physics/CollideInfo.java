package engine.Physics;

import engine.Interface.ICollidable;
import engine.Interface.ICollideOccur;
import engine.Util.Constant;
import org.joml.Vector3f;

import java.util.ArrayList;

public class CollideInfo {
    public static final int GOAL = 1;
    public static final int NORMAL = 0;
    public float minCollideTime;

    public BVertex vertex;
    public BFace face;
    public ICollideOccur fun;
    public ICollidable obj1;
    public ICollidable obj2;


    public Vector3f collidePoint = new Vector3f();
    public Vector3f collideSurfaceP0 = new Vector3f();
    public Vector3f collideSurfaceP1 = new Vector3f();
    public Vector3f intersectPoint = new Vector3f();
    public int type;
    public Vector3f currentPos = new Vector3f();
    public boolean collidePass;


    public CollideInfo() {
        reset();
    }

    public void reset() {
        minCollideTime = -1f;
        collidePoint.set(Constant.ZERO3f);
        collideSurfaceP0.set(Constant.ZERO3f);
        collideSurfaceP1.set(Constant.ZERO3f);
        currentPos.set(Constant.ZERO3f);
        intersectPoint.set(Constant.ZERO3f);

        vertex = null;
        face = null;
        obj1 = null;
        obj2 = null;
        fun = null;

    }


    public <T> T getObjWithType(Class<T> aclass) {
        if (aclass.isInstance(obj1)) {
            return (T) obj1;

        } else {
            return (T) obj2;
        }
    }

    public ArrayList<BVertex> collideAtSameTime = new ArrayList<>();

    public boolean hasCollideVetex(BVertex testTarget) {
        if (vertex == testTarget)
            return true;
        for (int i = 0; i < collideAtSameTime.size(); i++) {
            BVertex v = collideAtSameTime.get(i);
            if (v == testTarget) {
                return true;
            }
        }
        return false;
    }
}