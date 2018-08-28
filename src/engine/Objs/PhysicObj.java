package engine.Objs;

import engine.Interface.*;
import engine.Physics.BVertex;
import engine.Physics.BoundingBox;
import engine.Physics.BoundingMesh;
import engine.Util.Constant;
import engine.Util.Error;
import engine.Util.Raw;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

public class PhysicObj extends Obj implements IRenderBoundingBox,  ICollidable {
    PhysicObj() {
    }

    public PhysicObj(InputProperty<Raw> input) throws Exception {
        super(input);
    }

    public void create(Raw res) throws Exception {
        attachBoundingBox(res);
        super.create(res);
       

    }

    public BoundingMesh boundingMesh;

    @Override
    public void removeFromRenderGroups() {
        super.removeFromRenderGroups();
        if (boundingMesh != null) {
            removeFromGroup(canvas.renderGroupBoundingBox, boundingMesh.program.name);
        }
    }

    @Override
    public void addToRenderGroups() throws Exception {
        super.addToRenderGroups();
        if (boundingMesh != null) {
            addToGroup(canvas.renderGroupBoundingBox, boundingMesh.program.name);
        }
    }

    public void renderBoundingBox() {

        connectUniforms(boundingMesh);
        boundingMesh.render();

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

    @Override
    public Vector3f getSpeed() {
        return speed;
    }

    public Vector3f offset = new Vector3f();

    public Raw forces = new Raw();

    //    ArrayList<String> markedDeleteForces = new ArrayList<>();
//
//
//    @Override
//    public void markDelete(IForceFunction forceFun) {
//        markedDeleteForces.add(forceFun.getName());
//    }
    private String getClassName(Class<?> aClass) {
        String fullName = aClass.getName();
        String name = fullName.substring(fullName.lastIndexOf(".") + 1);
        return name;
    }

    public IForceFunction getForce(Class<?> forceClass) {
        String name = getClassName(forceClass);
        return forces.get(name);
    }
    
    public void addForce(Class<?> forceClass, Object data)  {
        try
        {
            IForceFunction forceFun = (IForceFunction) forceClass.newInstance();
            forceFun.setHostObj(this);
            forceFun.setScene(canvas.scene);
            if (forceFun instanceof INeedData)
                ((INeedData) forceFun).passInitData(data);
            forces.add(forceFun.getName(), forceFun);
            if (forceFun instanceof IInput) {
                inputCallBack = (IInput) forceFun;
                canvas.inputGroup.add(this.name, this);
            }
            if (forceFun instanceof IGoalTest) {
                goalTestFun = (IGoalTest) forceFun;
                canvas.goalTestGroup.add(this.name, this);
            }
        }catch(Exception e){
            Error.fatalError(e,"error in addForce");
        }
       
    }

//    public void addForce(String forceName) throws Exception {
//        Class myClass = Class.forName("engine.Physics.ForceFunctions." + forceName);
//        IForceFunction forceFun = (IForceFunction) myClass.newInstance();
//
//        forces.add(forceName, forceFun);
//    }

    public void removeForce(Class<?> forceClass) {
        String name = getClassName(forceClass);
        IForceFunction forceFun = forces.get(name);
        if (forceFun instanceof IInput) {
            inputCallBack = null;
            canvas.inputGroup.remove(this.name);
        }
        if (forceFun instanceof IGoalTest) {
            goalTestFun = null;
            canvas.goalTestGroup.remove(this.name);
        }
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

    private Vector3f relateVel = new Vector3f();

    public Vector3f getRelateVel() {
        return relateVel;
    }

    Vector3f accelerate = new Vector3f();
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
    public Matrix4f getInvertModelMatrix(){
        return modelMatrix.invert(invertModelMatrix);
    }

    BoundingBox boundingBox;

    void attachBoundingBox(Raw res) throws Exception {
        String boundingBoxName = raw.get("boundingBox");
        if (boundingBoxName != null)
            boundingMesh = res.getX(boundingBoxName);
        
        if (this instanceof ICollidable)
            boundingBox = new BoundingBox(boundingMesh, this);
    }




    public BoundingBox getBoundingBox() {
        return boundingBox;
    }


    Raw collideExclude = new Raw();

    public void addCollideExclude(ICollidable obj) {
        try {
            collideExclude.add(obj.getName(), obj);
        } catch (Exception e) {
            Error.fatalError(e, "error in add CollideExclude");
        }
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
