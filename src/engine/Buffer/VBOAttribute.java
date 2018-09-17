package engine.Buffer;

import engine.Interface.IFun3;
import engine.Util.Raw;

import java.util.Map;

public class VBOAttribute extends Raw {
    public VBOAttribute(String name) {
        super(name);
    }
    //iterate attributes and accumulate the values,stop when name matches
    public <T> int getAccumulateResult(IFun3<String, T> fun) {
        int result = 0;
        for (Map.Entry<String, Object> entry : entrySet()) {
            int part = fun.run(entry.getKey(), (T) entry.getValue());
            if (part != -1)
                result += part;
            else
                break;
        }
        return result;
    }
}