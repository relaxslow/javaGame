package engine.Textures;

import engine.Interface.InputProperty;
import engine.Util.Raw;

public class TextureAtlas extends Texture {

    int row;
    int col;

    public TextureAtlas(InputProperty<Raw> input)  {
        super(input);

    }

    @Override
    public void create() {
        super.create();
        row = raw.get("row");
        col = raw.get("col");
    }
}