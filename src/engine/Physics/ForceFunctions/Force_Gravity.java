package engine.Physics.ForceFunctions;

import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;
import engine.Interface.*;
import engine.Util.Constant;
import org.joml.Vector3f;

public class Force_Gravity extends ForceFunction implements IAccelerate, INeedData {
  

    float accumulator = 0;
    Vector3f accelerate = new Vector3f(0f, -20f, 0f);
    Vector3f finalSpeed = new Vector3f();

    @Override
    public void init(Object data) {
        accumulator = 0;
        initSpeed.set((Vector3f) data);
    }
    @Override
    public void apply(IPhysics obj, float interval) {
        accumulator += interval;
        accelerate.mul(accumulator, finalSpeed);
        finalSpeed.add(initSpeed);

        obj.getSpeed().add(finalSpeed);
    }

    @Override
    public Vector3f getAcceleration() {
        return accelerate;
    }


    Vector3f initSpeed = new Vector3f();

    
}

