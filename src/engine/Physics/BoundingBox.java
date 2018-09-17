package engine.Physics;


import engine.Interface.ICollidable;
import engine.Objs.Obj;
import engine.Util.Raw;

import java.util.ArrayList;


public class BoundingBox {
    public ArrayList<BVertex> points = new ArrayList<>();
    public ArrayList<BFace> faces = new ArrayList<>();
    public Raw indexedPoints = new Raw("indexes points");

    public BoundingBox(BoundingMesh boundingMesh, ICollidable obj) {
        for (int i = 0; i < boundingMesh.points.size(); i++) {
            Vertex v = boundingMesh.points.get(i);
            BVertex bv = new BVertex(v, obj);

            points.add(bv);
        }

        int faceIndex = 0;
        for (int i = 0; i < points.size(); i++) {
            if (i < points.size() - 1) {
                faces.add(new BFace(faceIndex,
                        points.get(i), points.get(i + 1),
                        obj.getName() + faceIndex));
                faceIndex++;
            } else if (i == points.size() - 1) {
                faces.add(new BFace(faceIndex,
                        points.get(i), points.get(0),
                        obj.getName() + faceIndex));
                faceIndex++;
            }
        }

        for (int i = 0; i < points.size(); i++) {
            BVertex point = points.get(i);
            indexedPoints.add(String.valueOf(point.indice), point);
            
        }


    }

    public BVertex getPointByIndice(int indice) {
        for (int i = 0; i < points.size(); i++) {
            BVertex point = points.get(i);
            if (indice == point.indice)
                return point;
        }
        return null;
    }
}
