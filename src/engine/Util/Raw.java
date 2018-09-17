package engine.Util;

import engine.Interface.*;

import java.util.*;

public class Raw extends LinkedHashMap<String, Object> {
    String name;
    boolean operateOn = false;

    public Raw(String name) {
        this.name = name;
    }

    HashMap<String, Object> createList = new HashMap<>();

    public void add(String name, Object obj) {
        if (Debug.DEBUG == true) {
            if (containsKey(name))
                Error.fatalError(new Exception("name " + name + " aleady exist!!"), null);
        }
        if (operateOn) {
            createList.put(name, obj);
        } else {
            this.put(name, obj);
        }
    }//set

    public void remove(String key) {
        if (operateOn) {
            deleteList.add(key);
        } else {
            super.remove(key);
        }
    }

    public <T> T getX(String name) {
        if (containsKey(name))
            return (T) super.get(name);
        else {
            Error.fatalError(new Exception("property no define : " + name), null);
            return null;
        }

    }

    public <T> T get(String name) {
        return (T) super.get(name);

    }

    ArrayList<String> deleteList = new ArrayList<>();


    void safeDeleteAndAdd() {
        for (int i = 0; i < deleteList.size(); i++) {
            String key = deleteList.get(i);
            remove(key);
        }
        deleteList.clear();
        for(Map.Entry<String,Object> entry:createList.entrySet()){
            add(entry.getKey(),entry.getValue());
        }
        createList.clear();
    }

    public <T> void iterateValueX(IFun1X<T> fun) {
        operateOn = true;
        for (Object object : values()) {
            fun.run((T) object);
        }
        operateOn = false;
        safeDeleteAndAdd();

    }

    public <T> void iterateValue(IFun1<T> fun) {
        operateOn = true;
        for (Object object : values()) {
            fun.run((T) object);
        }
        operateOn = false;
        safeDeleteAndAdd();

    }

    public <T> void iterateKeyValue(IFun2<String, T> fun) {
        operateOn = true;
        for (Map.Entry<String, Object> entry : entrySet()) {
            fun.run(entry.getKey(), (T) entry.getValue());
        }
        operateOn = false;
        safeDeleteAndAdd();

    }

    public <T> void iterateValueSafeDelete(IFun4<T> fun) {

        Iterator<Map.Entry<String, Object>> it = entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Object> pair = it.next();
            fun.run((T) pair.getValue(), it);
//            it.remove(); // avoids a ConcurrentModificationException
        }

    }

    public <T> void iterateKeyValueX(IFun2X<String, T> fun) {
        operateOn = true;
        for (Map.Entry<String, Object> entry : entrySet()) {
            fun.run(entry.getKey(), (T) entry.getValue());
        }
        operateOn = false;
        safeDeleteAndAdd();

    }

//    public void iterateKey(IFun1<String> fun) {
//        operateOn=true;
//        for (String key : keySet()) {
//            fun.run(key);
//        }
//        operateOn=false;
//    }

    public void iterateKeyX(IFun1X<String> fun) {
        operateOn = true;
        for (String key : keySet()) {
            fun.run(key);
        }
        operateOn = false;
        safeDeleteAndAdd();

    }


}