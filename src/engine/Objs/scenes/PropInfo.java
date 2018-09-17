package engine.Objs.scenes;

import engine.Interface.InputProperty;
import org.joml.Vector3f;

public class PropInfo {
    public PropInfo(InputProperty<PropInfo> input) {
        input.run(this);
    }

//    public final static int BLOCK = 0;
//    public final static int PROP = 1;
//    public final static int COLLIDABLE = 2;
//    public int type = PROP;
    public String meshName;
    public Vector3f offset = new Vector3f(0f, 0f, 0f);
    public Vector3f pos = new Vector3f(0f, 0f, 0f);
    public Vector3f size = new Vector3f(1f, 1f, 1f);

}
