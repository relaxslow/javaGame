package engine.Buffer;

import engine.Interface.INeedCreate;
import engine.Interface.InputProperty;
import engine.Interface.INeedClean;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;

import engine.Util.Raw;
import engine.Util.Res;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;

public class VBO extends Res implements INeedCreate, INeedClean {
    public int id;
    public int pointNum;
    int span;
    public int FSIZE;
    int dataType;
    public int stride;

    public VBORaw data = new VBORaw("VBORaw");

    public VBO(InputProperty<VBORaw> input) {
        input.run(data);
    }

    float[] pointData;

    @Override
    public void create()  {
        name = data.getX("name");
        generateNewBuffer(data.getX("raw"), data.getX("pointNum"),true);

    }
    
    public void generateNewBuffer(float[] pointData, int pointNum, boolean generateNew) {
        this.pointData = pointData;
        this.pointNum = pointNum;
        span = pointData.length / pointNum;
        FSIZE = Float.SIZE / 8;
        Integer dt = data.get("dataType");
        if (dt == null)
            dataType = GL_FLOAT;
        else
            dataType = dt;
        stride = FSIZE * span;


        FloatBuffer offHeapBuf = null;
        try {
            offHeapBuf = MemoryUtil.memAllocFloat(pointData.length);
            offHeapBuf.put(pointData).flip();

            // Create the VBO and bint to it
            if (generateNew == true)
                id = glGenBuffers();
            glBindBuffer(GL_ARRAY_BUFFER, id);
            glBufferData(GL_ARRAY_BUFFER, offHeapBuf, GL_STATIC_DRAW);

            // Unbind the VBO
            glBindBuffer(GL_ARRAY_BUFFER, 0);


        } finally {
            if (offHeapBuf != null) {
                MemoryUtil.memFree(offHeapBuf);
            } else {
                System.out.println("error create offHeapBuf" + name);
            }
        }
    }



    @Override
    public void clean() {
        glDeleteBuffers(id);

    }


}