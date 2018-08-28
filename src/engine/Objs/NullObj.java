package engine.Objs;

import engine.Interface.InputProperty;
import engine.Util.Raw;

public class NullObj extends PhysicObj {
    public NullObj(InputProperty<Raw> input) throws Exception {
        super(input);
    }
    NullObj() {
        
    }
    @Override
    public void create(Raw res) throws Exception {

        canvas = res.get("canvas");
        camera = raw.get("camera");
        originMesh = res.get("NullMesh");
        attachCallbacks();
        calculateMatrix();
    }
}
