package engine.Objs.scenes;

import engine.Interface.ICanUpdate;
import engine.Interface.IInit;
import engine.Interface.INeedTestBorder;
import engine.Interface.IPOOL;
import engine.Objs.Canvas;
import engine.Objs.Obj;
import engine.Physics.ForceFunctions.Force_CloudMove;
import engine.Util.Raw;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Cloud extends Scene_Motion implements IPOOL, INeedTestBorder {
    public static int index = 0;

    Obj model;
    
    public void initFromNew() {
        name = "cloud" + index;
        index++;

        raw.add("name", name);
        raw.add("camera", canvas.camera);
        raw.add("init", (IInit<Scene>) (Scene obj) -> {
            obj.matrix.scale(0.4f, 0.4f, 0.4f);//smaller the origin indicator
        });
        model = new Obj((Raw raw) -> {
            raw.add("parent", name);
            raw.add("meshName", "/model/cloud");
            raw.add("init", (IInit<Obj>) (Obj obj) -> {
                obj.matrix.translate(0f, 0f, -2f);
                obj.matrix.scale(1.2f, 1.2f, 1.2f);
            });
        });

        canvas.createObj(this);
        canvas.createObj(model);
        addForce(Force_CloudMove.class, null);

    }

    @Override
    public void initFromPool() {
        canvas.updateGroup.add(this.name, this);
        parent.addChild(this);
        addToRenderGroups();
        model.addToRenderGroups();
        canvas.borderTestGroup.add(this.name, this);
        addForce(Force_CloudMove.class, null);
    }
    @Override
    public void eliminate() {
        // remove from forceGroup, updateGroup and renderGroup and hierarchy
        removeForce(Force_CloudMove.class);
        canvas.updateGroup.remove(this.name);
        parent.removeChild(this);

        removeFromRenderGroups();
        model.removeFromRenderGroups();
        modelMatrix.identity();

        CloudGenerator.pool.reclaim(this);
        CloudGenerator.num--;

    }

    Vector3f currentPos = new Vector3f();
    @Override
    public boolean isOutOfBorder() {
        Matrix4f modelMatrix = getModelMatrix();
        modelMatrix.getTranslation(currentPos);
        if (currentPos.x < -canvas.eliminateBorder.wid || currentPos.x > canvas.eliminateBorder.wid)
            return true;
        return false;
    }

//    void test() {

//        canvas.createObj(new Scene_Motion((Raw raw) -> {
//            raw.add("name", "cloud1");
//            raw.add("camera", canvas.camera);
//            raw.add("init", (IInit<Scene>) (Scene obj) -> {
//                obj.matrix.scale(0.4f, 0.4f, 0.4f);//smaller the origin indicator
//                obj.operateMatrix.translate(4f, 2f, 0.2f);
//                obj.addForce(Force_CloudMove.class, null);
//            });
//
//        }));
//        canvas.createObj(new Obj((Raw raw) -> {
//            raw.add("parent", "cloud1");
//            raw.add("meshName", "/model/cloud");
////            raw.add("boundingBox", "/model/testPlatform1_BoundingBox");
////            raw.add("u_Color", new Vector4f(0f, 1f, 1f, 1f));
//
//            raw.add("init", (IInit<Obj>) (Obj obj) -> {
//                obj.matrix.translate(0f, 0f, -1f);
//                obj.matrix.scale(1.2f, 1.2f, 1.2f);
//
//
//            });
//        }));
//    }
    
}