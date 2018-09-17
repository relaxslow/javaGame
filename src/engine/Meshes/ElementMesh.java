package engine.Meshes;

import engine.Buffer.IBO;
import engine.Interface.INeedClean;
import engine.Interface.INeedCreate;
import engine.Interface.InputProperty;
import engine.Util.Raw;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class ElementMesh extends Mesh implements INeedCreate, INeedClean {

    public ElementMesh(InputProperty<Raw> input)  {
        input.run(raw);
    }


    public int count;
    public int offset;
    public IBO ibo;

    public ElementMesh() {
    }

    @Override
    public void create()  {
        name = raw.getX("name");
        program = canvas.allRes.getX(raw.getX("program"));
        getPrimitiveType();


        getIndice(canvas.allRes);

        getAttribute(canvas.allRes);
        getUniform(canvas.allRes);
        getTexture(canvas.allRes);

        generateVAO(true);


        Integer inputCount = raw.get("count");
        if (inputCount == null)
            count = ibo.pointNum;
        Integer inputOffset = raw.get("offset");
        if (inputOffset == null)
            offset = 0;
    }

    @Override
    public void generateVAO(boolean generateNew)  {
        if (generateNew)
            vaoId = glGenVertexArrays();
        glBindVertexArray(vaoId);

        bindAttributes();

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo.id);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
    }
    public void buildMesh()  {
       

    }

    void getIndice(Raw res)  {
        String[] indices = raw.getX("indices");
        if (primitiveType == GL_TRIANGLES)
            ibo = res.getX(indices[0]);
        else if (primitiveType == GL_LINES) {
            ibo = res.getX(indices[1]);
        }
    }


    @Override
    public void render() {//no uniform
        glBindVertexArray(vaoId);

        glDrawElements(primitiveType, ibo.pointNum, GL_UNSIGNED_INT, 0);

    }


}
