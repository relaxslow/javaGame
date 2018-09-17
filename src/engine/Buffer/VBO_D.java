package engine.Buffer;


import engine.Interface.InputProperty;
import engine.Util.Error;

public class VBO_D extends VBO {

    public VBO_D(InputProperty<VBORaw> input)  {
        super(input);
        generateNewBuffer(data.getX("raw"), data.getX("pointNum"),true);
       
    }
    
}
