package engine.Objs;

import engine.Interface.ICollidable;
import engine.Interface.IPOOL;
import engine.Interface.InputProperty;
import engine.Objs.scenes.PlatForm;
import engine.Objs.scenes.PlatFormInfo;
import engine.Objs.scenes.PropInfo;
import engine.Physics.BoundingBox;
import engine.Util.Pool;
import engine.Util.Raw;



public class Block extends CollidableObj implements ICollidable {
    public Block() {
        
    }

  public  Block(InputProperty<Raw> input)  {
       super(input);
   }

    @Override
    public void create()  {
        super.create();
        canvas.addToCollideGroup("Blocks",this);
    }

    public void setMeshInfo(String meshName) {
        mesh = canvas.allRes.getX(meshName);
        boundingMesh = canvas.allRes.getX(meshName + "_BoundingBox");
        boundingBox= new BoundingBox(boundingMesh,this);

    }


//    PropInfo propInfo;
//    static int blockIndex = 0;
//    public static Pool<Block> pool = new Pool<>("block_pool");
//    public static Block create(PropInfo info) {
//        Block newObj = pool.create(Block.class);
//        newObj.propInfo = info;
//        if (newObj.getCreateFrom() == Pool.CREATE_FROM_NEW) {
//            newObj.initFromNew();
//        } else if (newObj.getCreateFrom() == Pool.CREATE_FROM_POOL) {
//            newObj.initFromPool();
//        }
//        newObj.calculateMatrix();
//        return newObj;
//    }
//    
//    @Override
//    public void initFromNew() {
//        
//    }
//
//    @Override
//    public void initFromPool() {
//
//    }
//
//    @Override
//    public void eliminate() {
//
//    }
}
