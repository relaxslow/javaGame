package engine.UniformFunctions;

import engine.Objs.Obj;
import org.joml.Matrix4f;

public class u_MVPMatrix extends UniformFunction {

//    private Matrix4f MVPMatrix = new Matrix4f();

    public u_MVPMatrix() {
        name = "u_MVPMatrix";
    }

    @Override
    public void run(Obj obj, int location, String name) {
        Matrix4f MVPMatrix=obj.getMVPMatrix();
        pass_MatrixUnifrom_value(MVPMatrix, location);
    }


}
