package engine.Interface;

import engine.Objs.PhysicObj;
import engine.Objs.Scene;
import engine.Physics.BVertex;

import java.util.Iterator;

public interface IForceFunction {
    String getName();
    void apply(IPhysics obj, float interval);


    void setHostObj(PhysicObj obj);
//    void setRoot(BVertex vertex);
    void setScene(Scene scene);
    
}
