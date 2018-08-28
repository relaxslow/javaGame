package engine.Objs;

import engine.Interface.IInit;
import engine.Interface.InputProperty;
import engine.Textures.CharInfo;
import engine.Textures.FontTexture;
import engine.Util.Raw;
import engine.Util.Error;
import engine.View.Camera2d;
import org.joml.Vector4f;

import java.nio.charset.Charset;

public class FontTextObj extends NullObj {
    String text;
    int charNum;
    FontTexture fontTexture;
    Vector4f color;

    public FontTextObj(InputProperty<Raw> input) throws Exception {
        super(input);
    }

    @Override
    public void create(Raw res) throws Exception {
        super.create(res);

        text = raw.getX("text");
        fontTexture = res.getX(raw.getX("fontTextureName"));

        getcolor();

        buildText();

    }

    void getcolor() {
        color = raw.get("color");
        if (color == null)
            color = new Vector4f(1f, 1f, 1f, 1f);
    }

    protected void buildText() {
        byte[] chars = text.getBytes(Charset.forName(fontTexture.charSetName));
        charNum = chars.length;
        float start = 0;
//        int i = 0;
        for (int i = 0; i < charNum; i++) {
            final byte currentChar = chars[i];
            float offset = start;
            float charWid = buildChar(currentChar, offset, i);
            start += charWid;
        }
    }

    private float buildChar(byte currentChar, float offset, int charIndex) {
        float charWid;
        float charHei;

        CharInfo charInfo = fontTexture.charMap.get(currentChar);
        if (camera instanceof Camera2d) {
            charWid = charInfo.width;
            charHei = fontTexture.height;

        } else {
            charWid = (float) charInfo.width / (float) fontTexture.height;
            charHei = 1;
        }

        try {
            canvas.createObj(CharObj.create((Raw raw) -> {
                raw.put("charInfo", charInfo);
                raw.put("charIndex", charIndex);
                raw.put("name", this.name + "_CharObj_" + charIndex);
                raw.put("parent", this.name);
                raw.put("fontTexture", fontTexture);
                raw.put("currentChar", currentChar);
                raw.put("camera", camera);
                raw.put("init", (IInit<CharObj>) (CharObj obj) -> {
                    obj.raw.put("u_Color", color);
                    obj.matrix.translate(offset, 0f, 0f);
                    obj.matrix.scale(charWid, charHei, 1f);
                });

            }));
        } catch (Exception e) {
            Error.fatalError(e, "can't create CharObj");
        }

        return charWid;
    }


    public void changeText(String text) {
        //intopool
        this.text = text;
        childs.iterateValue((CharObj obj) -> {
            CharObj.reclaim(obj);

        });
        childs.clear();

        buildText();


    }

}