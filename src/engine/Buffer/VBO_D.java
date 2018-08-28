package engine.Buffer;


import engine.Interface.InputProperty;

public class VBO_D extends VBO {

    public VBO_D(InputProperty<VBORaw> input) throws Exception {
        super(input);
        generateNewBuffer(data.getX("raw"), data.getX("pointNum"),true);
    }
    
}
