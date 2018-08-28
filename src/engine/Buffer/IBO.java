package engine.Buffer;

import engine.Util.Raw;
import engine.Interface.InputProperty;
import engine.Interface.INeedClean;
import engine.Interface.INeedCreate;
import engine.Util.Res;
import org.lwjgl.system.MemoryUtil;

import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;

public class IBO extends Res implements INeedCreate, INeedClean {
    public int id;
    public int pointNum;
    Raw raw = new Raw();
    private int[] indiceData;

    public IBO(InputProperty<Raw> input) throws Exception {
        input.run(raw);
    }

    @Override
    public void create(Raw res) throws Exception {
        name = raw.getX("name");
        generateNewBuffer(raw.getX("raw"), true);
    }

    public void generateNewBuffer(int[] indiceData, boolean generateNew) {
        this.indiceData = indiceData;
        pointNum = indiceData.length;

        IntBuffer buffer = null;
        try {
            buffer = MemoryUtil.memAllocInt(indiceData.length);
            buffer.put(indiceData).flip();

            if (generateNew)
                id = glGenBuffers();
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, id);
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);


            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);


        } finally {
            if (buffer != null) {
                MemoryUtil.memFree(buffer);
            } else {
                System.out.println("error create buffer" + name);
            }
        }
    }


    @Override
    public void clean() {
        glDeleteBuffers(id);
    }
}