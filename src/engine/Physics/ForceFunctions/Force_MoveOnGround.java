package engine.Physics.ForceFunctions;

import engine.Input.KeyBoard;
import engine.Input.Mouse;
import engine.Interface.*;
import engine.Objs.PhysicObj;
import engine.Physics.*;
import engine.Util.Constant;
import engine.Util.Debug;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.LinkedList;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_D;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_J;

public class Force_MoveOnGround extends ForceFunction implements IInput, INeedData, INeedChangeData, IGoalTest {
    public static float speed = 4f;
    Vector3f inputSpeed = new Vector3f();

    LinkedList<ICollidable> grounds = new LinkedList<>();

    BVertex goal;
    BVertex left;
    BVertex right;
    BVertex at;//at goal

    Vector3f input_direct = new Vector3f();
    Vector3f left_direct = new Vector3f();
    Vector3f right_direct = new Vector3f();

    Vector3f jump_direct = new Vector3f();
    Vector3f left_up = new Vector3f();
    Vector3f right_up = new Vector3f();

    Vector3f up_direct = new Vector3f();
    Quaternionf posture = new Quaternionf();

    @Override
    public void passInitData(Object data) {
        CollideInfo info = (CollideInfo) data;
        addGround(info.obj2);

        root.update();
        info.face.update();
        left = info.face.p1;
        right = info.face.p0;

        left.sub(root, left_direct);
        right.sub(root, right_direct);
        left_direct.normalize();
        right_direct.normalize();

        //adjust posture;
        up_direct.set(Constant.UP3f);
        adjustPosture(info.face.normal);


    }


    @Override  //change status from one ground to another
    public void change(Object data) {

        CollideInfo info = (CollideInfo) data;
    }

    @Override
    public void apply(IPhysics obj, float interval) {

        input_direct.mul(speed, inputSpeed);
        Vector3f speed = obj.getSpeed();

        speed.add(inputSpeed);

    }

    Vector3f jumpSpeedVec = new Vector3f();
    float jumpspeed = 8f;


    final int leftKey = GLFW_KEY_A;
    final int rightKey = GLFW_KEY_D;
    final int jumpKey = GLFW_KEY_J;

    Vector3f tempUpVec = new Vector3f();

    @Override
    public void input(KeyBoard key, Mouse mouse, Object obj) {
        root.update();
        input_direct.set(Constant.ZERO3f);
        if (key.isPressed(rightKey) && key.isPressed(leftKey)) {
            goal = null;

        } else {
            if (key.isPressed(rightKey)) {
                goal = right;
                if (at != null) {
                    left = at;
                    at = null;
                }
                right.update();
                right.sub(root, right_direct);
                right_direct.normalize();
                input_direct.add(right_direct);


                //change posture
                right_direct.rotateZ(Constant.Radian_90, tempUpVec);
                adjustPosture(tempUpVec);


            } else if (key.isPressed(GLFW_KEY_A)) {
                goal = left;
                if (at != null) {
                    right = at;
                    at = null;
                }
                left.update();
                left.sub(root, left_direct);
                left_direct.normalize();
                input_direct.add(left_direct);

                //change posture
                left_direct.rotateZ(Constant.Radian_Negetive_90, tempUpVec);
                adjustPosture(tempUpVec);

            }
        }

        if (!input_direct.equals(Constant.ZERO3f))
            input_direct.normalize();

        if (key.isPressed(jumpKey)) {
            left_direct.rotateZ(Constant.Radian_Negetive_90, left_up);
            right_direct.rotateZ(Constant.Radian_90, right_up);
            left_up.add(right_up, jump_direct);
            jump_direct.normalize();


            input_direct.add(jump_direct);
            input_direct.normalize();


            ((IPhysics) obj).removeForce(Force_MoveOnGround.class);
            input_direct.mul(jumpspeed, jumpSpeedVec);
            ((IPhysics) obj).addForce(Force_Gravity.class, jumpSpeedVec);
            removeAllGround();

            adjustPosture(Constant.UP3f);


        }


    }

    void adjustPosture(Vector3f dest) {
        up_direct.rotationTo(dest, posture);
        up_direct.rotate(posture);
        hostObj.getOperateMatrix().rotate(posture);
    }

    public void addGround(ICollidable ground) {
        grounds.add(ground);
        hostObj.addCollideExclude(ground);

    }


    public void removeAllGround() {
        for (int i = 0; i < grounds.size(); i++) {
            ICollidable ground = grounds.get(i);
            hostObj.removeCollideExclude(ground);
        }
        grounds.clear();

    }


    ICollideOccur reachGoalFun = new ReachGoalFun();

    @Override
    public void testGoal(CollideInfo info) {
        if (input_direct.equals(Constant.ZERO3f)) return;
        goal.update();
        root.update();
        float l = root.distance(goal);
        float collideTime = l / speed;
        if (info.minCollideTime == -1 || collideTime < info.minCollideTime) {
            info.type = CollideInfo.GOAL;
            info.minCollideTime = collideTime;
            info.collidePoint.set(goal);
            info.currentPos.set(root);
            info.fun = reachGoalFun;

            info.vertex = goal;
        }

    }


    class ReachGoalFun implements ICollideOccur {

        @Override
        public void run(CollideInfo info) {
            Debug.log("reachGoal");
            if (goal == left) {
                left = info.vertex.leftFace.p1;
            } else if (goal == right) {
                right = info.vertex.rightFace.p0;
            }
            at = goal;
            goal = null;

            //change posture at goal point
//            Vector3f n1 = info.vertex.leftFace.normal;
//            Vector3f n2 = info.vertex.rightFace.normal;
//            n1.add(n2, combinedNormal);
//            combinedNormal.normalize();
//
//
//            up_direct.rotationTo(combinedNormal, posture);
//            up_direct.rotate(posture);
//            hostObj.getOperateMatrix().rotate(posture);


        }
    }

    class ReachGoalFun_jumpCliff implements ICollideOccur {

        @Override
        public void run(CollideInfo info) {
            BFace face = null;
            if (goal == left) {
                face = info.vertex.leftFace;
            } else if (goal == right) {
                face = info.vertex.rightFace;
            }
            Vector3f normal = face.normal;
            
        }
    }

    BVertex root;//the charactor's root 

    @Override
    public void setHostObj(PhysicObj obj) {
        super.setHostObj(obj);
        root = ((ICharactor) obj).getRootPoint();
        root.update();

    }
}
