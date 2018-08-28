package engine.Programs;

import engine.Interface.INeedClean;
import engine.Interface.INeedCreate;
import engine.Interface.InputProperty;
import engine.Util.Raw;
import engine.Util.Res;

import static org.lwjgl.opengl.GL11.GL_TRUE;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glDeleteProgram;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL30.glBindFragDataLocation;

public class Program extends Res implements INeedCreate, INeedClean {
    public int id;

    ProgramRaw data = new ProgramRaw();

    public Program(InputProperty<ProgramRaw> input) throws Exception {
        input.run(data);
    }


    @Override
    public void create(Raw allRes) throws Exception {
        name = data.getX("name");
        String vertexShaderName = data.getX("vertexShaderName");
        String fragShaderName = data.getX("fragShaderName");
        Shader vertexShader = allRes.get(vertexShaderName);
        Shader fragShader = allRes.get(fragShaderName);
        Integer defaultFragData = data.getFragData("fragColor");
        if (defaultFragData == null)
            data.addFragData("fragColor", 0);


        this.id = glCreateProgram();
        if (this.id == 0) {
            throw new Exception("Could not create engine.Shader");
        }
        glAttachShader(id, vertexShader.id);
        glAttachShader(id, fragShader.id);

        specifyFragDataLocation();
        glLinkProgram(id);

        int status = glGetProgrami(id, GL_LINK_STATUS);
        if (status != GL_TRUE) {
            throw new RuntimeException(glGetProgramInfoLog(id));
        }
        glDetachShader(id, vertexShader.id);
        glDetachShader(id, fragShader.id);

        glUseProgram(id);


        specifyAttributeLocation();
        specifyUniformLocation();

        textureChannels = data.get("channels");
    }

   public Raw attributes;
    public Raw uniforms;
    public Raw texturesUniforms;
   public Raw textureChannels;

    void specifyAttributeLocation() throws Exception {
        String[] inputAttributes = data.getX("attributes");
        attributes = new Raw();
        for (int i = 0; i < inputAttributes.length; i++) {
            String attributeName = inputAttributes[i];
            attributes.add(attributeName, glGetAttribLocation(id, attributeName));
        }


    }

    void specifyUniformLocation() throws Exception {
        String[] inputUniforms = data.get("uniforms");
        if (inputUniforms == null) return;
        uniforms = new Raw();
        for (int i = 0; i < inputUniforms.length; i++) {
            String uniformName = inputUniforms[i];
            uniforms.add(uniformName, glGetUniformLocation(id, uniformName));
        }

        textureChannels = data.get("channels");
        if (textureChannels == null) return;
        texturesUniforms = new Raw();
        textureChannels.iterateKeyX((String uniformName) -> {
            texturesUniforms.add(uniformName, glGetUniformLocation(id, uniformName));
        });


    }

    void specifyFragDataLocation() {
        Raw fragDatas = data.get("fragDatas");
        fragDatas.iterateKeyValue((String name, Integer location) -> {
            glBindFragDataLocation(id, location, name);
        });
    }


    @Override
    public void clean() {
        glDeleteProgram(id);
    }
}