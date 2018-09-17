package engine.Objs.scenes;

import engine.Util.Raw;

import java.util.ArrayList;

public class SceneManager {
    public Raw platformType = new Raw("platform type in scene manager");
    public Raw map = new Raw("map nodes in scene manager");


    public void init() {
        definePlatformType();
        defineMap();
    }

    //difine the map when player is at 0,0,0
    private void defineMap() {
        map.add("root", new MapNode((MapNode node) -> {
            node.type = "p1";
            node.originPos.set(0f, -2f, 0.2f);
            node.createPos.set(0f, -2f, 0.2f);
        }));

        map.add("n0", new MapNode((MapNode node) -> {
            node.type = "p2";
            node.originPos.set(-5f, -2f, 0.2f);
            node.addNeighbor("root");
        }));
        map.add("n1", new MapNode((MapNode node) -> {
            node.type = "p2";
            node.originPos.set(-10f, -2f, 0.2f);
            node.addNeighbor("n0");
        }));
        map.add("n2", new MapNode((MapNode node) -> {
            node.type = "end";
            node.originPos.set(-15f, -2f, 0.2f);
            node.addNeighbor("n1");
        }));

    }

    public void beginCreate() {
        MapNode node = map.getX("root");
        node.createPos.set(node.originPos);
        PlatForm.create(node);


    }

    private void definePlatformType() {
        platformType.add("p1", new PlatFormInfo((PlatFormInfo info) -> {
            info.name = "p1";
            info.blocks.add("b1", new PropInfo((PropInfo prop) -> {
                prop.meshName = "/model/testPlatform3";
            }));

            info.props.add(new PropInfo((PropInfo prop) -> {
                prop.meshName = "/model/tree";
                prop.offset.set(0f, 0f, -1f);
                prop.size.set(1.6f, 1.6f, 1f);
                prop.pos.set(2f, 0f, 0f);
            }));
            info.collidables.add(new PropInfo((PropInfo prop) -> {
                prop.meshName = "/model/thorns";
                prop.pos.set(7f, 0f, 0f);
            }));
        }));
        platformType.add("p2", new PlatFormInfo((PlatFormInfo info) -> {
            info.name = "p2";
            info.blocks.add("b1", new PropInfo((PropInfo prop) -> {
                prop.meshName = "/model/testPlatform1";
            }));
            info.props.add(new PropInfo((PropInfo prop) -> {
                prop.meshName = "/model/tree";
                prop.offset.set(0f, 0f, -1f);
                prop.pos.set(-0.5f, 0, 0);
                prop.size.set(1.2f, 1.2f, 1f);
            }));
            info.props.add(new PropInfo((PropInfo prop) -> {
                prop.meshName = "/model/tree";
                prop.offset.set(0f, 0f, -1f);
                prop.pos.set(0.5f, 0, 0);
                prop.size.set(2f, 2f, 1f);
            }));
        }));
        platformType.add("end", new PlatFormInfo((PlatFormInfo info) -> {
            info.name = "end";
            info.blocks.add("b1", new PropInfo((PropInfo prop) -> {
                prop.meshName = "/model/testPlatform1";
            }));
            info.props.add(new PropInfo((PropInfo prop) -> {
                prop.meshName = "/model/flag";
                prop.offset.set(0f, 0f, 0.2f);
                prop.pos.set(-0.8f, 0, 0);
            }));
        }));
    }

    public ArrayList<MapNode> constructList = new ArrayList<>();

    public void constructPlatFroms() {
        for (int i = 0; i < PlatForm.live.size(); i++) {
            PlatForm platForm = PlatForm.live.get(i);
            platForm.checkNeighbor();
        }
        for (int i = 0; i < constructList.size(); i++) {
            MapNode node = constructList.get(i);
            PlatForm.create(node);
        }
        constructList.clear();
    }

    public void resetScene() {
        while(PlatForm.live.size()>0){
            PlatForm livePlatForm = PlatForm.live.get(0);
            livePlatForm.eliminate();
        }
    
        beginCreate();
    }
}


