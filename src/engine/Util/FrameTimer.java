package engine.Util;

import engine.Interface.IFrameTask;
import engine.Objs.Canvas;

public class FrameTimer {
    static int index=0;
    public String name;
    Canvas canvas;
    public FrameTimer(Canvas canvas, String name){
        this.name=name+index;
        index++;
        this.canvas=canvas;
      
    }
    float  count;
    IFrameTask task;
    public void schedule(IFrameTask task,float delay){
        count=delay;
        this.task=task;
        canvas.addFrameTimer(this);
    }
    public void cancel(){
        canvas.removeFrameTimer(this);
    }
    public void countDown(float interval){
        count-=interval;
        if(count<=0){
//            canvas.removeFrameTimer(this);
            task.run();
        }
        
    }

    public void restart(int delay) {
        count=delay;
    }
}
