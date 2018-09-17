package engine.Objs;

import engine.Interface.InputProperty;
import engine.Util.Raw;

public class NullObj extends PhysicObj {
    public NullObj(InputProperty<Raw> input)  {
        super(input);
    }
    public NullObj() {
        
    }
    @Override
    public void create()  {
        originMesh = canvas.allRes.get("NullMesh");
        attachCamera();
        attachCallbacks();
        calculateMatrix();
    }
}
