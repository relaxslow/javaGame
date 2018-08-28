package engine.Objs;

import engine.Interface.IGoalTest;
import engine.Interface.IInit;
import engine.Interface.IInput;
import engine.Interface.IUpdate;
import engine.Util.Raw;

abstract class SceneObject {
    public static int index;
    public String name = null;
    IInit initCallBack;
    IUpdate updateCallBack;
    IInput inputCallBack;
    IGoalTest goalTestFun;
    
    public String getName() {
        return name;
    }

    public Raw raw = new Raw();

}
