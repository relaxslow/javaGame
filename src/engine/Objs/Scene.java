package engine.Objs;

import engine.Interface.InputProperty;
import engine.Util.Raw;
import org.joml.Vector3f;
import org.lwjgl.system.CallbackI;

public class Scene extends NullObj {

    public Scene(InputProperty<Raw> input) throws Exception {
        super(input);
      
    }

    @Override
    public void create(Raw res) throws Exception {
        super.create(res);
        canvas.scene=this;
    }

    public Scene() {
        
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