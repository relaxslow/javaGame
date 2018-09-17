package engine.Objs.scenes;

import engine.Interface.INeedTestBorder;
import engine.Interface.IPOOL;
import engine.Interface.IUpdate;
import engine.Objs.Block;
import engine.Objs.CollidableObj;
import engine.Objs.Obj;
import engine.Util.Pool;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.ArrayList;

public class PlatForm extends Scene implements IPOOL, INeedTestBorder {
    public static SceneManager sceneManager;
    static int platformIndex = 0;
    public static Pool<PlatForm> pool = new Pool<>("platform_pool");

    static ArrayList<PlatForm> live = new ArrayList<>();
    MapNode node;
    
    Vector3f toNeighborVec = new Vector3f();
    void checkNeighbor() {
        for (int i = 0; i < node.neighbors.size(); i++) {
            MapNode neighbor = node.neighbors.get(i);
            if(neighbor.platForm!=null)continue;

            neighbor.originPos.sub(node.originPos, toNeighborVec);
            modelMatrix.getTranslation(currentPos);
            currentPos.add(toNeighborVec, neighbor.createPos);
            if (neighbor.shouldInScreen())
                sceneManager.constructList.add(neighbor);
        }
    }

    void attachNode(MapNode node) {
        this.node = node;
        node.platForm=this;
    }

    void detachNode() {
        node.platForm=null;
        this.node = null;
        
    }

    public static PlatForm create(MapNode node) {
        PlatForm newObj = pool.create(PlatForm.class);
        newObj.attachNode(node);

        if (newObj.getCreateFrom() == Pool.CREATE_FROM_NEW) {
            newObj.initFromNew();
        } else if (newObj.getCreateFrom() == Pool.CREATE_FROM_POOL) {
            newObj.initFromPool();
        }
        newObj.calculateMatrix();
        live.add(newObj);
        return newObj;
    }

    @Override
    public void initFromNew() {
        name = "platForm" + platformIndex;
        platformIndex++;

        attachParent(canvas);
        originMesh = canvas.allRes.get("NullMesh");

        camera = canvas.camera;
        matrix.scale(0.4f, 0.4f, 0.4f);
        operateMatrix.translate(node.createPos);

        updateCallBack = (IUpdate<Scene>) (float interval, Scene scene) -> {
          

            scene.offset.set(scene.getSpeed()).mul(interval);
            scene.operateMatrix.translate(scene.offset);
            
//            checkNeighbor();
        };


        canvas.updateGroup.add(name, this);
        canvas.borderTestGroup.add(name, this);
        addToRenderGroups();

        //------------------------------------------------
        createBlocks();
        createProps();
        createCollidables();

    }

    ArrayList<Block> blocks = new ArrayList<>();
    ArrayList<Obj> props = new ArrayList<>();
    ArrayList<CollidableObj> collidables = new ArrayList<>();

    void createBlocks() {
        node.platFormInfo.blocks.iterateValue((PropInfo info)->{
            Block block = new Block();
            block.name = "platformBlock" + platformIndex;
            platformIndex++;
            block.attachParent(this);
            block.setMeshInfo(info.meshName);
            block.camera = canvas.camera;

            block.attachCustomTexture();
            block.addToRenderGroups();
            block.addToCollideGroup("Blocks");

            block.matrix.translate(info.offset);
            block.matrix.scale(info.size);
            block.operateMatrix.translate(info.pos);
//            block.calculateMatrix();

            blocks.add(block);
        });
    
    }

    void createProps() {
        for (int i = 0; i < node.platFormInfo.props.size(); i++) {
            PropInfo info = node.platFormInfo.props.get(i);
            Obj prop = new Obj(info.meshName);
            prop.attachParent(this);
            prop.addToRenderGroups();

            prop.matrix.translate(info.offset);
            prop.matrix.scale(info.size);
            prop.operateMatrix.translate(info.pos);

//            prop.calculateMatrix();

            props.add(prop);
        }
    }

    void createCollidables() {
        for (int i = 0; i < node.platFormInfo.collidables.size(); i++) {
            PropInfo info = node.platFormInfo.collidables.get(i);
            CollidableObj collidable = new CollidableObj(info.meshName);
            collidable.attachParent(this);
            collidable.addToRenderGroups();

            collidable.matrix.translate(info.offset);
            collidable.matrix.scale(info.size);
            collidable.operateMatrix.translate(info.pos);

//            collidable.calculateMatrix();
            collidables.add(collidable);
        }
    }

    @Override
    public void initFromPool() {
        parent.addChild(this);
        addToRenderGroups();
        canvas.updateGroup.add(this.name, this);
        canvas.borderTestGroup.add(this.name, this);
        operateMatrix.translate(node.createPos);

        createBlocks();
        createProps();
        createCollidables();

    }


    @Override
    public void eliminate() {
        parent.removeChild(this);
        removeFromRenderGroups();
        canvas.updateGroup.remove(this.name);
        canvas.borderTestGroup.remove(this.name);
        modelMatrix.identity();
        operateMatrix.identity();


        removeBlocks();
        removeProps();
        removeCollidables();
        detachNode();
        live.remove(this);
        pool.reclaim(this);
    }

    private void removeCollidables() {
        for (int i = 0; i < collidables.size(); i++) {
            CollidableObj collidable = collidables.get(i);
            removeChild(collidable);
            collidable.removeFromRenderGroups();
            collidable.removeFromCollideGroup("Blocks");

        }
        collidables.clear();
    }

    private void removeProps() {
        for (int i = 0; i < props.size(); i++) {
            Obj prop = props.get(i);
            removeChild(prop);
            prop.removeFromRenderGroups();
        }
        props.clear();
    }

    private void removeBlocks() {
        for (int i = 0; i < blocks.size(); i++) {
            Block block = blocks.get(i);
            removeChild(block);
            block.removeFromRenderGroups();
            block.removeFromCollideGroup("Blocks");

        }
        blocks.clear();
    }

    Vector3f currentPos = new Vector3f();

    @Override
    public boolean isOutOfBorder() {
        Matrix4f modelMatrix = getModelMatrix();
        modelMatrix.getTranslation(currentPos);
        if (currentPos.x < -canvas.eliminateBorder.wid
                || currentPos.x > canvas.eliminateBorder.wid
                || currentPos.y < -canvas.eliminateBorder.hei
                || currentPos.y > canvas.eliminateBorder.wid)
            return true;
        return false;
    }

  

}
