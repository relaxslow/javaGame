package engine.Objs;

import engine.Interface.InputProperty;
import engine.Util.Error;
import engine.Util.Raw;

import java.nio.charset.Charset;

public class ScoreObj extends FontTextObj  {

    int score = 0;
    int digitNum = 5;

    public ScoreObj(InputProperty<Raw> input) throws Exception {
        super(input);


    }


    float accumulator = 0f;

    public void increase(float interval) {
        accumulator += interval;
        if (accumulator >= 1f) {
            this.score += 1f;
            accumulator -= 1f;
        }

        scoreToText();
        byte[] chars = text.getBytes(Charset.forName(fontTexture.charSetName));
        charNum = chars.length;

        childs.iterateValue((CharObj charObj) -> {
            int charIndex = charObj.charIndex;
            try {
                charObj.changeChar(chars[charIndex]);
            } catch (Exception e) {
                Error.fatalError(e,"change Char failed");
            }
        });

    }

    void scoreToText() {
        text = String.valueOf(score);
        int complement = 0;
        if (text.length() < digitNum)
            complement = digitNum - text.length();
        for (int i = 0; i < complement; i++)
            text = "0" + text;

    }

    @Override
    public void create(Raw res) throws Exception {
        canvas = res.get("canvas");
        camera = raw.get("camera");
        originMesh = res.get("NullMesh");
        getcolor();
        attachCallbacks();
        fontTexture = res.getX(raw.getX("fontTextureName"));
        scoreToText();
        buildText();

    }
}