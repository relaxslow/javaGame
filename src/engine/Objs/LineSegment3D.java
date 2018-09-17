package engine.Objs;

import engine.Buffer.VBO_D;
import engine.Meshes.LineMesh;
import engine.Util.Error;
import engine.Util.Raw;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class LineSegment3D extends LineObj {
    public LineSegment3D(){
        
    }
    public LineSegment3D(String name, Vector4f color) {
        this.name = name;
        setObjColor(color);

    }

    @Override
    public void create() {
        camera = canvas.camera;
        points = new Vector3f[2];
        points[0] = new Vector3f();
        points[1] = new Vector3f();

        mesh = new LineMesh(
                "lineMesh" + LineMesh.lineIndex,
                getFloatArr(points),
                points.length,
                getIntArr(points)
        );

    }

    float[] vertexPos = new float[6];

    public void changeLine(Vector3f p1, Vector3f p2) {
        changeLine(p1.x, p1.y, p1.z, p2.x, p2.y, p2.z);
    }

    public void changeLine(float p1x, float p1y, float p1z, float p2x, float p2y, float p2z) {
        vertexPos[0] = p1x;
        vertexPos[1] = p1y;
        vertexPos[2] = p1z;
        vertexPos[3] = p2x;
        vertexPos[4] = p2y;
        vertexPos[5] = p2z;
        VBO_D posVBO = mesh.vbos.get("a_Position");
        posVBO.generateNewBuffer(vertexPos, points.length, false);
        mesh.generateVAO(false);


    }
}
