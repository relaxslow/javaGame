package engine.Objs;

import engine.Interface.*;
import engine.Physics.BVertex;
import engine.Physics.CollideInfo;
import engine.Util.Constant;
import engine.Util.Raw;
import engine.Util.Debug;
import org.joml.Vector3f;

import java.util.LinkedList;


public class Player extends PhysicObj implements
        ICanInput, INeedGoalTest, ICharactor {
    //    public  int status;
//    final public static int IN_AIR = 0;
//    final public static int ON_GROUND = 1;
    public Player(InputProperty<Raw> input) throws Exception {
        super(input);
//        status = IN_AIR;
    }

    public BVertex rootPoint;
    public BVertex corner1;
    public BVertex corner3;
    public BVertex corner0;
    public BVertex corner2;

    @Override
    public BVertex getRootPoint() {
        return rootPoint;
    }

    @Override
    public void create(Raw res) throws Exception {
        super.create(res);
        rootPoint = boundingBox.getPointByIndice(4);
        corner1 = boundingBox.getPointByIndice(1);
        corner3 = boundingBox.getPointByIndice(3);
        corner2= boundingBox.getPointByIndice(0);
        corner1.collideable = false;
        corner3.collideable = false;
        
        canvas.player = this;
        canvas.addToCollideGroup("Players", this);

    }


    @Override
    public Vector3f getGlobalAccelerate() {
        globalAccelerate.set(Constant.ZERO3f);
        return globalAccelerate;
    }


    @Override
    public Vector3f getGlobalSpeed() {
        globalSpeed.set(Constant.ZERO3f);
        return globalSpeed;
    }

    @Override
    public Vector3f getRelateAccelerate() {
        relateAccelerate.set(Constant.ZERO3f);
        return relateAccelerate;
    }

    public void die() {
        Debug.log("player die!!");
    }


    @Override
    public IGoalTest getGoalTestFun() {
        return goalTestFun;
    }


}


