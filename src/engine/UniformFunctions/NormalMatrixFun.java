package engine.UniformFunctions;

import engine.Objs.Obj;

public class NormalMatrixFun extends UniformFunction {


    @Override
    public void run(Obj obj, int location, String name) {
        obj.modelMatrix.invert(obj.normalMatrix);
        obj.normalMatrix.transpose();
//        gl.uniformMatrix4fv(this.location, false, mesh.normalMatrix.elements);
    }
}
