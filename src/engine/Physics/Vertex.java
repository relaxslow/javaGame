package engine.Physics;

import engine.Objs.Obj;
import engine.Util.Constant;
import engine.Util.Raw;
import org.joml.Vector3f;

import java.util.ArrayList;

//dedicate for bounding mesh
public class Vertex extends Vector3f {
    public int indice;
   

    public Vertex() {
        indice = -1;

    }

    public Vertex(ArrayList<Vector3f> vertices, int indice) {
        this.indice = indice;
        Vector3f point = vertices.get(indice);
        this.set(point);
    }

 
   

}