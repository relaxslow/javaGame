package engine.Util;

import engine.Interface.INeedClean;

import java.util.ArrayList;

public class Pool<T> extends Res implements INeedClean {
    ArrayList<T> store = new ArrayList<>();
  
   public Pool(String name){
       this.name =name;
   }
    public void reclaim(T obj) {
        store.add(obj);
    }

    int capcity = 1000;
    int count = 0;

    public T create(Class<T> aClass)  {

        T result = null;

        if (store.size() == 0) {
            if (count > capcity)
                Error.fatalError(new Exception(),"exceed capcity,can't produce more");
            else {
                try {
                    result = aClass.newInstance();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                count++;
//                ((INeedPoolOrCanvasCreate) result).setCreatFrom(INeedPoolOrCanvasCreate.FROM_CANVAS);

            }
        } else {
            result = store.get(0);
            store.remove(0);
//            ((INeedPoolOrCanvasCreate) result).setCreatFrom(INeedPoolOrCanvasCreate.FROM_POOL);
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