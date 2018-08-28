package engine.Objs;

import engine.Buffer.VBO_D;
import engine.Interface.INeedClean;
import engine.Interface.InputProperty;
import engine.Meshes.TextMesh;

import engine.Textures.CharInfo;
import engine.Textures.FontTexture;
import engine.Util.Error;
import engine.Util.Raw;
import engine.Util.Pool;
import engine.Util.Tools;
import engine.View.Camera2d;

import java.util.ArrayList;
import java.util.List;

public class CharObj extends Obj implements INeedClean {
    static Pool<CharObj> pool = new Pool<>("CharObj_Pool");

    static CharObj create(InputProperty<Raw> input) throws Exception {
        CharObj newObj = pool.create(CharObj.class);
        input.run(newObj.raw);
        newObj.matrix.identity();
        newObj.operateMatrix.identity();
        newObj.createName();
        return newObj;
    }

    static void reclaim(CharObj obj) {
        obj.removeFromRenderGroups();
        pool.reclaim(obj);

    }

    byte currentChar;
    FontTexture texture;
    int charIndex;
    CharInfo charInfo;

    //    engine.CharObj(engine.InputProperty<engine.Raw> input) throws Exception {
//        super(input);
//    }
//
//    engine.CharObj() {
//
//    }


    @Override
    public void create(Raw res) throws Exception {
        charIndex = raw.getX("charIndex");
        charInfo = raw.getX("charInfo");
        texture = raw.getX("fontTexture");


        canvas = res.get("canvas");
        camera = raw.getX("camera");
        if (mesh == null) ;
        mesh = new TextMesh(this.name + "_mesh", canvas.allRes, texture);
        attachCallbacks();
        if (finalTextures == null)
            attachCustomTexture(res);
        changeChar(raw.getX("currentChar"));


    }



    void changeChar(byte c) throws Exception {
        this.currentChar = c;
        VBO_D textureCoordsVBO = mesh.vbos.get("a_TextureCoords");
        textureCoordsVBO.generateNewBuffer(getNewTexCoords(), 4,false);
        if (mesh.vaoId == 0)
            mesh.generateVAO(true);
        else
            mesh.generateVAO(false);
    }

    float[] getNewTexCoords() {

        CharInfo charInfo = texture.charMap.get(currentChar);

        List<Float> textCoords = new ArrayList();

        if (camera instanceof Camera2d) {
            textCoords.add((float) charInfo.startX / (float) texture.width);
            textCoords.add(1.0f);
            textCoords.add((float) charInfo.startX / (float) texture.width);
            textCoords.add(0.0f);


            textCoords.add((float) (charInfo.startX + charInfo.width) / (float) texture.width);
            textCoords.add(0.0f);
            textCoords.add((float) (charInfo.startX + charInfo.width) / (float) texture.width);
            textCoords.add(1.0f);
        } else {

            textCoords.add((float) charInfo.startX / (float) texture.width);
            textCoords.add(0.0f);
            textCoords.add((float) charInfo.startX / (float) texture.width);
            textCoords.add(1.0f);


            textCoords.add((float) (charInfo.startX + charInfo.width) / (float) texture.width);
            textCoords.add(1.0f);
            textCoords.add((float) (charInfo.startX + charInfo.width) / (float) texture.width);
            textCoords.add(0.0f);
        }


        float[] textCoordsArr = Tools.listToArray(textCoords);
        return textCoordsArr;
    }

    @Override
    public void clean() {
        mesh.clean();

    }
}