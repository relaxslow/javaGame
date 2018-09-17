package engine.Interface;

import engine.Objs.PhysicObj;

public interface IForceFunction {
    String getName();
    void apply(IPhysics obj, float interval);


    void setHostObj(PhysicObj obj);
//    void setRoot(BVertex vertex);
//    void setScene(Scene scene);
    
}
