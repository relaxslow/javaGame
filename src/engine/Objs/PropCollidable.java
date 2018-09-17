package engine.Objs;

import engine.Physics.BoundingBox;

public class PropCollidable extends CollidableObj {
    static int index_propCollidable;

    public PropCollidable(String meshName) {
        createName("collidable prop");
        index_propCollidable++;
        
        mesh=canvas.allRes.getX(meshName);
        boundingMesh=canvas.allRes.getX(meshName+"_BoundingBox");
        boundingBox=new BoundingBox(boundingMesh,this);
        camera=canvas.camera;
        
    }
    
    public void hide(){
        
    }
    public void show(){
        
    }
}
