package engine.Objs;

import engine.Interface.*;
import engine.Physics.BVertex;
import engine.Physics.ForceFunctions.Force_Gravity;
import engine.Util.Constant;
import engine.Util.Raw;
import engine.Util.Debug;
import org.joml.Vector3f;


public class Player extends CollidableObj implements
        ICanInput, INeedGoalTest, ICharactor {

    public Player(InputProperty<Raw> input) {
        super(input);

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
    public void create() {
        super.create();
        rootPoint = boundingBox.getPointByIndice(4);
        corner1 = boundingBox.getPointByIndice(1);
        corner3 = boundingBox.getPointByIndice(3);
        corner2 = boundingBox.getPointByIndice(2);
        corner0 = boundingBox.getPointByIndice(0);
        corner1.collideable = false;
        corner3.collideable = false;
        corner0.collideable = false;
        corner2.collideable = false;

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


    public void reset() {
        removeAllForce();
        operateMatrix.identity();
        modelMatrix.identity();
        addForce(Force_Gravity.class,new Vector3f());
    }
}


