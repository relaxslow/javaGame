package engine.Meshes;

import engine.Buffer.VBO;
import engine.Interface.INeedClean;
import engine.Interface.INeedCreate;
import engine.Interface.InputProperty;
import engine.Util.Raw;

import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class ArrayMesh extends Mesh implements INeedCreate, INeedClean {

    int first;
    int count;

    public ArrayMesh(InputProperty<Raw> input)  {
        input.run(raw);
    }

    ArrayMesh() {

    }

    @Override
    public void create() {
        name = raw.getX("name");
        program = canvas.allRes.getX(raw.getX("program"));

        getPrimitiveType();


        getAttribute(canvas.allRes);
        getUniform(canvas.allRes);
        getTexture(canvas.allRes);

        generateVAO(true);

        VBO positionVBO = vbos.get("a_Position");
        count = positionVBO.pointNum;
        first = 0;
    }

    @Override
    public void render() {

        glBindVertexArray(vaoId);
        glDrawArrays(primitiveType, first, count);
    }


}
