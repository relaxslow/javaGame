package engine.Textures;

import engine.Interface.InputProperty;
import engine.Util.Raw;

public class TextureAtlas extends Texture {

    int row;
    int col;

    public TextureAtlas(InputProperty<Raw> input) throws Exception {
        super(input);

    }

    @Override
    public void create(Raw res) throws Exception {
        super.create(res);
        row = raw.get("row");
        col = raw.get("col");
    }
}