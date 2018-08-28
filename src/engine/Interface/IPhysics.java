package engine.Interface;

import engine.Objs.PhysicObj;
import engine.Objs.Scene;
import engine.Physics.BVertex;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public interface IPhysics {
    //add a single force to obj's force collection
    void addForce(Class<?> aclass,Object data);
     void removeForce(Class<?> forceClass);
    //run forcefunctions  from obj's force collection 
    IForceFunction getForce(Class<?> forceClass);
    void applyForces(float interval);
    
    Vector3f getSpeed();
    Vector3f getGlobalSpeed();
    
    Vector3f getAccelerate();
    Vector3f getGlobalAccelerate();
    
    Vector3f getRelateVel();
    Matrix4f getModelMatrix();

    
    Vector3f getRelateAccelerate();
    
 
    
}
