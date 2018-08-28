package engine.UniformFunctions;

import engine.Interface.IUniformFunction;
import engine.Objs.Obj;
import engine.Util.Res;
import org.joml.Matrix4f;
import org.lwjgl.system.MemoryStack;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL20.glUniform1i;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;

public abstract class UniformFunction extends Res implements IUniformFunction<Obj> {

    void pass_MatrixUnifrom_value(Matrix4f matrix, int location) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            FloatBuffer fb = stack.mallocFloat(16);
            matrix.get(fb);
            glUniformMatrix4fv(location, false, fb);
        }
    }

    void pass_Int_value(int value, int location) {
        glUniform1i(location, value);
    }
}


