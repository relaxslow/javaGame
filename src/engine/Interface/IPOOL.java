package engine.Interface;

import engine.Objs.Canvas;

public interface IPOOL {
    void setCreateFrom(String createFrom);

    String getCreateFrom();

    void initFromNew();

    void initFromPool();

    void eliminate();
    
  

}
