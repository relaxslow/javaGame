package engine.Objs.UIObjs;

import engine.Input.KeyBoard;
import engine.Input.Mouse;
import engine.Interface.ICanInput;
import engine.Interface.IInput;
import engine.Interface.IMouseClick;
import engine.Objs.NullObj;
import engine.Objs.Obj;
import engine.Util.Debug;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class Button extends NullObj implements ICanInput {

    int wid = 100;
    int hei = 20;
    static int index = 0;

    Obj background = new Obj();
    FontTextObj textObj = new FontTextObj();


    boolean mouseDown = false;
    Vector3f pos = new Vector3f();

    boolean mouseOnButton(Mouse mouse) {
        if (mouse.currentPos.x >= pos.x && mouse.currentPos.x <= pos.x + wid &&
                mouse.currentPos.y >= pos.y && mouse.currentPos.y <= pos.y + hei) {
            return true;
        }
        return false;
    }

    public IMouseClick mouseClickFun;

    public Button(String text) {
        name = "button" + index;
        attachParent(canvas);
        originMesh = canvas.allRes.getX("NullMesh");
        camera = canvas.UICamera;
        matrix.scale(10f, 10f, 10f);
        calculateMatrix();
        attachInputFun((IInput<Button>) (KeyBoard key, Mouse mouse, Button button) -> {
            modelMatrix.getTranslation(pos);

            if (mouseDown == false && mouse.leftButtonPressed == true) {

                if (mouseOnButton(mouse)) {
//                    Debug.log("mouse down");
                    mouseDown = true;
                    operateMatrix.translate(0f, 2f, 0f);
                    if (mouseClickFun != null) {
                        mouseClickFun.run();
                    }
                }
            }
            if (mouseDown == true && mouse.leftButtonPressed == false) {
//                Debug.log("mouse up");
                mouseDown = false;
                operateMatrix.translate(0f, -2f, 0f);
            }


        });

        addToRenderGroups();
        background.name = "buttonBackground" + index;
        background.attachParent(this);
        background.mesh = canvas.allRes.getX("/model/button");
        background.camera = canvas.UICamera;
        background.matrix.translate(0f, 0f, -0.5f);
        background.matrix.scale(wid, hei, 1f);
        background.calculateMatrix();
        background.addToRenderGroups();


        textObj.name = "buttonText" + index;
        textObj.attachParent(this);
        textObj.originMesh = canvas.allRes.get("NullMesh");
        textObj.camera = canvas.UICamera;
        textObj.matrix.translate(0f, 0f, 0.5f);
        textObj.matrix.scale(10f, 10f, 10f);
        textObj.calculateMatrix();
        textObj.addToRenderGroups();
        textObj.text = text;
        textObj.fontTexture = canvas.allRes.getX("DebugTextFontTexture");
        textObj.color = new Vector4f(1f, 1f, 1f, 1f);
        textObj.buildText();

        float offsetX = wid / 2 - textObj.wid / 2;
        float offsetY = hei / 2 - textObj.hei / 2;
        textObj.operateMatrix.translate(offsetX, offsetY, 0);

        index++;
    }

    void setSize(int wid, int hei) {
        this.wid = wid;
        this.hei = hei;

    }

    @Override
    public void clean() {
        textObj.clean();
    }
}
