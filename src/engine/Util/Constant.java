package engine.Util;

import org.joml.Vector3f;

public final class Constant {

    final public static float Radian_90 = (float) Math.toRadians(90);
    final public static float DEGREE180 = (float) Math.toRadians(180);
    final public static float Radian_Negetive_90 = (float) Math.toRadians(-90);
    final public static float Cliff=(float)Math.toRadians(135);
    final public static float Wall=(float)Math.toRadians(45);
  
    
    final public static Vector3f ZERO3f = new Vector3f(0f,0f,0f);
    final public static Vector3f UP3f=new Vector3f(0f,1f,0f);
    final public static Vector3f DOWN3f=new Vector3f(0f,-1f,0f);
    final public static Vector3f RIGHT3f=new Vector3f(1f,0f,0f);
    final public static Vector3f LEFT3f=new Vector3f(-1f,0f,0f);
    

}
