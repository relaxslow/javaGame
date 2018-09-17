package engine.Objs.UIObjs;

import engine.Buffer.VBO_D;
import engine.Interface.INeedClean;
import engine.Interface.IPOOL;
import engine.Interface.InputProperty;
import engine.Meshes.TextMesh;
import engine.Objs.Canvas;
import engine.Objs.Obj;
import engine.Textures.CharInfo;
import engine.Textures.FontTexture;
import engine.Util.Pool;
import engine.Util.Raw;
import engine.Util.Tools;
import engine.View.Camera2d;

import java.util.ArrayList;
import java.util.List;

public class CharObj extends Obj implements INeedClean, IPOOL {
    public static Pool<CharObj> pool = new Pool<>("CharObj_Pool");

    static void create( InputProperty<Raw> input) {
        CharObj newObj = pool.create(CharObj.class);
        input.run(newObj.raw);
        if (newObj.getCreateFrom() == Pool.CREATE_FROM_NEW) {
            newObj.initFromNew();

        } else if (newObj.getCreateFrom() == Pool.CREATE_FROM_POOL) {
            newObj.initFromPool();
        }

    }

    @Override
    public void initFromNew() {
        createNameByRaw();
        canvas.createObj(this);
    }

    @Override
    public void initFromPool() {
        matrix.identity();
        operateMatrix.identity();
        canvas.attachParent(this);
        addToRenderGroups();
        create();
    }

    @Override
    public void eliminate() {
        removeFromRenderGroups();
        pool.reclaim(this);
    }
    

    byte currentChar;
    FontTexture texture;
   public  int charIndex;
    CharInfo charInfo;

    @Override
    public void create() {
        charIndex = raw.getX("charIndex");
        charInfo = raw.getX("charInfo");
        texture = raw.getX("fontTexture");
        
        camera = raw.getX("camera");
        if (mesh == null) ;
        mesh = new TextMesh(this.name + "_mesh", canvas.allRes, texture);
        attachCallbacks();
        if (finalTextures == null)
            attachCustomTexture();
        changeChar(raw.getX("currentChar"));
    }


    public void changeChar(byte c) {
        this.currentChar = c;
        VBO_D textureCoordsVBO = mesh.vbos.get("a_TextureCoords");
        textureCoordsVBO.generateNewBuffer(getNewTexCoords(), 4, false);
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

//class CharInitFun implements IInit<CharObj> {
//    Vector4f color;
//    float offset;
//    float charWid;
//    float charHei;
//
//    CharInitFun(Vector4f color, float offset, float charWid, float charHei) {
//        this.color = color;
//        this.offset = offset;
//        this.charWid = charWid;
//        this.charHei = charHei;
//    }
//
//    @Override
//    public void init(CharObj obj) {
//        obj.raw.put("u_Color", color);
//        obj.matrix.translate(offset, 0f, 0f);
//        obj.matrix.scale(charWid, charHei, 1f);
//    }
//}