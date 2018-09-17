package engine.Util;

import engine.Interface.INeedClean;
import engine.Interface.IPOOL;
import engine.Objs.Canvas;

import java.util.ArrayList;

public class Pool<T> extends Res implements INeedClean {
    ArrayList<T> store = new ArrayList<>();
    public static String CREATE_FROM_POOL = "create from store";
    public static String CREATE_FROM_NEW = "create from new";

//    public Canvas canvas;
    public Pool(String name) {
        this.name = name;
    }

    public void reclaim(T obj) {
        store.add(obj);
    }

    int capcity = 1000;
    int count = 0;

    public T create(Class<T> aClass) {

        T result = null;

        if (store.size() == 0) {
            if (count > capcity)
                Error.fatalError(new Exception(), "exceed capcity,can't produce more");
            else {
                try {
                    result = aClass.newInstance();
                    if (result instanceof IPOOL)
                        ((IPOOL) result).setCreateFrom(CREATE_FROM_NEW);
                } catch (Exception e) {
                    Error.fatalError(e, "error create new instance from pool" + name);
                }
                count++;
            }
        } else {
            result = store.get(0);
            if (result instanceof IPOOL)
                ((IPOOL) result).setCreateFrom(CREATE_FROM_POOL);
            store.remove(0);
        }
        return result;
    }

    public void clean() {
        for (int i = 0; i < store.size(); i++) {
            T obj = store.get(i);
            if (obj instanceof INeedClean) {
                ((INeedClean) obj).clean();
            }

        }
    }

}