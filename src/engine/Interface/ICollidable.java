package engine.Interface;

import engine.Objs.Block;
import engine.Objs.Obj;
import engine.Objs.PhysicObj;
import engine.Objs.Scene;
import engine.Physics.BVertex;
import engine.Physics.BoundingBox;
import engine.Physics.BoundingMesh;
import engine.Util.Raw;
import org.joml.Matrix4f;
import org.joml.Vector4f;


public interface ICollidable extends IPhysics {
    String getName();

    //    void  testCollide(ICollidable obj,ICollideOccur fun);
    BoundingBox getBoundingBox();

    Raw getCollideExclude();

    void addCollideExclude(ICollidable obj);

    void removeCollideExclude(ICollidable obj);
    
    Matrix4f getOperateMatrix();

}
