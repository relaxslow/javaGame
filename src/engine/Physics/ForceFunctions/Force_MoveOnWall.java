package engine.Physics.ForceFunctions;

import engine.Interface.ICountDown;
import engine.Interface.INeedData;
import engine.Interface.IPhysics;
import engine.Util.Constant;

import java.util.Timer;
import java.util.TimerTask;


public class Force_MoveOnWall extends ForceFunction implements ICountDown, INeedData {


    Timer timer = new Timer();

  
    @Override
    public void startTimer(){
        TimerTask countDown = new CountDown(hostObj,this);
        timer.schedule(countDown, 100);
    }
    @Override
    public void init(Object data) {
        
    }
    @Override
    public void apply(IPhysics obj, float interval) {
        
    }

  
}

class CountDown extends TimerTask {
    IPhysics hostObj;
    Force_MoveOnWall fun;
    CountDown(IPhysics hostObj,Force_MoveOnWall fun) {
        this.hostObj = hostObj;
        this.fun=fun;
    }

    @Override
    public void run() {
        fun.timer.cancel();
        hostObj.removeForce(Force_MoveOnWall.class);
        hostObj.addForce(Force_Gravity.class, Constant.ZERO3f);
    }
}