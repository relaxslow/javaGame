package engine.Meshes;

import engine.Buffer.IBO_D;
import engine.Buffer.VBO;
import engine.Buffer.VBORaw;
import engine.Buffer.VBO_D;
import engine.Util.Raw;

import static org.lwjgl.opengl.GL11.GL_LINES;

public class LineMesh extends ElementMesh {
    public static int lineIndex = 0;
    public int pointNum;

    public LineMesh(String name, float[] vertexPos, int pointNum, int[] indices)  {
        this.name = name;
        lineIndex++;
        this.pointNum = pointNum;
        primitiveType = GL_LINES;
        program = canvas.allRes.getX("simple3DProgram");
        attributesLocation = program.attributes;
        getAttribute(vertexPos, indices);
        getUniform(canvas.allRes);
        generateVAO(true);
        count = ibo.pointNum;
        offset = 0;
    }


    void getAttribute(float[] vertexPos, int[] indices)  {
        vbos = new Raw("vbos");
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