package engine.Meshes;

import engine.Buffer.VBORaw;
import engine.Buffer.VBO_D;
import engine.Textures.Texture;
import engine.Util.Raw;

import static org.lwjgl.opengl.GL11.GL_LINES;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;

public class TextMesh extends ElementMesh {
    public TextMesh(String name, Raw res, Texture texture)  {
        this.name = name;

        raw.add("primitiveType", GL_TRIANGLES);
        getPrimitiveType();

        if (primitiveType == GL_TRIANGLES) {
            ibo = res.getX("uiTextIBO_Triangles");
            program = res.getX("textProgram");
        } else if (primitiveType == GL_LINES) {
            ibo = res.getX("uiTextIBO_Lines");
            program = res.getX("simple3DProgram");
        }

        
        attributesLocation = program.attributes;
        vbos = new Raw("vbos");

        vbos.add("a_Position", res.getX("uiTextVertexPos_VBO"));

        vbos.add("a_TextureCoords", new VBO_D((VBORaw texCoordsData) -> {
            texCoordsData.add("name", "attribute2");
            texCoordsData.add("pointNum", 4);
            texCoordsData.setDataAmount("a_TextureCoords", 2);
            texCoordsData.add("raw", new float[]{
                    0f, 0f,
                    0f, 1f,
                    1f, 1f,
                    1f, 0f,
            });

        }));

        textures = new Raw("textures");
        textures.add("u_Texture1", texture);


        getUniform(res);
        getTexture(res);

        generateVAO(true);


        count = ibo.pointNum;
        offset = 0;
    }

    //only need to clean textureCoord since it is dynamic
    @Override
    public void clean() {
        VBO_D textureCoordsVBO = vbos.get("a_TextureCoords");
        textureCoordsVBO.clean();
        super.clean();

    }
}