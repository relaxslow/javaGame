package engine.Objs.scenes;

import engine.Interface.InputProperty;
import engine.Objs.Canvas;
import org.joml.Vector3f;

import java.util.ArrayList;

public class MapNode {
    public static SceneManager sceneManager;
    public static Canvas canvas;

    MapNode(InputProperty<MapNode> input) {
        input.run(this);
        platFormInfo = sceneManager.platformType.getX(type);
    }

    public String type;
    public PlatFormInfo platFormInfo;
    public PlatForm platForm;
    public Vector3f originPos = new Vector3f();
    public Vector3f createPos = new Vector3f();
    public ArrayList<MapNode> neighbors = new ArrayList<>();

    void addNeighbor(String nodeName) {
        MapNode neighbor = sceneManager.map.getX(nodeName);
        neighbors.add(neighbor);
        neighbor.neighbors.add(this);
    }

    public boolean shouldInScreen() {
        if (createPos.x < -canvas.generateBorder.wid
                || createPos.x > canvas.generateBorder.wid
                || createPos.y < -canvas.generateBorder.hei
                || createPos.y > canvas.generateBorder.wid)
            return false;
        return true;
    }


    public ArrayList<MapNodeLinkInfo> linked = new ArrayList<>();

    void addLinked(String blockName) {
        
    }
}
