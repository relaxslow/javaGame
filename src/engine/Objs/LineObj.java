package engine.Objs;

import engine.Buffer.*;
import engine.Interface.ICanInput;
import engine.Interface.InputProperty;
import engine.Meshes.ElementMesh;
import engine.Meshes.LineMesh;
import engine.Util.Raw;
import org.joml.Vector3f;
import org.joml.Vector4f;

import static org.lwjgl.opengl.GL11.GL_LINES;

public class LineObj extends Obj  {
//    Vector4f color;
public Vector3f[] points;

    public LineObj(InputProperty<Raw> input)  {
        super(input);

    }

    public LineObj() {
    }

    @Override
    public void create()  {
        camera = raw.getX("camera");//draw in 2d or 3d

        color = raw.getX("u_Color");
        points = raw.getX("points");

        mesh = new LineMesh(
                "lineMesh" + LineMesh.lineIndex,
                getFloatArr(points),
                points.length,
                getIntArr(points)
        );
        attachCallbacks();

    }

    public int[] getIntArr(Vector3f[] points) {
        int[] indices = new int[(points.length - 1) * 2];
        for (int i = 0; i < points.length - 1; i++) {
            indices[i * 2] = i;
            indices[i * 2 + 1] = i + 1;
        }
        return indices;
    }

    public float[] getFloatArr(Vector3f[] points) {
        float[] vertexPos = new float[points.length * 3];
        for (int i = 0; i < points.length; i++) {
            vertexPos[i * 3] = points[i].x;
            vertexPos[i * 3 + 1] = points[i].y;
            vertexPos[i * 3 + 2] = points[i].z;
        }
        return vertexPos;
    }

    public void changeColor(Vector4f color) {
        this.color = color;
    }

    public void changeLine(Vector3f[] points)  {
        this.points = points;

        float[] vertexPos = getFloatArr(points);
        ((LineMesh) mesh).pointNum = points.length;
        VBO_D posVBO = mesh.vbos.get("a_Position");
        posVBO.generateNewBuffer(vertexPos, points.length, false);
        int[] indices = getIntArr(points);
        IBO ibo = ((LineMesh) mesh).ibo;
        ibo.generateNewBuffer(indices, false);
        
        mesh.generateVAO(false);

    }

    @Override
    public void clean() {
        mesh.clean();
    }

}

