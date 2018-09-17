package engine.Objs;

import engine.Interface.ICollidable;
import engine.Interface.IRenderBoundingBox;
import engine.Interface.InputProperty;
import engine.Physics.BoundingBox;
import engine.Physics.BoundingMesh;
import engine.Util.Raw;
import org.joml.Vector4f;

public class CollidableObj extends PhysicObj implements IRenderBoundingBox, ICollidable {
    public CollidableObj() {
        super();
    }

    public CollidableObj(String meshName) {
        name = "collidable obj " + index;
        index++;
        mesh = canvas.allRes.getX(meshName);
        boundingMesh=canvas.allRes.getX(meshName+"_BoundingBox");
        boundingBox=new BoundingBox(boundingMesh,this);
        camera=canvas.camera;
    }

    public CollidableObj(InputProperty<Raw> input) {
        super(input);
    }

    public BoundingBox boundingBox;

    void attachBoundingBox() {
        String boundingBoxName = raw.get("boundingBox");
        if (boundingBoxName != null)
            boundingMesh = canvas.allRes.getX(boundingBoxName);

        if (this instanceof ICollidable)
            boundingBox = new BoundingBox(boundingMesh, this);
    }

    @Override
    public void create() {
        attachBoundingBox();
        super.create();
    }

    public BoundingBox getBoundingBox() {
        return boundingBox;
    }

    public BoundingMesh boundingMesh;

    @Override
    public void removeFromRenderGroups() {
        super.removeFromRenderGroups();
        if (boundingMesh != null) {
            removeFromGroup(canvas.renderGroup_BoundingBox, boundingMesh.program.name);
        }
    }

    @Override
    public void addToRenderGroups() {
        super.addToRenderGroups();
        if (boundingMesh != null) {
            addToGroup(canvas.renderGroup_BoundingBox, boundingMesh.program.name);
        }
    }

    final Vector4f boundingBoxColor = new Vector4f(0f, 1f, 1f, 1f);
    public void renderBoundingBox() {
        setUColor(boundingBoxColor);
        connectUniforms(boundingMesh);
        boundingMesh.render();
    }

    public void addToCollideGroup(String groupName){
        canvas.addToCollideGroup(groupName,this);
    }

    public void removeFromCollideGroup(String groupName) {
        canvas.removeFromCollideGroup(groupName,this);
    }
}
