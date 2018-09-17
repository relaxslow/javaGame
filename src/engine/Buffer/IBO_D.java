package engine.Buffer;


import engine.Interface.InputProperty;
import engine.Util.Raw;

public class IBO_D extends IBO {

    public IBO_D(InputProperty<Raw> input)  {
        super(input);
        generateNewBuffer(raw.getX("raw"),true);
    }


}
