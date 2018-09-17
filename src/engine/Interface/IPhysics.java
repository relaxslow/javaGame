package engine.Interface;

import org.joml.Matrix4f;
import org.joml.Vector3f;

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

    Matrix4f getOperateMatrix();
    
}
