package engine.Util;

import engine.Interface.IHasName;
import engine.Objs.Canvas;

public abstract class Res implements IHasName {
    public static int index;
    public static Canvas canvas;

    public String name = null;

    @Override
    public String getName() {
        return name;
    }

}