package engine.Objs;

import engine.Interface.*;
import engine.Physics.BoundingBox;
import engine.Physics.BoundingMesh;
import engine.Util.Constant;
import engine.Util.Error;
import engine.Util.Raw;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.Iterator;
import java.util.Map;

public class PhysicObj extends Obj implements IPhysics {
    PhysicObj() {
        super();
    }

    public PhysicObj(String meshName) {
        super(meshName);
    }

    public PhysicObj(InputProperty<Raw> input) {
        super(input);
    }

    public void create() {
        super.create();


    }


    public Vector3f speed = new Vector3f();
    public Vector3f globalSpeed = new Vector3f();


    public Vector3f getGlobalSpeed() {
        IParent p = parent;
        globalSpeed.set(speed);
        while (!(p instanceof Canvas)) {
            if (p instanceof IPhysics) {
                globalSpeed.add(((IPhysics) p).getSpeed());
            }

            p = p.getParent();
        }

        return globalSpeed;
    }


    public Vector3f offset = new Vector3f();

    public Raw forces = new Raw("objs forces");


    private String getClassName(Class<?> aClass) {
        String fullName = aClass.getName();
        String name = fullName.substring(fullName.lastIndexOf(".") + 1);
        return name;
    }

    public IForceFunction getForce(Class<?> forceClass) {
        String name = getClassName(forceClass);
        return forces.get(name);
    }

    public void addForce(Class<?> forceClass, Object data) {

        IForceFunction forceFun = null;
        try {
            forceFun = (IForceFunction) forceClass.newInstance();
        } catch (Exception e) {
            Error.fatalError(e, "error add force to obj " + name);
        }
        forceFun.setHostObj(this);
        if (forceFun instanceof INeedData)
            ((INeedData) forceFun).init(data);
        forces.add(forceFun.getName(), forceFun);
        if (forceFun instanceof IInput) {
            inputCallBack = (IInput) forceFun;
            canvas.inputGroup.add(this.name, this);
        }
        if (forceFun instanceof IGoalTest) {
            goalTestFun = (IGoalTest) forceFun;
            canvas.goalTestGroup.add(this.name, this);
        }
        if (forceFun instanceof ICountDown) {
            ((ICountDown) forceFun).startTimer();
        }


    }

    //    public void addForce(String forceName)  {
//        Class myClass = Class.forName("engine.Physics.ForceFunctions." + forceName);
//        IForceFunction forceFun = (IForceFunction) myClass.newInstance();
//
//        forces.add(forceName, forceFun);
//    }
    public void removeAllForce() {
        
        forces.iterateKeyValueX((String name,IForceFunction fun) -> {
            removeCheck(fun);
            forces.remove(name);
        });
    }

    void removeCheck(IForceFunction forceFun) {
        if (forceFun instanceof IInput) {
            inputCallBack = null;
            canvas.inputGroup.remove(this.name);
        }
        if (forceFun instanceof IGoalTest) {
            goalTestFun = null;
            canvas.goalTestGroup.remove(this.name);
        }
    }

    public void removeForce(Class<?> forceClass) {
        String name = getClassName(forceClass);
        IForceFunction forceFun = forces.get(name);
        removeCheck(forceFun);

        forces.remove(name);

    }

    //there should not have add or remove force in the apply function(concurrentModification)
    public void applyForces(float interval) {//change speed of current frame
        Iterator it = forces.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            IForceFunction force = (IForceFunction) pair.getValue();
            force.apply(this, interval);
        }

    }

    @Override
    public Vector3f getSpeed() {
        return speed;
    }

    private Vector3f relateVel = new Vector3f();

    public Vector3f getRelateVel() {
        return relateVel;
    }

    public Vector3f accelerate = new Vector3f();
    Vector3f globalAccelerate = new Vector3f();

    public Vector3f getAccelerate() {
        accelerate.set(Constant.ZERO3f);
        forces.iterateValue((IForceFunction force) -> {
            if (force instanceof IAccelerate) {
                accelerate.add(((IAccelerate) force).getAcceleration());
            }
        });
        return accelerate;
    }


    public Vector3f getGlobalAccelerate() {//global
        getAccelerate();

        IParent p = parent;

        while (!(p instanceof Canvas)) {
            if (p instanceof IPhysics)
                accelerate.add(((IPhysics) p).getAccelerate());
            p = p.getParent();
        }
        return globalAccelerate;
    }

    Vector3f relateAccelerate = new Vector3f();

    public Vector3f getRelateAccelerate() {
        return relateAccelerate;
    }


    public Matrix4f getModelMatrix() {
        return modelMatrix;
    }

    Matrix4f invertModelMatrix;

    public Matrix4f getInvertModelMatrix() {
        return modelMatrix.invert(invertModelMatrix);
    }


    Raw collideExclude = new Raw("collide exclude");

    public void addCollideExclude(ICollidable obj) {

        collideExclude.add(obj.getName(), obj);

    }

    public void removeCollideExclude(ICollidable obj) {
        collideExclude.remove(obj.getName());
    }


    @Override
    public Matrix4f getOperateMatrix() {
        return operateMatrix;
    }

    public Raw getCollideExclude() {
        return collideExclude;
    }

}
