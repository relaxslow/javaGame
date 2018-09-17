package engine.Objs;

import engine.Interface.IGoalTest;
import engine.Interface.IInit;
import engine.Interface.IInput;
import engine.Interface.IUpdate;
import engine.Util.Raw;

public abstract class SceneObject {
    public static int index;
    public String name = null;
    public IInit initCallBack;
    public IUpdate updateCallBack;
    public IInput inputCallBack;
    public  IGoalTest goalTestFun;
    
    public String getName() {
        return name;
    }

    public Raw raw = new Raw("scene object raw data");

}
