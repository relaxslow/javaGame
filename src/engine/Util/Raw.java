package engine.Util;

import engine.Interface.IFun1;
import engine.Interface.IFun1X;
import engine.Interface.IFun2;
import engine.Interface.IFun2X;

import java.util.LinkedHashMap;
import java.util.Map;

public class Raw extends LinkedHashMap<String, Object> {
    public void add(String name, Object obj) throws Exception {
        if (Debug.DEBUG == true) {
            if (containsKey(name))
                throw new Exception("name " + name + " aleady exist!!");
        }
        this.put(name, obj);
    }//set


    public <T> T getX(String name) throws Exception {
        if (containsKey(name))
            return (T) super.get(name);
        else
            throw new Exception("property no define : " + name);

    }

    public <T> T get(String name) {
        return (T) super.get(name);

    }

    public <T> void iterateValueX(IFun1X<T> fun) throws Exception {
        for (Object object : values()) {
            fun.run((T) object);
        }
    }

    public <T> void iterateValue(IFun1<T> fun) {
        for (Object object : values()) {
            fun.run((T) object);
        }
    }

    public <T> void iterateKeyValue(IFun2<String, T> fun) {
        for (Map.Entry<String, Object> entry : entrySet()) {
            fun.run(entry.getKey(), (T) entry.getValue());
        }
    }


    public <T> void iterateKeyValueX(IFun2X<String, T> fun) throws Exception {
        for (Map.Entry<String, Object> entry : entrySet()) {
            fun.run(entry.getKey(), (T) entry.getValue());
        }
    }

    public void iterateKey(IFun1<String> fun) {
        for (String key : keySet()) {
            fun.run(key);
        }
    }

    public void iterateKeyX(IFun1X<String> fun) throws Exception {
        for (String key : keySet()) {
            fun.run(key);
        }
    }

}