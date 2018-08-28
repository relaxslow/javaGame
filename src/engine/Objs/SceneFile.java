package engine.Objs;

import engine.Interface.InputProperty;
import engine.Util.Raw;
import engine.Util.Tools;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class SceneFile extends Scene {
    public SceneFile(String file) throws Exception {
        parseFile(file);
    }

    void parseFile(String file) throws Exception {
        List<String> lines = Tools.readAllLines(name + ".obj");
        ArrayList<ObjInfo> objInfos=new ArrayList<>();
    }
}

class ObjInfo {
    String meshName;
    Vector3f offset;
    
}