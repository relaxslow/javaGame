package engine.Objs;

import engine.Interface.ICollidable;
import engine.Interface.InputProperty;
import engine.Util.Raw;



public class Block extends  PhysicObj implements ICollidable {

  public  Block(InputProperty<Raw> input) throws Exception {
       super(input);
   }

    @Override
    public void create(Raw res) throws Exception {
        super.create(res);
        canvas.addToCollideGroup("Blocks",this);
    }
    

  


}
