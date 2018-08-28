package engine.Interface;

import engine.Objs.Block;
import engine.Objs.Obj;
import engine.Objs.PhysicObj;

import java.util.LinkedList;

public interface ICollideWithGround extends ICollidable { 

    void addGround(ICollidable ground, int leftOrRight) throws Exception;
    void removeGround(int leftOrRight);

    LinkedList<ICollidable> getGround();
}
