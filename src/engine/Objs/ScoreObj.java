package engine.Objs;

import engine.Interface.InputProperty;
import engine.Objs.UIObjs.CharObj;
import engine.Objs.UIObjs.FontTextObj;
import engine.Util.Raw;

import java.nio.charset.Charset;

public class ScoreObj extends FontTextObj {

    int score = 0;
    int digitNum = 5;

    public ScoreObj(InputProperty<Raw> input) {
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

            charObj.changeChar(chars[charIndex]);
        
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
    public void create() {
        canvas = canvas.allRes.get("canvas");
        attachCamera();
        originMesh = canvas.allRes.get("NullMesh");
        getcolor();
        attachCallbacks();
        fontTexture = canvas.allRes.getX(raw.getX("fontTextureName"));
        scoreToText();
        buildText();

    }
}