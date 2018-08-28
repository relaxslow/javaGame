package engine.UniformFunctions;

import engine.Objs.Obj;
import org.joml.Vector4f;

import static org.lwjgl.opengl.GL20.glUniform4f;

public class u_Color extends UniformFunction {
    public u_Color() {
        name = "u_Color";
    }

    @Override
    public void run(Obj obj, int location, String uniformName) {
        Vector4f color = obj.raw.get(uniformName);
        glUniform4f(location, color.x, color.y, color.z, color.w);
    }
}
