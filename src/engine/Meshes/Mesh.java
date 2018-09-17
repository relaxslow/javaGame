package engine.Meshes;

import engine.Buffer.VBO;
import engine.Buffer.VBOAttribute;
import engine.Interface.IRenderMesh;
import engine.Interface.IUniformFunction;

import engine.Objs.Canvas;
import engine.Programs.Program;
import engine.Textures.Texture;
import engine.Util.Raw;
import engine.Util.Debug;
import engine.Util.Res;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_LINES;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public abstract class Mesh extends Res implements IRenderMesh {
    public Raw raw = new Raw("Mesh raw data");
    public Program program;
    public int primitiveType;
    public int vaoId;
    public Raw vbos = new Raw("mesh vbos collection");//{key:attributeName,VBO:buffers}
    public Raw attributesLocation;//{key:attributeName,int:location}
    public Raw uniformsLocation;//{key:uniformName,int:location}
    public Raw uniformsFunction;//{key:uniformName, Uniformfunction:function}

    public Raw texturesLocation;
    public Raw texturesFunction;
    public Raw texturesChannels;//{key:uniformName,int:channel}
    public Raw textures;//{key:uniformName,Texture:texture}

    void getPrimitiveType()  {
        if (Debug.wireFrame == true)
            primitiveType = GL_LINES;
        else
            primitiveType = raw.getX("primitiveType");

    }

    public void getAttribute(Raw res)  {
        attributesLocation = program.attributes;

        attributesLocation.iterateKeyValueX((String attributeName, Integer location) -> {
            if (location >= 0) {
                Raw bufferNames = raw.getX("attributes");
                String bufferName = bufferNames.getX(attributeName);
                VBO buffer = res.getX(bufferName); //get buffer
                vbos.add(attributeName, buffer);
            }

        });

    }

    public void getUniform(Raw res)  {
        uniformsLocation = program.uniforms;
        if (uniformsLocation == null) return;
        uniformsFunction = new Raw("uniformFunction collection");
        uniformsLocation.iterateKeyValueX((String uniformName, Integer location) -> {
            if (location >= 0) {
                IUniformFunction fun = res.getX(uniformName); //attach function
                uniformsFunction.add(uniformName, fun);
            }
        });


    }

    void getTexture(Raw res)  {
        texturesChannels = program.textureChannels;
        if (texturesChannels == null) return;

        texturesLocation = program.texturesUniforms;

        if (textures == null) {
            Raw defaultTextureNames = raw.get("textures");//default textures

            textures = new Raw("textures in mesh");
            texturesChannels.iterateKeyX((String uniformName) -> {
                Texture texture;
                if (defaultTextureNames == null || defaultTextureNames.getX(uniformName) == null)
                    texture = res.getX("/textures/default.png");
                else
                    texture = res.getX(defaultTextureNames.getX(uniformName));
                textures.add(uniformName, texture);
            });
        }


        texturesFunction = new Raw("textures functions in mesh");
        texturesChannels.iterateKeyX((String uniformName) -> {
            IUniformFunction fun = res.getX("u_Sampler");
            texturesFunction.add(uniformName, fun);
        });

    }


    void bindAttributes()  {
        attributesLocation.iterateKeyValueX((String name, Integer location) -> {
            VBO vbo = vbos.getX(name);
            glBindBuffer(GL_ARRAY_BUFFER, vbo.id);
            int beginIndex = getBeginIndex(name, vbo);
            glVertexAttribPointer(location, 3, GL_FLOAT, false, vbo.stride, vbo.FSIZE * beginIndex);
            glEnableVertexAttribArray(location);

        });
    }


    int getBeginIndex(String attributeName, VBO buffer)  {
        VBOAttribute attributes = buffer.data.getX("attributes");
        return attributes.getAccumulateResult((String name, Integer dataAmount) -> {
            if (name.equals(attributeName))
                return -1;
            else return dataAmount;
        });
    }



    public void generateVAO(boolean generateNew)  {
        if (generateNew)
            vaoId = glGenVertexArrays();
        glBindVertexArray(vaoId);
        bindAttributes();
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
    }
    
    public void clean() {
        glBindVertexArray(0);
        glDisableVertexAttribArray(0);
        glDeleteVertexArrays(vaoId);
    }

}
