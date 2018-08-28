package engine.Util;

import engine.Interface.IHasName;

public abstract class Res implements IHasName {

    public static int index;


    public String name = null;

    @Override
    public String getName() {
        return name;
    }

}