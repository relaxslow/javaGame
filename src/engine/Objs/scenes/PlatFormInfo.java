package engine.Objs.scenes;

import engine.Interface.InputProperty;
import engine.Util.Raw;
import org.joml.Vector3f;

import java.util.ArrayList;

public class PlatFormInfo  {
    public static SceneManager sceneManager;

    public PlatFormInfo(InputProperty<PlatFormInfo> input) {
        input.run(this);

    }

    public Raw blocks = new Raw("blocks");
    public ArrayList<PropInfo> props = new ArrayList<>();
    public ArrayList<PropInfo> collidables = new ArrayList<>();

   

    public String name;
    public Vector3f pos = new Vector3f(0f, 0f, 0f);
//    public ArrayList<String> adjacent = new ArrayList<>();

//    public void addAdjacent(String name) {
////        PlatFormInfo info = sceneManager.getX(name);
//
//    }

}


