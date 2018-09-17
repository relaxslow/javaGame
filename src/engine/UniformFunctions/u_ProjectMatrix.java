package engine.UniformFunctions;

import engine.Objs.Canvas;
import engine.Objs.Obj;

public class u_ProjectMatrix extends UniformFunction {
   

    public u_ProjectMatrix() {
        name = "u_ProjectMatrix";

    }


    @Override
    public void run(Obj obj, int location, String name) {

        pass_MatrixUnifrom_value(obj.camera.projectMatrix, location);
    }

}
