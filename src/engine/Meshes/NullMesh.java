package engine.Meshes;

import engine.Buffer.VBO;
import engine.Util.Raw;

import static org.lwjgl.opengl.GL11.GL_LINES;

public class NullMesh extends ArrayMesh {
    @Override
    public void create()  {
        name = "NullMesh";
        raw.add("name", name);
        program = canvas.allRes.getX("MVPProgram");
        primitiveType = GL_LINES;

        attributesLocation = program.attributes;
        vbos = new Raw("vbos");

        vbos.add("a_Position", canvas.allRes.get("NullVertexPosition"));
        vbos.add("a_Color", canvas.allRes.get("NullVertexColor"));


        getUniform(canvas.allRes);


        generateVAO(true);


        VBO positionVBO = vbos.get("a_Position");
        count = positionVBO.pointNum;
        first = 0;
    }
}
