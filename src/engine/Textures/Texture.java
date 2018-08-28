package engine.Textures;

import de.matthiasmann.twl.utils.PNGDecoder;
import engine.Interface.INeedClean;
import engine.Interface.INeedCreate;
import engine.Interface.InputProperty;
import engine.Util.Raw;
import engine.Util.Res;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glDeleteTextures;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;

public class Texture extends Res implements INeedClean, INeedCreate {

    public int id;
    Raw raw = new Raw();
    public int width;
    public int height;

    public Texture(InputProperty<Raw> input) throws Exception {
        input.run(raw);
        name = raw.getX("name");
    }


    @Override
    public void create(Raw res) throws Exception {

        InputStream is = Texture.class.getResourceAsStream(name);
        buildTexture(is);
    }

    void buildTexture(InputStream is) throws IOException {
        // Load engine.Texture file
        PNGDecoder decoder = new PNGDecoder(is);

        width = decoder.getWidth();
        height = decoder.getHeight();
        // Load texture contents into a byte buffer
        ByteBuffer buf = ByteBuffer.allocateDirect(4 * width * height);
        decoder.decode(buf, width * 4, PNGDecoder.Format.RGBA);
        buf.flip();

        // Create a new OpenGL texture 
        id = glGenTextures();
        // Bind the texture
        glBindTexture(GL_TEXTURE_2D, id);

        // Tell OpenGL how to unpack the RGBA bytes. Each component is 1 byte size
        glPixelStorei(GL_UNPACK_ALIGNMENT, 1);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        // Upload the texture raw
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, this.width, this.height, 0, GL_RGBA, GL_UNSIGNED_BYTE, buf);
        // Generate Mip Map
        glGenerateMipmap(GL_TEXTURE_2D);
    }


    @Override
    public void clean() {
        glDeleteTextures(id);
    }


}

