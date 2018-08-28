package engine.Physics;

import engine.Interface.IForceFunction;
import engine.Interface.IForceAutoTerminated;
import engine.Util.Error;

import java.util.LinkedList;

public class ForceTimer {
    LinkedList<ForceTimer> timers;
    IForceFunction forceFun;
    public int duration;
    
    public ForceTimer(LinkedList<ForceTimer> timers,IForceFunction forceFun, int duration) {//countdown frame
        if (!(forceFun instanceof IForceAutoTerminated))
            Error.fatalError(new Exception(), "forceFun not implement autoTerminated");
        this.forceFun = forceFun;
        this.duration = duration;
        timers.add(this);
        this.timers=timers;
        
    }

    public void count() {
        duration--;
        if (duration == 0) {
            ((IForceAutoTerminated)forceFun).Terminated();
            timers.remove(this);
        }

    }
}
