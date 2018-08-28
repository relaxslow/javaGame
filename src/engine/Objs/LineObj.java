package engine.Objs;

import engine.Buffer.*;
import engine.Interface.ICanInput;
import engine.Interface.InputProperty;
import engine.Meshes.ElementMesh;
import engine.Util.Raw;
import org.joml.Vector3f;
import org.joml.Vector4f;

import static org.lwjgl.opengl.GL11.GL_LINES;

public class LineObj extends Obj implements ICanInput {
    Vector4f color;
    Vector3f[] points;

    public LineObj(InputProperty<Raw> input) throws Exception {
        super(input);

    }

    public LineObj() {
    }

    @Override
    public void create(Raw res) throws Exception {
        canvas = res.getX("canvas");
        camera = raw.getX("camera");//draw in 2d or 3d

        color = raw.getX("u_Color");
        points = raw.getX("points");

        mesh = new LineMesh(
                "lineMesh" + LineMesh.lineIndex,
                getFloatArr(points),
                points.length,
                getIntArr(points),
                res
        );
        attachCallbacks();

    }

    int[] getIntArr(Vector3f[] points) {
        int[] indices = new int[(points.length - 1) * 2];
        for (int i = 0; i < points.length - 1; i++) {
            indices[i * 2] = i;
            indices[i * 2 + 1] = i + 1;
        }
        return indices;
    }

    float[] getFloatArr(Vector3f[] points) {
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

    public void changeLine(Vector3f[] points) throws Exception {
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

class LineMesh extends ElementMesh {
    static int lineIndex = 0;
    int pointNum;

    LineMesh(String name, float[] vertexPos, int pointNum, int[] indices, Raw res) throws Exception {
        this.name = name;
        lineIndex++;
        this.pointNum = pointNum;
        primitiveType = GL_LINES;
        program = res.getX("simple3DProgram");
        attributesLocation = program.attributes;
        getAttribute(vertexPos, indices);
        getUniform(res);
        generateVAO(true);
        count = ibo.pointNum;
        offset = 0;
    }


    void getAttribute(float[] vertexPos, int[] indices) throws Exception {
        vbos = new Raw();
        if (pointNum < 2) {
            return;
        }
        vbos.add("a_Position", new VBO_D((VBORaw positionData) -> {
            positionData.add("pointNum", pointNum);
            positionData.setDataAmount("a_Position", 3);
            positionData.add("raw", vertexPos);
        }));
        ibo = new IBO_D((Raw iboRaw) -> {
            iboRaw.add("name", "lineMesh" + index + "IBO");
            iboRaw.add("raw", indices);
        });

    }


    @Override
    public void clean() {
        vbos.iterateValue((VBO vbo) -> {
            vbo.clean();
        });
        ibo.clean();
        super.clean();
    }
}