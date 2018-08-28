package engine.UniformFunctions;

import engine.Objs.Obj;

public class u_UseTexture extends UniformFunction {
    public u_UseTexture() {
        name = "u_UseTexture";
    }

    @Override
    public void run(Obj obj, int location, String uniformName) {

        Integer useTexture = obj.raw.get(uniformName);
        if (useTexture == null) {
            useTexture = obj.mesh.raw.get("UseTexture");
            if (useTexture == null)
                useTexture = 0;
        }
        pass_Int_value(useTexture, location);
    }
}
