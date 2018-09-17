package engine.Objs.UIObjs;

import engine.Interface.ICanUpdate;
import engine.Interface.IInit;
import engine.Interface.InputProperty;
import engine.Objs.NullObj;
import engine.Textures.CharInfo;
import engine.Textures.FontTexture;
import engine.Util.Raw;
import engine.View.Camera2d;
import org.joml.Vector4f;

import java.nio.charset.Charset;

//contain dynamic generate res
public class FontTextObj extends NullObj implements ICanUpdate {
    public String text;
    public int charNum;
    public FontTexture fontTexture;
    Vector4f color;

    public FontTextObj(InputProperty<Raw> input) {
        super(input);
    }

    public FontTextObj() {

    }

    @Override
    public void create() {
        super.create();

        text = raw.getX("text");
        fontTexture = canvas.allRes.getX(raw.getX("fontTextureName"));

        getcolor();

        buildText();

    }

    public void getcolor() {
        color = raw.get("color");
        if (color == null)
            color = new Vector4f(1f, 1f, 1f, 1f);
    }

    protected void buildText() {
        byte[] chars = text.getBytes(Charset.forName(fontTexture.charSetName));
        charNum = chars.length;
        wid = 0;
        hei=fontTexture.height;
//        int i = 0;
        for (int i = 0; i < charNum; i++) {
            final byte currentChar = chars[i];
            float offset = wid;
            float charWid = buildChar(currentChar, offset, i);
            wid += charWid;
        }
     
    }

    int wid;
    int hei;

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

      

        CharObj.create((Raw raw) -> {
            raw.put("charInfo", charInfo);
            raw.put("charIndex", charIndex);
            raw.put("name", this.name + "_CharObj_" + charIndex);
            raw.put("parent", this.name);
            raw.put("fontTexture", fontTexture);
            raw.put("currentChar", currentChar);
            raw.put("camera", camera);
            raw.put("init", (IInit<CharObj>) (CharObj obj) -> {
                obj.setObjColor(color);
//                obj.raw.put("u_Color", color);
                obj.matrix.translate(offset, 0f, 0f);
                obj.matrix.scale(charWid, charHei, 1f);
            });
        });


        return charWid;
    }


    public void changeText(String text) {
        //intopool
        this.text = text;

        childs.iterateValue((CharObj obj) -> {
            obj.eliminate();
        });

        childs.clear();

        buildText();


    }

    @Override
    public void clean() {
        childs.iterateValue((CharObj obj) -> {
            obj.clean();
        });
    }
}