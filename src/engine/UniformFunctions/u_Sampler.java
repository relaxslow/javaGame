package engine.UniformFunctions;

import engine.Objs.Obj;
import engine.Textures.Texture;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL20.glUniform1i;

public class u_Sampler extends UniformFunction {
    public u_Sampler() {
        name = "u_Sampler";
    }

    @Override
    public void run(Obj obj, int location, String name) {
        Texture texture = obj.finalTextures.get(name);
        int channel = obj.mesh.texturesChannels.get(name);

        glActiveTexture(GL_TEXTURE0 + channel);
        glBindTexture(GL_TEXTURE_2D, texture.id);
        glUniform1i(location, channel);
    }
}
