package engine.Physics;

import engine.Interface.ICollidable;
import engine.Util.Raw;
import org.joml.Vector3f;
import org.joml.Vector4f;

//dedicate for Obj bounding box
public class BVertex extends BVec3f {
    public int indice;
    public BFace leftFace;
    public BFace rightFace;
    BVertex(Vertex v, ICollidable obj) {
        belongTo = obj;
        set(v);
        origin.set(v);
        indice = v.indice;
  
    }
    
    public boolean collideable=true;
 
   
}
