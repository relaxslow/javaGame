package engine.Objs.scenes;

import engine.Interface.ICanUpdate;
import engine.Interface.IPOOL;
import engine.Interface.IUpdate;
import engine.Interface.InputProperty;
import engine.Objs.NullObj;
import engine.Util.Raw;
import org.joml.Vector3f;

public class Scene extends NullObj implements ICanUpdate {
    public Scene(InputProperty<Raw> input) {
        super(input);

    }

    public Scene() {

    }

    @Override
    public void create() {
        
        raw.add("update", (IUpdate<Scene>) (float interval, Scene scene) -> {
            scene.offset.set(scene.getSpeed()).mul(interval);
            scene.operateMatrix.translate(scene.offset);
        });
        super.create();
    }


    public Vector3f getSpeed() {
        canvas.player.getSpeed().mul(-1, speed);
        return speed;
    }

    public Vector3f getAccelerate() {
        canvas.player.getAccelerate().mul(-1, accelerate);
        return accelerate;
    }

  
}