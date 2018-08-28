package engine.UniformFunctions;

import engine.Objs.Canvas;
import engine.Objs.Obj;

public class u_ProjectMatrix extends UniformFunction {
    Canvas canvas;

    public u_ProjectMatrix(Canvas canvas) {
        name = "u_ProjectMatrix";
        this.canvas = canvas;

    }


    @Override
    public void run(Obj obj, int location, String name) {

        pass_MatrixUnifrom_value(obj.camera.projectMatrix, location);
    }

}
