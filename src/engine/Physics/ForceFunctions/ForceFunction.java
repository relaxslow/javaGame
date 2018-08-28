package engine.Physics.ForceFunctions;

import engine.Interface.*;
import engine.Objs.PhysicObj;
import engine.Objs.Scene;
import engine.Physics.BVertex;
import engine.Physics.ForceTimer;
import engine.Util.Raw;

import java.util.LinkedList;

public abstract class ForceFunction implements IForceFunction {

    public String name = null;
    ForceFunction() {
        setName();
    }
    public void setName() {
        String fullName = getClass().getName();
        name = fullName.substring(fullName.lastIndexOf(".") + 1);
    }

    @Override
    public String getName() {
        return name;
    }
    
    ICollidable hostObj;
  

    public void setHostObj(PhysicObj obj) {
        hostObj = obj;
    }

    Scene scene;
    public void setScene(Scene scene) {
        this.scene = scene;
    }

    


}
