package engine.Objs;

public class Props extends Obj {

    static int propIndex;
    public Props(String meshName) {
       name="props"+propIndex;
        propIndex++;

        mesh = canvas.allRes.getX(meshName);
        camera=canvas.camera;
        attachCustomTexture();
        

       
        
    }
    public void hide(){
        removeFromRenderGroups();
        
    }
    public void show(){
        addToRenderGroups();
    }
    
}
