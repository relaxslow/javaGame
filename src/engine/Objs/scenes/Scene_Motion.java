package engine.Objs.scenes;

import engine.Interface.IMotionScene;
import org.joml.Vector3f;

public class Scene_Motion extends Scene implements IMotionScene {
    public Scene_Motion() {

    }

    //motion is driven by forces
    Vector3f motion = new Vector3f();

    public Vector3f getSpeed() {
        canvas.player.getSpeed().mul(-1, speed);
        speed.add(motion);
        return speed;
    }

    @Override
    public Vector3f getMotionVec() {
        return motion;
    }

}
