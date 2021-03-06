package engine.Objs;

import engine.Input.KeyBoard;
import engine.Input.Mouse;
import engine.Interface.*;
import engine.Objs.Helper.Border;
import engine.Objs.UIObjs.CharObj;
import engine.Objs.scenes.*;
import engine.Physics.BFace;
import engine.Physics.BVertex;
import engine.Physics.CollideInfo;
import engine.Programs.Program;
import engine.UniformFunctions.UniformFunction;
import engine.Util.*;
import engine.Util.Error;
import engine.View.Camera;
import engine.View.CameraUI;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWFramebufferSizeCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import java.util.ArrayList;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Canvas implements IParent, IHasName {
    private GLFWErrorCallback errorCallback;

    private GLFWFramebufferSizeCallback resizeCallBack;

    private long window = 0;
    private final String title = "my game";
    public int width;
    public int height;

    public Mouse mouse;
    public KeyBoard keyBoard;

    public Canvas(int width, int height) {
        this.width = width;
        this.height = height;
        create();
    }

    private void create() {
        System.out.println("LWJGL Version " + Version.getVersion() + " is working.");
        errorCallback = GLFWErrorCallback.createPrint();
        glfwSetErrorCallback(errorCallback);
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }
        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GL_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GL_TRUE); // the window will be resizable
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);

        window = glfwCreateWindow(width, height, title, NULL, NULL);
        if (window == NULL) {
            glfwTerminate();
            throw new RuntimeException("Failed to create the GLFW window");
        }

        mouse = new Mouse(window);

        keyBoard = new KeyBoard(window);

        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        // Center our window
        glfwSetWindowPos(
                window,
                (vidmode.width() - width) / 2,
                (vidmode.height() - height) / 2
        );

        glfwMakeContextCurrent(window);

        glfwSwapInterval(1);//Vsync
        glfwShowWindow(window);


        initGl();


        resizeCallBack = new ResizeCallBack(this);
        glfwSetFramebufferSizeCallback(window, resizeCallBack);


        addInfrastructure();


    }

    public SceneManager sceneManager = new SceneManager();

    void addInfrastructure() {
        Res.canvas = this;
        Obj.canvas = this;
        UniformFunction.canvas = this;
        Camera.canvas = this;
        addRes(this);
        addRes(CharObj.pool);//contain gpu res,need release
        addRes(new CloudGenerator(this));
        addRes(PlatForm.pool);

        PlatForm.sceneManager = sceneManager;
        PlatFormInfo.sceneManager = sceneManager;
        MapNode.sceneManager = sceneManager;
        MapNode.canvas = this;
        sceneManager.init();
    }

    private void initGl() {
        GL.createCapabilities();
        glEnable(GL_DEPTH_TEST);
//        glEnable(GL_POINT_SMOOTH);
        glClearColor(0.3f, 0.3f, 0.3f, 1.0f);
        glPointSize(10f);

        if (!Debug.wireFrame) {
            glEnable(GL_BLEND);
            glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        }


    }

    public Raw allRes = new Raw("all res in canvas");

    public void addRes(Object res) {
        if (res instanceof INeedCreate)
            ((INeedCreate) res).create();

        allRes.add(((IHasName) res).getName(), res);

    }

    void cleanAllResources() {
        for (Object res : allRes.values()) {
            if (res instanceof INeedClean)
                ((INeedClean) res).clean();
        }
    }


    public Raw renderGroups_Normal = new Raw("render group: normal in canvas");
    public Raw renderGroup_Origin = new Raw("render group: origin in canvas");
    public Raw renderGroup_BoundingBox = new Raw("render group: boundingbox in canvas");
    public Raw renderGroup_TrackLine = new Raw("render group: trackLine in canvas");

    private void render() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        renderNormal();
        renderOrigin();
        renderBoundingBox();
        renderTrackLines();
    }

    void renderTrackLines() {
        renderGroup_TrackLine.iterateKeyValue((String programName, Raw group) -> {
            Program program = allRes.get(programName);
            glUseProgram(program.id);
            glClear(GL_DEPTH_BUFFER_BIT);
            group.iterateValue((IRenderable obj) -> {
                obj.render();

            });

        });
    }

    void renderNormal() {
        renderGroups_Normal.iterateKeyValue((String programName, Raw group) -> {
            Program program = allRes.get(programName);
            glUseProgram(program.id);
            if (programName.equals("textProgram"))
                glClear(GL_DEPTH_BUFFER_BIT);
            group.iterateValue((IRenderable obj) -> {
                obj.render();

            });
        });
    }

    void renderOrigin() {
        renderGroup_Origin.iterateKeyValue((String programName, Raw group) -> {
            Program program = allRes.get(programName);
            glClear(GL_DEPTH_BUFFER_BIT);
            glUseProgram(program.id);
            group.iterateValue((IRenderable obj) -> {
                obj.renderOrigin();

            });
        });
    }

    void renderBoundingBox() {
        renderGroup_BoundingBox.iterateKeyValue((String programName, Raw group) -> {
            Program program = allRes.get(programName);
            glClear(GL_DEPTH_BUFFER_BIT);
            glUseProgram(program.id);
            group.iterateValue((IRenderBoundingBox obj) -> {
                obj.renderBoundingBox();

            });
        });
    }

    private void applyForcesToAll(float interval) {

        for (Object value : childs.values()) {
            if (value instanceof IPhysics) {
                ((IPhysics) value).getSpeed().set(0f, 0f, 0f);
                ((IPhysics) value).applyForces(interval);//input,gravity...
            }
        }


    }


    private void calculateAllChildsMatrix(IParent root) {
        Raw childs = root.getChilds();
        for (Object value : childs.values()) {
            if (value instanceof IChild) {
                ((IChild) value).calculateMatrix();
            }
            if (value instanceof IParent)
                calculateAllChildsMatrix((IParent) value);
        }
    }

    public Raw updateGroup = new Raw("update group in canvas");

    void updateAll(float interval) {
        updateGroup.iterateValue((ICanUpdate canUpdate) -> {
            canUpdate.getUpdateFun().update(interval, canUpdate);
        });
        calculateAllChildsMatrix(this);//must place after update and before render
    }

    void cleanFromRoot(IParent root) {
        Raw childs = root.getChilds();
        for (Object value : childs.values()) {
            if (value instanceof INeedClean) {
                ((INeedClean) value).clean();
            }
            if (value instanceof IParent)
                cleanFromRoot((IParent) value);
        }


    }

    Raw inputGroup = new Raw("init group in canvas");

    void input() {

        inputGroup.iterateValue((ICanInput canInput) -> {
            IInput input = canInput.getInputCallBack();
            if (input != null)
                input.input(keyBoard, mouse, canInput);
        });

    }


    private Timer timer = new Timer();


    public static final int UPS = 30;
    public static final int FPS = 30;

    public void loop() {
        float elapsed;
        float accumulator = 0f;
        float updateInterval = 1f / UPS;
        float frameInterval = 1f / FPS; // time per frame should take
        timer.init();
        while (!glfwWindowShouldClose(window)) {
            double time = timer.getTime();
            elapsed = (float) (time - timer.lastTimeStamp);
            timer.lastTimeStamp = time;

            accumulator += elapsed;

            mouse.input();

            while (accumulator >= updateInterval) {
                camera.update(updateInterval);
                UICamera.update(updateInterval);
                updateWithCollide(updateInterval);
                accumulator -= updateInterval;
            }

//            float alpha = accumulator / updateInterval;
//            render(alpha);
            render();
            glfwSwapBuffers(window);
            glfwPollEvents();


            double endTime = timer.lastTimeStamp + frameInterval;

            while (timer.getTime() < endTime) {//time is not up
                Thread.yield();
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    Error.fatalError(e, "fatal error in loop");
                }
            }

        }

    }

    private void updateWithCollide(float interval) {

        float remainTime = interval;

        while (remainTime > 0) {
            sceneManager.constructPlatFroms();
            runAllFrameTimer(interval);
            input();//change speed
            applyForcesToAll(interval);//change speed

            collideInfo.reset();
            borderTestAll();
            collideAll();//minCollideTime Changed
            goalTestAll();
            displayTrackLines();

            float collideTime = collideInfo.minCollideTime;
//            Debug.log(String.valueOf(collideTime));
            if (collideTime >= 0 && collideTime < remainTime) {//collide occur in this update
                updateAll(collideTime);
                collideInfo.fun.run(collideInfo);
            } else {
                collideTime = remainTime;
                updateAll(collideTime);
            }
            if (collideInfo.collidePass == true)
                break;
            remainTime -= collideTime;

        }

    }


    LineSegment3D trackVertex;
    LineSegment3D trackFace;

    public void setFaceTrackLine(String name) {
        trackFace = (LineSegment3D) searchFromRoot(name, this);
    }

    public void setVertexTrackLine(String name) {
        trackVertex = (LineSegment3D) searchFromRoot(name, this);
    }


    private void displayTrackLines() {
        if (collideInfo.type == CollideInfo.GOAL) {
            trackVertex.changeLine(collideInfo.currentPos, collideInfo.collidePoint);
            trackFace.changeLine(Constant.ZERO3f, Constant.ZERO3f);

        } else if (collideInfo.type == CollideInfo.NORMAL) {
            if (collideInfo.vertex != null) {
                trackVertex.changeLine(collideInfo.vertex, collideInfo.collidePoint);
                trackFace.changeLine(collideInfo.collideSurfaceP0, collideInfo.collideSurfaceP1);
            }

        }


    }


    public void clean() {
        cleanFromRoot(this);
        cleanAllResources();
        trackVertex.clean();
        trackFace.clean();

        glfwDestroyWindow(window);
        keyBoard.clean();
        mouse.clean();
        resizeCallBack.free();
        glfwTerminate();
        errorCallback.free();
    }

    public CameraUI UICamera;
    public Camera camera;
    private Matrix4f parentMatrix = new Matrix4f();


    Raw childs = new Raw("childs in canvas");

    @Override
    public void addChild(IChild obj) {
        childs.add(obj.getName(), obj);
    }

    @Override
    public void removeChild(IChild obj) {
        childs.remove(obj.getName());
    }


    public <T> T getChildX(String name) {
        T child = childs.get(name);
        if (child == null)
            Error.fatalError(new Exception("can't find child"), null);

        return child;
    }

    @Override
    public Raw getChilds() {
        return childs;
    }

    @Override
    public Matrix4f getParentMatrix() {
        return parentMatrix;
    }

    @Override
    public IParent getParent() {
        return null;
    }


    @Override
    public String getName() {
        return "canvas";
    }


    Object searchFromRoot(String name, IParent root) {
        Raw childs = root.getChilds();
        Object result;
        for (Object value : childs.values()) {
            if (value instanceof IChild) {
                String objName = ((IChild) value).getName();
                if (objName.equals(name)) {
                    return value;
                }
            }

            if (value instanceof IParent) {
                result = searchFromRoot(name, (IParent) value);
                if (result != null)
                    return result;
            }


        }
        return null;
    }

    public void attachParent(IChild obj) {
        String parentName = obj.getParentName();
        if (parentName.equals("canvas")) {
            addChild(obj);
            obj.setParent(this);
        } else {

            IParent parent = searchParentFromRootByName(this, parentName);
            parent.addChild(obj);
            obj.setParent(parent);
        }
    }

    public void createObj(IChild obj) {
        attachParent(obj);

        if (obj instanceof INeedCreate) {
            ((INeedCreate) obj).create();
        }
        if (obj instanceof ICanInput) {
            if (((ICanInput) obj).getInputCallBack() != null)
                inputGroup.add(obj.getName(), obj);

        }
        if (obj instanceof ICanUpdate) {
            if (((ICanUpdate) obj).getUpdateFun() != null) {
                updateGroup.add(obj.getName(), obj);
            }
        }
        if (obj instanceof INeedTestBorder) {
            borderTestGroup.add(obj.getName(), obj);
        }
        if (obj instanceof IRenderable)
            ((IRenderable) obj).addToRenderGroups();

    }


    private IParent searchParentFromRootByName(IParent parent, String name) {
        IParent result = null;
        Raw childs = parent.getChilds();
        if (childs.containsKey(name)) {
            Object obj = childs.get(name);
            if (obj instanceof IParent)
                result = (IParent) obj;
        } else {
            for (Object value : childs.values()) {
                if (value instanceof IParent)
                    result = searchParentFromRootByName((IParent) value, name);
            }
        }
        return result;
    }

    public void setUICamera(String name) {
        UICamera = allRes.get(name);
        if (UICamera.getInputCallBack() != null)
            inputGroup.add(name, UICamera);
    }

    public void setSceneCamera(String name) {
        camera = allRes.get(name);
        if (camera.getInputCallBack() != null)
            inputGroup.add(name, camera);
    }

    public Raw collideGroups = new Raw("collideGroups in canvas");


    public void addCollideGroup(String name) {
        collideGroups.add(name, new Raw("collideGroup:" + name));
    }

    public void addToCollideGroup(String name, ICollidable obj) {
        Raw collideGroup = collideGroups.getX(name);
        collideGroup.add(obj.getName(), obj);
    }

    public void removeFromCollideGroup(String name, ICollidable obj) {
        Raw collideGroup = collideGroups.getX(name);
        collideGroup.remove(obj.getName());
    }

    Raw collidePossibilities = new Raw("collide possibilities group");

    public void allowCollide(String group1Name, String group2Name, ICollideOccur fun) {
        Raw possibility = new Raw("collide Possibilitie:" + group1Name + "|" + group2Name);
        possibility.add("collideOccurFun", fun);
        possibility.add("group1", collideGroups.get(group1Name));
        possibility.add("group2", collideGroups.get(group2Name));

        collidePossibilities.add(group1Name + "-" + group2Name, possibility);
    }

    void collideAll() {

//        Debug.log("-----new loop----");
        collidePossibilities.iterateValue((Raw certainGroup) -> {
            Raw group1 = certainGroup.get("group1");
            Raw group2 = certainGroup.get("group2");
            ICollideOccur fun = certainGroup.get("collideOccurFun");
            testCollideGroups(group1, group2, fun);
        });
    }

    private void testCollideGroups(Raw group1, Raw group2, ICollideOccur fun) {
        group1.iterateValue((ICollidable obj1) -> {
            group2.iterateValue((ICollidable obj2) -> {
                calculateGlobalSpeedAndRelateVel(obj1, obj2);
                Vector3f relateVel = obj1.getRelateVel();
                if (!(relateVel.equals(Constant.ZERO3f))) {
                    testCollide(obj1, obj2, fun);
//                    testCollide(obj2, obj1, fun);
                }


            });
        });
    }

    Vector3f obj1RelateVelDirect = new Vector3f();
    Vector3f obj2RelateVelDirect = new Vector3f();

    private void calculateGlobalSpeedAndRelateVel(ICollidable obj1, ICollidable obj2) {
        Vector3f s1 = obj1.getGlobalSpeed();
        Vector3f s2 = obj2.getGlobalSpeed();
        Vector3f obj1RelateVel = obj1.getRelateVel();

        s1.sub(s2, obj1RelateVel);
        if (obj1RelateVel.equals(Constant.ZERO3f)) {
            obj1RelateVelDirect.set(Constant.ZERO3f);
            obj2RelateVelDirect.set(Constant.ZERO3f);
        } else {
            obj2.getRelateVel().set(obj1RelateVel).mul(-1);
            obj1RelateVel.normalize(obj1RelateVelDirect);
            obj2RelateVelDirect.set(obj1RelateVelDirect).mul(-1);
        }

        Vector3f a1 = obj1.getGlobalAccelerate();
        Vector3f a2 = obj2.getGlobalAccelerate();
        Vector3f relateAccelerate = obj1.getRelateAccelerate();
        a1.sub(a2, relateAccelerate);

        if (relateAccelerate.equals(Constant.ZERO3f)) {
            obj2.getRelateAccelerate().set(Constant.ZERO3f);
        } else {
            float project = relateAccelerate.dot(obj1RelateVelDirect);
            obj1RelateVelDirect.mul(project, relateAccelerate);
            obj2.getRelateAccelerate().set(relateAccelerate).mul(-1);
        }


    }

    CollideInfo collideInfo = new CollideInfo();

    //gether info instead of update each vertex on bounding box could reduce calculation
    public void testCollide(ICollidable selfObj, ICollidable otherObj, ICollideOccur fun) {
        Raw collideExclude = selfObj.getCollideExclude();
        if (collideExclude.containsKey(otherObj.getName()))
            return;
        ArrayList<BVertex> selfPoints = selfObj.getBoundingBox().points;
        ArrayList<BFace> otherFaces = otherObj.getBoundingBox().faces;
        for (int i = 0; i < selfPoints.size(); i++) {
            collideVertex = selfPoints.get(i);
            if (!collideVertex.collideable) continue;
            collideVertex.update();
            for (int j = 0; j < otherFaces.size(); j++) {
                collideFace = otherFaces.get(j);
                collideFace.update();
                testVertexCollideFace(collideVertex, collideFace, fun);
            }
        }


    }

    BVertex collideVertex;
    BFace collideFace;


    Vector3f f2v = new Vector3f();//face point -> vertex 
    Vector3f perpendicular = new Vector3f();
    Vector3f intersectPoint = new Vector3f();
    Vector3f relateVelDirect = new Vector3f();
    Vector3f cPoint = new Vector3f();
    Vector3f pointOffset = new Vector3f();
    Vector3f cFaceP1 = new Vector3f();
    Vector3f cFaceP2 = new Vector3f();
    Vector3f faceOffset = new Vector3f();

    Vector3f relateAccel = new Vector3f();
    Vector3f relateVel = new Vector3f();

    private void testVertexCollideFace(BVertex vertex, BFace face, ICollideOccur fun) {
        //test if vertex is facing the face
        vertex.leftFace.update();
        vertex.rightFace.update();

        float facing1 = vertex.leftFace.normal.dot(face.normal);
        float facing2 = vertex.rightFace.normal.dot(face.normal);
        if (facing1 >= 0 && facing2 >= 0) return;


        //ignore the surface which is back to vertex;
        f2v = vertex.sub(face.p0, f2v);
        float dist = f2v.dot(face.normal);
        if (dist <= 0) {
            return;
        }

        //get intersectPoint
        face.normal.mul(dist, perpendicular);
        float angle = face.belongTo.getRelateVel().angle(perpendicular);
        if (angle >= Constant.Radian_90 || angle <= Constant.Radian_Negetive_90) return;

        float l = dist / (float) Math.cos(angle);
        relateVel.set(vertex.belongTo.getRelateVel());
        relateVel.normalize(relateVelDirect);
        vertex.add(relateVelDirect, intersectPoint);

        //get collideTime
        float collideTime = -1;
        relateAccel.set(vertex.belongTo.getRelateAccelerate());
        float v = relateVel.length();
        float a = relateAccel.length();
        if (a == 0) {//uniform speed
            collideTime = l / v;
        } else {//accelerate movement
            collideTime = (-v + (float) Math.sqrt(v * v + 2 * a * l)) / a;

        }

        //get collide position
        face.belongTo.getGlobalSpeed().mul(collideTime, faceOffset);
        face.p0.add(faceOffset, cFaceP1);
        face.p1.add(faceOffset, cFaceP2);

        vertex.belongTo.getGlobalSpeed().mul(collideTime, pointOffset);
        vertex.add(pointOffset, cPoint);

        //if(vertec in face when colliding) then record to collideInfo
        // test all the collide and find the min collide time
        boolean isInFace = pointInFace(cPoint, cFaceP1, cFaceP2);
        if (isInFace) {
            CollideInfo info = collideInfo;
            if (info.minCollideTime == -1 || collideTime < info.minCollideTime) {
//                Debug.log(vertex.belongTo.getName() + " vertex" + vertex.indice + " collide with " + face.belongTo.getName() + " face" + face.index);
                info.type = CollideInfo.NORMAL;
                info.minCollideTime = collideTime;
                info.collidePoint.set(cPoint);
                Tools.roundFloatVec(cFaceP1, 6, info.collideSurfaceP0);
                Tools.roundFloatVec(cFaceP2, 6, info.collideSurfaceP1);
                info.intersectPoint.set(intersectPoint);
                info.obj1 = vertex.belongTo;
                info.obj2 = face.belongTo;
                info.fun = fun;
                info.vertex = vertex;
                info.face = face;

                info.collideAtSameTime.clear();
                info.collideAtSameTime.add(vertex);
            } else if (info.minCollideTime == -1 || collideTime == info.minCollideTime) {
                info.collideAtSameTime.add(vertex);
            }
        }

    }


    Vector3f pf1 = new Vector3f();//from point -> face point 1
    Vector3f pf2 = new Vector3f();//from point -> face point 2
    Vector3f f1f2 = new Vector3f();//from face point 1 -> face point 2

    boolean pointInFace(Vector3f point, Vector3f faceP1, Vector3f faceP2) {
        faceP1.sub(point, pf1);
        faceP2.sub(point, pf2);
        faceP1.sub(faceP2, f1f2);
        float d1 = pf1.dot(f1f2);
        float d2 = pf2.dot(f1f2);
        float inSuface = d1 * d2;
        if (inSuface < 0) return true;
        return false;
    }

    public Player player;
    Raw goalTestGroup = new Raw("goal Test Group");

    void goalTestAll() {
        goalTestGroup.iterateValue((INeedGoalTest obj) -> {
            obj.getGoalTestFun().testGoal(collideInfo);
        });
    }

    public Raw borderTestGroup = new Raw("board test group");

    ArrayList<String> deleteIndexs = new ArrayList<>();

    private void borderTestAll() {

        borderTestGroup.iterateKeyValue((String key, INeedTestBorder obj) -> {
            if (obj.isOutOfBorder()) {
                deleteIndexs.add(key);
            }
        });
        for (int i = 0; i < deleteIndexs.size(); i++) {
            String index = deleteIndexs.get(i);
            INeedTestBorder obj = borderTestGroup.get(index);
            obj.eliminate();
            borderTestGroup.remove(index);
        }
        deleteIndexs.clear();
    }

    public Border eliminateBorder;
    public Border visualBorder;
    public Border generateBorder;

    Raw frameTimerGroup = new Raw("frame timer group");

    public void addFrameTimer(FrameTimer timer) {
        frameTimerGroup.add(timer.name, timer);
    }

    public void removeFrameTimer(FrameTimer timer) {
        frameTimerGroup.remove(timer.name);
    }

    private void runAllFrameTimer(float interval) {
        frameTimerGroup.iterateValue((FrameTimer timer) -> {
            timer.countDown(interval);
        });
    }

    public float getRatio() {
        float ratio = (float) height / (float) width;
        return ratio;
    }

}








