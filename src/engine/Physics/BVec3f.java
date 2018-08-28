package engine.Physics;

import engine.Interface.ICollidable;
import org.joml.Vector3f;
import org.joml.Vector4f;

public   class BVec3f extends Vector3f  {
    static Vector4f vec4f = new Vector4f();
    public ICollidable belongTo;
    protected Vector3f origin=new Vector3f();
    public void update(){
        vec4f.set(origin, 1).mul(belongTo.getModelMatrix());
        set(vec4f.x, vec4f.y, 0);
    }
}
