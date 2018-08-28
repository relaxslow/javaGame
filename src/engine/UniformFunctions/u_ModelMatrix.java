package engine.UniformFunctions;

import engine.Objs.Obj;

public class u_ModelMatrix extends UniformFunction {

    public u_ModelMatrix() {
        name = "u_ModelMatrix";
    }

    @Override
    public void run(Obj obj, int location, String name) {
        pass_MatrixUnifrom_value(obj.modelMatrix, location);

    }
}
