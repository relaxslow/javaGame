package engine.Physics;

import org.joml.Vector3f;

//dedicate for bounding mesh
public class Face {
    public int index;
    public Vertex p1;
    public Vertex p2;
    

    public Face(int index,Vertex p1, Vertex p2) {
        this.index=index;
        this.p1 = p1;
        this.p2 = p2;
        
    }


}