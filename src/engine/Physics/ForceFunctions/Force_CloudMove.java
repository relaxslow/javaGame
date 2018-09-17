package engine.Physics.ForceFunctions;

import engine.Interface.IMotionScene;
import engine.Interface.IPhysics;
import org.joml.Vector3f;

public class Force_CloudMove extends ForceFunction  {
    float windSpeed = 0.5f;
    Vector3f windDirect = new Vector3f(-1f, 0f, 0f);
    Vector3f windSpeedVec = new Vector3f();
    public Force_CloudMove(){
        windDirect.mul(windSpeed, windSpeedVec);
    }
    @Override
    public void apply(IPhysics obj, float interval) {
        Vector3f motionVec = ((IMotionScene)obj).getMotionVec();
        motionVec.set(windSpeedVec);
    }

  
}
