package engine.Interface;

import engine.Physics.BoundingBox;
import engine.Util.Raw;


public interface ICollidable extends IPhysics {
    String getName();

    //    void  testCollide(ICollidable obj,ICollideOccur fun);
    BoundingBox getBoundingBox();

    Raw getCollideExclude();

    void addCollideExclude(ICollidable obj);

    void removeCollideExclude(ICollidable obj);
    


}
