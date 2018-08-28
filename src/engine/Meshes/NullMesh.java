package engine.Meshes;

import engine.Buffer.VBO;
import engine.Util.Raw;

import static org.lwjgl.opengl.GL11.GL_LINES;

public class NullMesh extends ArrayMesh {
    @Override
    public void create(Raw res) throws Exception {
        name = "NullMesh";
        raw.add("name", name);
        program = res.getX("MVPProgram");
        primitiveType = GL_LINES;

        attributesLocation = program.attributes;
        vbos = new Raw();

        vbos.add("a_Position", res.get("NullVertexPosition"));
        vbos.add("a_Color", res.get("NullVertexColor"));


        getUniform(res);


        generateVAO(true);


        VBO positionVBO = vbos.get("a_Position");
        count = positionVBO.pointNum;
        first = 0;
    }
}
