package project;

import engine.Buffer.*;
import engine.Input.KeyBoard;
import engine.Input.Mouse;
import engine.Interface.*;
import engine.Meshes.ArrayMesh;
import engine.Meshes.ElementMesh;
import engine.Meshes.FileMesh;
import engine.Meshes.NullMesh;
import engine.Objs.Block;
import engine.Objs.Canvas;
import engine.Objs.Helper.Border;
import engine.Objs.Helper.TrackLine;
import engine.Objs.Player;
import engine.Objs.ScoreObj;
import engine.Objs.UIObjs.Button;
import engine.Objs.UIObjs.FontTextObj;
import engine.Objs.scenes.CloudGenerator;
import engine.Physics.BoundingMesh;
import engine.Physics.CollideInfo;
import engine.Physics.ForceFunctions.Force_Gravity;
import engine.Physics.ForceFunctions.Force_MoveOnGround;
import engine.Physics.ForceFunctions.Force_MoveOnWall;
import engine.Programs.Program;
import engine.Programs.ProgramRaw;
import engine.Programs.Shader;
import engine.Textures.FontTexture;
import engine.Textures.Texture;
import engine.Textures.TextureAtlas;
import engine.UniformFunctions.*;
import engine.Util.Debug;
import engine.Util.Raw;
import engine.View.Camera2d;
import engine.View.Camera3d;
import engine.View.CameraUI;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.awt.*;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.GL_LINES;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;


public class Game {

    static Canvas canvas;

    public static void main(String[] args) {
        test();
        canvas = new Canvas(640, 480);

        addShaders();
        addPrograms();
        addTextures();
        addVBOs();
        addCameras();
        addUniformFunctions();
        addMesh();

        setCollide();
        addTrackLines();
        addSceneObj();
        addUIObj();


        canvas.loop();

        canvas.clean();

    }


    private static void addTrackLines() {

        new TrackLine("trackFaceLine", new Vector4f(1f, 1f, 0f, 1f));//yellow
        new TrackLine("trackPointLine", new Vector4f(1f, 0f, 1f, 1f));//purple
        canvas.setVertexTrackLine("trackPointLine");
        canvas.setFaceTrackLine("trackFaceLine");

    }

    private static void test() {


//        Vector3f aaa = new Vector3f(1, 0, 0);
//        Vector3f bbb = new Vector3f();
//        aaa.mul(-1, bbb);
//        bbb.x = 1;
//        boolean xxx = (aaa.equals(bbb));
//        Matrix4f m = new Matrix4f();
//        m.translate(-10, 0, 0);
//
//
//        float value = 1230.000000000000001f;
//        float scale = (float) Math.pow(10, 7);
//        float result = Math.round(value * scale) / scale;
//
        float radians = (float) Math.toRadians(90);
        Vector3f v1 = new Vector3f(0f, 1f, 0f);
        Vector3f v2 = new Vector3f(0.7f, -0.7f, 0f);
//        Vector3f add = v1.add(v2);
//        float dist = v1.dot(v2);
        float angle = v1.angle(v2);
        float angle2 = v1.angleCos(v2);
//        Debug.log("dot:" + dist);
        Debug.log("angle:" + angle2);
    }

    private static void setCollide() {
        canvas.addCollideGroup("Blocks");
        canvas.addCollideGroup("Props");
        canvas.addCollideGroup("Enemies");
        canvas.addCollideGroup("Players");

        canvas.allowCollide("Players", "Blocks", (CollideInfo info) -> {
            Player player = info.getObjWithType(Player.class);
            Block block = info.getObjWithType(Block.class);

            if (info.hasCollideVetex(player.rootPoint)) {
                player.removeForce(Force_Gravity.class);

                IForceFunction forcefun = player.getForce(Force_MoveOnGround.class);
                if (forcefun != null) {//already on ground
                    ((INeedChangeData) forcefun).change(info);
                } else {
                    player.addForce(Force_MoveOnGround.class, info);

                }
            } else if (info.hasCollideVetex(player.corner0) || info.hasCollideVetex(player.corner2)) {
                Debug.log("collide side!");
//                player.speed.set(Constant.ZERO3f);
                player.removeForce(Force_Gravity.class);
                player.addForce(Force_MoveOnWall.class, info);

            }

//                


        });

    }


    private static void addMesh() {
        //------------------------------------------------------------
        //meshes must init after those VBO 
        canvas.addRes(new ArrayMesh((Raw raw) -> {
            raw.add("name", "triangleMesh");
            raw.add("program", "triangleProgram");
            raw.add("primitiveType", GL_TRIANGLES);

            Raw attributes = new Raw("attributes raw");
            raw.add("attributes", attributes);
            attributes.add("a_Position", "triangleVBO");


        }));
        canvas.addRes(new ElementMesh((Raw raw) -> {
            raw.add("name", "rectangleMesh");
            raw.add("program", "rectangleProgram");
            raw.add("primitiveType", GL_LINES);


            Raw attributes = new Raw("attributes raw");
            raw.add("attributes", attributes);
            attributes.add("a_Position", "rectangleVertexPosition");
            attributes.add("a_Color", "rectangleVertexColor");


            raw.add("indices", new String[]{
                    "rectangleIBO_Triangles",
                    "rectangleIBO_Lines"
            });


        }));
        canvas.addRes(new ElementMesh((Raw raw) -> {
            raw.add("name", "box");
            raw.add("program", "MVPProgram");
            raw.add("primitiveType", GL_LINES);

            Raw attributes = new Raw("attributes raw");
            raw.add("attributes", attributes);
            attributes.add("a_Position", "boxVertexPosition");
            attributes.add("a_Color", "boxVertexColor");


            raw.add("indices", new String[]{
                    "boxIBOTriangles",
                    "boxIBOLines"
            });


        }));
        canvas.addRes(new ArrayMesh((Raw raw) -> {
            raw.add("name", "originIndicatorMesh");
            raw.add("program", "MVPProgram");
            raw.add("primitiveType", GL_LINES);

            Raw attributes = new Raw("attributes raw");
            raw.add("attributes", attributes);
            attributes.add("a_Position", "originIndicatorVBO");
            attributes.add("a_Color", "originIndicatorVBO");

        }));

        canvas.addRes(new NullMesh());
        canvas.addRes(new ElementMesh((Raw raw) -> {
            raw.add("name", "Obj2dMesh");
            raw.add("program", "simple3DProgram");
            raw.add("primitiveType", GL_TRIANGLES);


            Raw attributes = new Raw("attributes raw");
            raw.add("attributes", attributes);
            attributes.add("a_Position", "rectangleVertexPosition");

            raw.add("indices", new String[]{
                    "rectangleIBO_Triangles",
                    "rectangleIBO_Lines"
            });


        }));

        canvas.addRes(new ElementMesh((Raw raw) -> {
            raw.add("name", "testUIMeshIn3D");
            raw.add("program", "simple3DProgram");
            raw.add("primitiveType", GL_TRIANGLES);/*GL_TRIANGLES*//*GL_LINES*//*GL_LINE_LOOP*/


            Raw attributes = new Raw("attributes raw");
            raw.add("attributes", attributes);
            attributes.add("a_Position", "uiTextVertexPos_VBO");


            raw.add("indices", new String[]{
                    "uiTextIBO_Triangles",
                    "uiTextIBO_Lines"

            });


        }));
        canvas.addRes(new ElementMesh((Raw raw) -> {
            raw.add("name", "box_SeperateFace");
            raw.add("program", "textureProgram");
            raw.add("primitiveType", GL_TRIANGLES);/*GL_TRIANGLES*//*GL_LINES*//*GL_LINE_LOOP*/


            Raw attributes = new Raw("attributes raw");
            raw.add("attributes", attributes);
            attributes.add("a_Position", "box_SeperateFace_VertexPosition_VBO");
            attributes.add("a_TextureCoords", "box_SeperateFace_TextureCoords_VBO");


            raw.add("indices", new String[]{
                    "box_seperateFace_IBO",
                    "box_seperateFace_IBO_LINE"

            });


        }));
        canvas.addRes(new FileMesh((Raw raw) -> {
            raw.add("name", "/model/face");
            raw.add("primitiveType", GL_TRIANGLES);
        }));
        canvas.addRes(new BoundingMesh("/model/faceBoundingbox"));
        canvas.addRes(new FileMesh((Raw raw) -> {
            raw.add("name", "/model/canon");
            raw.add("primitiveType", GL_LINES);
        }));
        canvas.addRes(new FileMesh((Raw raw) -> {
            raw.add("name", "/model/helicopter");
            raw.add("primitiveType", GL_LINES);
        }));
        canvas.addRes(new FileMesh((Raw raw) -> {
            raw.add("name", "/model/tree");
            raw.add("primitiveType", GL_TRIANGLES);
        }));
        canvas.addRes(new FileMesh((Raw raw) -> {
            raw.add("name", "/model/jumpbox");
            raw.add("primitiveType", GL_TRIANGLES);
        }));
        canvas.addRes(new BoundingMesh("/model/jumpbox_Bounding"));

        canvas.addRes(new FileMesh((Raw raw) -> {
            raw.add("name", "/model/groundSurface");
            raw.add("primitiveType", GL_TRIANGLES);
        }));
        canvas.addRes(new BoundingMesh("/model/groundBoundingbox"));

        canvas.addRes(new FileMesh((Raw raw) -> {
            raw.add("name", "/model/stone");
            raw.add("primitiveType", GL_TRIANGLES);
        }));
        canvas.addRes(new BoundingMesh("/model/stone_BoundingBox"));

        canvas.addRes(new FileMesh((Raw raw) -> {
            raw.add("name", "/model/testPlatform1");
            raw.add("primitiveType", GL_TRIANGLES);
        }));
        canvas.addRes(new BoundingMesh("/model/testPlatform1_BoundingBox"));


        canvas.addRes(new FileMesh((Raw raw) -> {
            raw.add("name", "/model/testPlatform2");
            raw.add("primitiveType", GL_TRIANGLES);
        }));
        canvas.addRes(new BoundingMesh("/model/testPlatform2_BoundingBox"));

        canvas.addRes(new FileMesh((Raw raw) -> {
            raw.add("name", "/model/testPlatform3");
            raw.add("primitiveType", GL_TRIANGLES);
        }));
        canvas.addRes(new BoundingMesh("/model/testPlatform3_BoundingBox"));

        canvas.addRes(new FileMesh((Raw raw) -> {
            raw.add("name", "/model/thorns");
            raw.add("primitiveType", GL_TRIANGLES);
        }));
        canvas.addRes(new BoundingMesh("/model/thorns_BoundingBox"));

        canvas.addRes(new FileMesh((Raw raw) -> {
            raw.add("name", "/model/cloud");
            raw.add("primitiveType", GL_TRIANGLES);
        }));
        canvas.addRes(new FileMesh((Raw raw) -> {
            raw.add("name", "/model/flag");
            raw.add("primitiveType", GL_TRIANGLES);
        }));

        canvas.addRes(new FileMesh((Raw raw) -> {
            raw.add("name", "/model/button");
            raw.add("primitiveType", GL_TRIANGLES);
        }));

    }


    private static void addCameras() {
        canvas.addRes(new CameraUI((Raw raw) -> {
            raw.add("name", "UICamera");
            raw.add("left", 0f);
            raw.add("right", (float) canvas.width);
            raw.add("bottom", (float) canvas.height);
            raw.add("top", 0f);
            raw.add("near", -100f);
            raw.add("far", 100f);

        }));
        canvas.setUICamera("UICamera");
        canvas.addRes(new Camera3d((Raw raw) -> {
            raw.add("name", "3dSceneCamera");
            raw.add("init", (IInit<Camera3d>) (Camera3d camera) -> {
                camera.FOV = camera.set("FOV", 60f);
                camera.near = camera.set("near", 0.01f);
                camera.far = camera.set("far", 1000f);

                camera.position.x = 4.45f;
                camera.position.y = 7.44f;
                camera.position.z = 14.69f;
                camera.rotation.x = 19.12f;
                camera.rotation.y = -13.20f;

            });
            raw.add("input", (IInput<Camera3d>) (KeyBoard key, Mouse mouse, Camera3d camera) -> {

                if (key.isPressed(GLFW_KEY_UP)) {
                    camera.deltaPos.z += -1;
                } else if (key.isPressed(GLFW_KEY_DOWN)) {
                    camera.deltaPos.z += 1;
                }
                if (key.isPressed(GLFW_KEY_LEFT)) {
                    camera.deltaPos.x += -1;
                } else if (key.isPressed(GLFW_KEY_RIGHT)) {
                    camera.deltaPos.x += 1;
                }
                if (key.isPressed(GLFW_KEY_KP_1)) {
                    camera.deltaPos.y += -1;
                } else if (key.isPressed(GLFW_KEY_KP_0)) {
                    camera.deltaPos.y += 1;
                }

                if (mouse.rightButtonPressed) {
                    camera.deltaRot.set(mouse.delta);

                } else
                    camera.deltaRot.set(0, 0);
            });
            raw.add("update", (IUpdate<Camera3d>) (float interval, Camera3d camera) -> {

                float moveStep = interval * camera.moveSpeed;


                camera.move(camera.deltaPos.x * moveStep, camera.deltaPos.y * moveStep, camera.deltaPos.z * moveStep);
                camera.deltaPos.set(0, 0, 0);

                final float speed = 10f;
                float rotateStep = interval * speed;


                float x;
                float y;
                float normal = 20f;
                x = camera.deltaRot.y / normal * rotateStep;
                y = camera.deltaRot.x / normal * rotateStep;

                camera.rotate(x, y, 0);
//                Debug.log(camera.rotation.x + "|" + camera.rotation.y);
                camera.buildViewMatrix();

            });
        }));

        canvas.addRes(new Camera2d((Raw raw) -> {
            raw.add("name", "2dSceneCamera");
//            raw.add("wid", 5f);
            float width = 11.90f;
            float ratio = canvas.getRatio();
            float height = width * ratio;
            raw.add("ratio", ratio);
            raw.add("left", -width);
            raw.add("right", width);
            raw.add("bottom", -height);
            raw.add("top", height);
            raw.add("near", -20f);
            raw.add("far", 20f);
            raw.add("init", (IInit<Camera2d>) (Camera2d camera) -> {
                camera.position.y = 1.6f;
            });
            raw.add("input", (IInput<Camera2d>) (KeyBoard key, Mouse mouse, Camera2d camera) -> {

                if (key.isPressed(GLFW_KEY_RIGHT)) {
                    camera.deltaPos.x += -1;
                } else if (key.isPressed(GLFW_KEY_LEFT)) {
                    camera.deltaPos.x += 1;
                }
                if (key.isPressed(GLFW_KEY_UP)) {
                    camera.deltaPos.y += -1;
                } else if (key.isPressed(GLFW_KEY_DOWN)) {
                    camera.deltaPos.y += 1;
                }
                camera.zoomed = false;
                if (key.isPressed(GLFW_KEY_KP_1)) {
                    camera.zoomIn();
                    camera.zoomed = true;
                } else if (key.isPressed(GLFW_KEY_KP_0)) {
                    camera.zoomOut();
                    camera.zoomed = true;
                }

            });
            raw.add("update", (IUpdate<Camera2d>) (float interval, Camera2d camera) -> {

                float moveStep = interval * camera.moveSpeed;
                camera.move(camera.deltaPos.x * moveStep, camera.deltaPos.y * moveStep, camera.deltaPos.z * moveStep);
                camera.deltaPos.set(0, 0, 0);

                camera.buildViewMatrix();

            });
        }));
        canvas.setSceneCamera("2dSceneCamera");
    }

    private static void addUIObj() {
        canvas.createObj(new FontTextObj((Raw raw) -> {
            raw.add("name", "scoreText");
            raw.add("camera", canvas.UICamera);
            raw.add("color", new Vector4f(1f, 0f, 0f, 0.8f));
            raw.add("init", (IInit<FontTextObj>) (FontTextObj obj) -> {
                obj.raw.add("text", "SCORE:");
                obj.raw.add("fontTextureName", "InGameTextFontTexture");
                obj.matrix.scale(10f, 10f, 10f);
                obj.operateMatrix.translate(10f, 10f, 0f);
//                    obj.operateMatrix.scale(4f, 4f, 0f);
            });

        }));

        canvas.createObj(new ScoreObj((Raw raw) -> {
            raw.add("name", "scoreNum");
            raw.add("camera", canvas.UICamera);
            raw.add("color", new Vector4f(1f, 0f, 0f, 0.8f));
            raw.add("init", (IInit<ScoreObj>) (ScoreObj obj) -> {
                obj.raw.add("fontTextureName", "InGameTextFontTexture");
                obj.matrix.scale(10f, 10f, 10f);
                obj.operateMatrix.translate(200f, 10f, 0f);
//                    obj.operateMatrix.scale(4f, 4f, 0f);
            });
            raw.add("update", (IUpdate<ScoreObj>) (float interval, ScoreObj score) -> {
                score.increase(interval);
            });
//            raw.add("input", (IInput<ScoreObj>) (KeyBoard key, Mouse mouse, ScoreObj obj) -> {
//                if (key.isPressed(GLFW_KEY_U)) {
////                    obj.changeScore(9876);
//                    obj.increase(0.1f);
//                }
//
//
//            });

        }));
        canvas.createObj(new FontTextObj((Raw raw) -> {
            raw.add("name", "cameraPositionInfo");
            raw.add("camera", canvas.UICamera);
            raw.add("init", (IInit<FontTextObj>) (FontTextObj obj) -> {
                obj.raw.add("text", "cameraPos:");
                obj.raw.add("fontTextureName", "DebugTextFontTexture");
                obj.matrix.scale(10f, 10f, 10f);
                obj.operateMatrix.translate(400f, 10f, 0f);
//                    obj.operateMatrix.scale(4f, 4f, 0f);
            });
            raw.add("update", (IUpdate<FontTextObj>) (float interval, FontTextObj obj) -> {
                String display = "";

                if (canvas.camera instanceof Camera3d) {
                    if (canvas.camera.moved) {
                        String x = String.format("%.2f", canvas.camera.position.x);
                        String y = String.format("%.2f", canvas.camera.position.y);
                        String z = String.format("%.2f", canvas.camera.position.z);

                        obj.changeText(x + "," + y + "," + z);


                    }
                } else if (canvas.camera instanceof Camera2d) {

                    if (canvas.camera.moved || ((Camera2d) canvas.camera).zoomed) {
                        String x = String.format("%.2f", canvas.camera.position.x);
                        String y = String.format("%.2f", canvas.camera.position.y);

                        String left = String.format("%.2f", ((Camera2d) canvas.camera).left);
                        String bottom = String.format("%.2f", ((Camera2d) canvas.camera).bottom);

                        obj.changeText(x + "," + y + "|" + left + "," + bottom);
                    }

                }


            });
        }));
        canvas.createObj(new FontTextObj((Raw raw) -> {
            raw.add("name", "cameraRotateInfo");
            raw.add("camera", canvas.UICamera);
            raw.add("init", (IInit<FontTextObj>) (FontTextObj obj) -> {
                obj.raw.add("text", "cameraRot:");
                obj.raw.add("fontTextureName", "DebugTextFontTexture");
                obj.matrix.scale(10f, 10f, 10f);
                obj.operateMatrix.translate(400f, 22f, 0f);
//                    obj.operateMatrix.scale(4f, 4f, 0f);
            });
            raw.add("update", (IUpdate<FontTextObj>) (float interval, FontTextObj obj) -> {
                if (canvas.camera.rotated) {

                    String x = String.format("%.2f", canvas.camera.rotation.x);
                    String y = String.format("%.2f", canvas.camera.rotation.y);
                    String z = String.format("%.2f", canvas.camera.rotation.z);
                    obj.changeText(x + "," + y + "," + z);
                }
            });
        }));
        Button button = new Button("restart");
        button.operateMatrix.translate(10f, 100f, 0f);
        button.mouseClickFun=()->{
            canvas.player.reset();
            canvas.sceneManager.resetScene();
        };


    }

    private static void addSceneObj() {
//        new OriginIndicator(canvas);
        float visualWid = 15f;
        float eliminWid = visualWid * 2f;
        float generaWid = visualWid * 1.5f;
        canvas.eliminateBorder = new Border("eliminate border",eliminWid, eliminWid * canvas.getRatio());
        canvas.visualBorder = new Border("visual border",visualWid, visualWid * canvas.getRatio());
        canvas.generateBorder = new Border("generate border",generaWid, generaWid * canvas.getRatio());

        canvas.sceneManager.beginCreate();

        canvas.createObj(new Player((Raw raw) -> {
            raw.add("name", "jumpbox");
            raw.add("meshName", "/model/jumpbox");
            raw.add("boundingBox", "/model/jumpbox_Bounding");
//            raw.add("u_Color", new Vector4f(0f, 1f, 1f, 1f));

            raw.add("init", (IInit<Player>) (Player player) -> {
//                player.operateMatrix.rotateZ((float) Math.toRadians(10));
                player.addForce(Force_Gravity.class, new Vector3f());

            });

            raw.add("update", (IUpdate<Player>) (float interval, Player obj) -> {

            });
        }));

        CloudGenerator cloudGenerator = canvas.allRes.get("cloud generator");
        cloudGenerator.begin();
    }


    private static void addShaders() {

        new Shader(GL_VERTEX_SHADER, "/shader/simple3D.vert");
        new Shader(GL_FRAGMENT_SHADER, "/shader/simple.frag");

        new Shader(GL_VERTEX_SHADER, "/shader/triangle.vert");
        new Shader(GL_FRAGMENT_SHADER, "/shader/triangle.frag");

        new Shader(GL_VERTEX_SHADER, "/shader/rectangle.vert");
        new Shader(GL_FRAGMENT_SHADER, "/shader/rectangle.frag");

        new Shader(GL_VERTEX_SHADER, "/shader/mvp.vert");
        new Shader(GL_VERTEX_SHADER, "/shader/simpleTriangle.vert");
        new Shader(GL_VERTEX_SHADER, "/shader/texture.vert");

        new Shader(GL_FRAGMENT_SHADER, "/shader/texture.frag");

        new Shader(GL_VERTEX_SHADER, "/shader/UIText.vert");
        new Shader(GL_FRAGMENT_SHADER, "/shader/UIText.frag");

        new Shader(GL_VERTEX_SHADER, "/shader/text.vert");
        new Shader(GL_FRAGMENT_SHADER, "/shader/text.frag");

        new Shader(GL_VERTEX_SHADER, "/shader/model.vert");
        new Shader(GL_FRAGMENT_SHADER, "/shader/model.frag");

        new Shader(GL_VERTEX_SHADER, "/shader/modelNoTex.vert");
        new Shader(GL_FRAGMENT_SHADER, "/shader/modelNoTex.frag");

    }

    static void addPrograms() {
        //program
        canvas.addRes(new Program((ProgramRaw data) -> {
            data.add("name", "Program-modelNoTex");
            data.add("vertexShaderName", "/shader/modelNoTex.vert");
            data.add("fragShaderName", "/shader/modelNoTex.frag");
            data.add("attributes", new String[]{
                    "a_Position",
                    "a_Color",
            });
            data.add("uniforms", new String[]{
                    "u_MVPMatrix",
            });

        }));
        canvas.addRes(new Program((ProgramRaw data) -> {
            data.add("name", "Program-model");
            data.add("vertexShaderName", "/shader/model.vert");
            data.add("fragShaderName", "/shader/model.frag");
            data.add("attributes", new String[]{
                    "a_Position",
                    "a_Color",
                    "a_TexCoords"
            });
            data.add("uniforms", new String[]{
                    "u_MVPMatrix",
                    "u_UseTexture"
            });
            Raw channels = new Raw("channels");
            data.add("channels", channels);
            channels.add("u_Texture01", 1);

        }));

        canvas.addRes(new Program((ProgramRaw data) -> {
            data.add("name", "textProgram");
            data.add("vertexShaderName", "/shader/text.vert");
            data.add("fragShaderName", "/shader/text.frag");
            data.add("attributes", new String[]{
                    "a_Position",
                    "a_TextureCoords"
            });
            data.add("uniforms", new String[]{
                    "u_MVPMatrix",
                    "u_Color"
            });
            Raw channels = new Raw("channels");
            data.add("channels", channels);
            channels.add("u_Texture1", 1);
        }));

        canvas.addRes(new Program((ProgramRaw data) -> {
            data.add("name", "triangleProgram");
            data.add("vertexShaderName", "/shader/simpleTriangle.vert");
            data.add("fragShaderName", "/shader/triangle.frag");
            data.add("attributes", new String[]{
                    "a_Position"
            });


        }));
        canvas.addRes(new Program((ProgramRaw data) -> {
            data.add("name", "simple3DProgram");
            data.add("vertexShaderName", "/shader/simple3D.vert");
            data.add("fragShaderName", "/shader/simple.frag");
            data.add("attributes", new String[]{
                    "a_Position"
            });
            data.add("uniforms", new String[]{
                    "u_MVPMatrix",
                    "u_Color",
            });
        }));

        canvas.addRes(new Program((ProgramRaw data) -> {
            data.add("name", "rectangleProgram");
            data.add("vertexShaderName", "/shader/rectangle.vert");
            data.add("fragShaderName", "/shader/rectangle.frag");
            data.add("attributes", new String[]{
                    "a_Position",
                    "a_Color"
            });
            data.add("uniforms", new String[]{
                    "u_MVPMatrix"
            });


        }));


        canvas.addRes(new Program((ProgramRaw data) -> {
            data.add("name", "MVPProgram");
            data.add("vertexShaderName", "/shader/mvp.vert");
            data.add("fragShaderName", "/shader/rectangle.frag");

            data.add("attributes", new String[]{
                    "a_Position",
                    "a_Color"
            });
            data.add("uniforms", new String[]{
                    "u_MVPMatrix",
                    "u_ModelMatrix"
            });

        }));
        canvas.addRes(new Program((ProgramRaw data) -> {
            data.add("name", "textureProgram");
            data.add("vertexShaderName", "/shader/texture.vert");
            data.add("fragShaderName", "/shader/texture.frag");

            data.add("attributes", new String[]{
                    "a_Position",
                    "a_TextureCoords"
            });
            data.add("uniforms", new String[]{
                    "u_MVPMatrix",
            });

            Raw channels = new Raw("channels");
            data.add("channels", channels);
            channels.add("u_Texture1", 1);


        }));
        canvas.addRes(new Program((ProgramRaw data) -> {
            data.add("name", "UITextProgram");
            data.add("vertexShaderName", "/shader/UIText.vert");
            data.add("fragShaderName", "/shader/UIText.frag");

            data.add("attributes", new String[]{
                    "a_Position",

            });
            data.add("uniforms", new String[]{
                    "u_MVPMatrix",

            });


        }));
    }

    private static void addUniformFunctions() {
        //uniforms functions
        canvas.addRes(new u_ProjectMatrix());
        canvas.addRes(new u_MVPMatrix());
        canvas.addRes(new u_ModelMatrix());
        canvas.addRes(new u_Sampler());
        canvas.addRes(new u_Color());
        canvas.addRes(new u_UseTexture());
    }


    private static void addVBOs() {
        //VBO
        //triangleBuffers
        canvas.addRes(new VBO((VBORaw data) -> {
            data.add("name", "triangleVBO");
            data.add("raw", new float[]{
                    0.0f, 0.3f, -1f,
                    -0.3f, -0.3f, -1f,
                    0.3f, -0.3f, -1f
            });

            data.add("pointNum", 3);
            data.setDataAmount("a_Position", 3);

        }));


        //rectangle buffers
        canvas.addRes(new VBO((VBORaw data) -> {
            data.add("name", "rectangleVertexPosition");
            data.add("raw", new float[]{
                    0.5f, -0.5f, 0f,
                    0.5f, 0.5f, 0f,
                    -0.5f, 0.5f, 0f,
                    -0.5f, -0.5f, 0f,
            });
            data.add("pointNum", 4);
            data.setDataAmount("a_Position", 3);

        }));
        canvas.addRes(new VBO((VBORaw data) -> {
            data.add("name", "rectangleVertexTextureCoords");
            data.add("raw", new float[]{
                    0f, 1f,
                    1f, 1f,
                    1f, 0f,
                    0f, 0f,
            });
            data.add("pointNum", 4);
            data.setDataAmount("a_TextureCoords", 2);

        }));
        canvas.addRes(new VBO((VBORaw data) -> {
            data.add("name", "rectangleVertexColor");
            data.add("raw", new float[]{
                    0.5f, 0.0f, 0.0f,
                    0.0f, 0.5f, 0.0f,
                    0.0f, 0.0f, 0.5f,
                    0.0f, 0.5f, 0.5f,
            });
            data.add("pointNum", 4);
            data.setDataAmount("a_Color", 3);

        }));

        canvas.addRes(new IBO((Raw raw) -> {
            raw.add("name", "rectangleIBO_Triangles");
            raw.add("raw", new int[]{
                    0, 1, 2, 2, 3, 0
            });
        }));
        canvas.addRes(new IBO((Raw raw) -> {
            raw.add("name", "rectangleIBO_Lines");
            raw.add("raw", new int[]{
                    0, 1, 1, 2, 2, 3, 3, 0,
            });
        }));


        //box buffers
        canvas.addRes(new VBO((VBORaw data) -> {
            data.add("name", "boxVertexPosition");
            data.add("raw", new float[]{
                    -0.5f, 0.5f, 0.5f,
                    -0.5f, -0.5f, 0.5f,
                    0.5f, -0.5f, 0.5f,
                    0.5f, 0.5f, 0.5f,
                    -0.5f, 0.5f, -0.5f,
                    0.5f, 0.5f, -0.5f,
                    -0.5f, -0.5f, -0.5f,
                    0.5f, -0.5f, -0.5f,
            });
            data.add("pointNum", 8);
            data.setDataAmount("a_Position", 3);

        }));
        canvas.addRes(new VBO((VBORaw data) -> {
            data.add("name", "boxVertexColor");
            data.add("raw", new float[]{
                    0.5f, 0.0f, 0.0f,
                    0.0f, 0.5f, 0.0f,
                    0.0f, 0.0f, 0.5f,
                    0.0f, 0.5f, 0.5f,
                    0.5f, 0.0f, 0.0f,
                    0.0f, 0.5f, 0.0f,
                    0.0f, 0.0f, 0.5f,
                    0.0f, 0.5f, 0.5f,
            });
            data.add("pointNum", 8);
            data.setDataAmount("a_Color", 3);

        }));
        canvas.addRes(new IBO((Raw raw) -> {
            raw.add("name", "boxIBOTriangles");
            raw.add("raw", new int[]{
                    // Front face
                    0, 1, 3, 3, 1, 2,
                    // Top Face
                    4, 0, 3, 5, 4, 3,
                    // Right face
                    3, 2, 7, 5, 3, 7,
                    // Left face
                    6, 1, 0, 6, 0, 4,
                    // Bottom face
                    2, 1, 6, 2, 6, 7,
                    // Back face
                    7, 6, 4, 7, 4, 5,
            });
        }));
        canvas.addRes(new IBO((Raw raw) -> {
            raw.add("name", "boxIBOLines");
            raw.add("raw", new int[]{
                    0, 1, 1, 2, 2, 3, 3, 0,
                    5, 4, 4, 6, 6, 7, 7, 5,
                    0, 4, 3, 5, 1, 6, 2, 7,
            });
        }));

        canvas.addRes(new VBO((VBORaw data) -> {
            data.add("name", "originIndicatorVBO");
            data.add("raw", new float[]{
                    0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f,
                    0.2f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f,

                    0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f,
                    0.0f, 0.2f, 0.0f, 0.0f, 1.0f, 0.0f,

                    0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f,
                    0.0f, 0.0f, 0.2f, 0.0f, 0.0f, 1.0f,
            });
            data.add("pointNum", 6);
            data.setDataAmount("a_Position", 3);
            data.setDataAmount("a_Color", 3);

        }));
        canvas.addRes(new VBO((VBORaw data) -> {
            data.add("name", "box_SeperateFace_VertexPosition_VBO");
            data.add("raw", new float[]{
                    // V0
                    -0.5f, 0.5f, 0.5f,
                    // V1
                    -0.5f, -0.5f, 0.5f,
                    // V2
                    0.5f, -0.5f, 0.5f,
                    // V3
                    0.5f, 0.5f, 0.5f,
                    // V4
                    -0.5f, 0.5f, -0.5f,
                    // V5
                    0.5f, 0.5f, -0.5f,
                    // V6
                    -0.5f, -0.5f, -0.5f,
                    // V7
                    0.5f, -0.5f, -0.5f,
                    // For text coords in top face
                    // V8: V4 repeated
                    -0.5f, 0.5f, -0.5f,
                    // V9: V5 repeated
                    0.5f, 0.5f, -0.5f,
                    // V10: V0 repeated
                    -0.5f, 0.5f, 0.5f,
                    // V11: V3 repeated
                    0.5f, 0.5f, 0.5f,
                    // For text coords in right face
                    // V12: V3 repeated
                    0.5f, 0.5f, 0.5f,
                    // V13: V2 repeated
                    0.5f, -0.5f, 0.5f,
                    // For text coords in left face
                    // V14: V0 repeated
                    -0.5f, 0.5f, 0.5f,
                    // V15: V1 repeated
                    -0.5f, -0.5f, 0.5f,
                    // For text coords in bottom face
                    // V16: V6 repeated
                    -0.5f, -0.5f, -0.5f,
                    // V17: V7 repeated
                    0.5f, -0.5f, -0.5f,
                    // V18: V1 repeated
                    -0.5f, -0.5f, 0.5f,
                    // V19: V2 repeated
                    0.5f, -0.5f, 0.5f,
            });

            data.add("pointNum", 20);
            data.setDataAmount("a_Position", 3);

        }));
        canvas.addRes(new VBO((VBORaw data) -> {
            data.add("name", "box_SeperateFace_TextureCoords_VBO");
            data.add("raw", new float[]{
                    0.0f, 0.0f,//0
                    0.0f, 0.5f,//1
                    0.5f, 0.5f,//2
                    0.5f, 0.0f,//3
                    0.0f, 0.0f,//4
                    0.5f, 0.0f,//5
                    0.0f, 0.5f,//6
                    0.5f, 0.5f,//7
                    // For text coords in top face
                    0.0f, 0.5f,
                    0.5f, 0.5f,
                    0.0f, 1.0f,
                    0.5f, 1.0f,
                    // For text coords in right face
                    0.0f, 0.0f,
                    0.0f, 0.5f,
                    // For text coords in left face
                    0.5f, 0.0f,
                    0.5f, 0.5f,
                    // For text coords in bottom face
                    0.5f, 0.0f,
                    1.0f, 0.0f,
                    0.5f, 0.5f,
                    1.0f, 0.5f,
            });

            data.add("pointNum", 20);
            data.setDataAmount("a_TextureCoords", 2);

        }));
        canvas.addRes(new IBO((Raw raw) -> {
            raw.add("name", "box_seperateFace_IBO");
            raw.add("raw", new int[]{
                    // Front face
                    0, 1, 3, 3, 1, 2,
                    // Top Face
                    8, 10, 11, 9, 8, 11,
                    // Right face
                    12, 13, 7, 5, 12, 7,
                    // Left face
                    14, 15, 6, 4, 14, 6,
                    // Bottom face
                    16, 18, 19, 17, 16, 19,
                    // Back face
                    4, 6, 7, 5, 4, 7,
            });
        }));


        canvas.addRes(new IBO((Raw raw) -> {
            raw.add("name", "box_seperateFace_IBO_LINE");
            raw.add("raw", new int[]{
                    // Front face
                    0, 1, 1, 2, 2, 3, 3, 0,
                    3, 5, 5, 4, 4, 0, 0, 3,
                    5, 7, 7, 6, 6, 4,
                    7, 2, 1, 6

            });
        }));
        canvas.addRes(new VBO((VBORaw data) -> {
            data.add("name", "box_SeperateFace_VertexColor_differencePerface_VBO");
            data.add("raw", new float[]{
                    0.4f, 0.4f, 1.0f, 0.4f, 0.4f, 1.0f, 0.4f, 0.4f, 1.0f, 0.4f, 0.4f, 1.0f,  // v0-v1-v2-v3 front(blue)
                    0.4f, 1.0f, 0.4f, 0.4f, 1.0f, 0.4f, 0.4f, 1.0f, 0.4f, 0.4f, 1.0f, 0.4f,  // v0-v3-v4-v5 right(green)
                    1.0f, 0.4f, 0.4f, 1.0f, 0.4f, 0.4f, 1.0f, 0.4f, 0.4f, 1.0f, 0.4f, 0.4f,  // v0-v5-v6-v1 up(red)
                    1.0f, 1.0f, 0.4f, 1.0f, 1.0f, 0.4f, 1.0f, 1.0f, 0.4f, 1.0f, 1.0f, 0.4f,  // v1-v6-v7-v2 left
                    1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f,  // v7-v4-v3-v2 down
                    0.4f, 1.0f, 1.0f, 0.4f, 1.0f, 1.0f, 0.4f, 1.0f, 1.0f, 0.4f, 1.0f, 1.0f   // v4-v7-v6-v5 back
            });

            data.add("pointNum", 24);
            data.setDataAmount("a_Color", 3);

        }));
        canvas.addRes(new VBO((VBORaw data) -> {
            data.add("name", "box_SeperateFace_VertexColor_allWhite_VBO");
            data.add("raw", new float[]{
                    1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f,  // v0-v1-v2-v3 front(blue)
                    1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f,  // v0-v3-v4-v5 right(green)
                    1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f,  // v0-v5-v6-v1 up(red)
                    1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f,  // v1-v6-v7-v2 left
                    1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f,  // v7-v4-v3-v2 down
                    1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f   // v4-v7-v6-v5 back
            });

            data.add("pointNum", 24);
            data.setDataAmount("a_Color", 3);

        }));
        canvas.addRes(new VBO((VBORaw data) -> {
            data.add("name", "box_SeperateFace_vertexNormal_VBO");
            data.add("pointNum", 24);
            data.setDataAmount("a_Color", 3);
            data.add("raw", new float[]{
                    0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f,  // v0-v1-v2-v3 front
                    1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f,  // v0-v3-v4-v5 right
                    0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f,  // v0-v5-v6-v1 up
                    -1.0f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f,  // v1-v6-v7-v2 left
                    0.0f, -1.0f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f, -1.0f, 0.0f,  // v7-v4-v3-v2 down
                    0.0f, 0.0f, -1.0f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f, -1.0f   // v4-v7-v6-v5 back
            });


        }));
        canvas.addRes(new VBO((VBORaw data) -> {
            data.add("name", "NullVertexPosition");
            data.add("pointNum", 6);
            data.setDataAmount("a_Position", 3);

            data.add("raw", new float[]{
                    0f, 0f, 0f,
                    1f, 0f, 0f,
                    0f, 0f, 0f,
                    0f, 1f, 0f,
                    0f, 0f, 0f,
                    0f, 0f, 1f,
            });
        }));

        canvas.addRes(new VBO((VBORaw data) -> {
            data.add("name", "NullVertexColor");
            data.add("pointNum", 6);
            data.setDataAmount("a_Color", 3);

            data.add("raw", new float[]{
                    1f, 0f, 0f,
                    1f, 0f, 0f,
                    0f, 1f, 0f,
                    0f, 1f, 0f,
                    0f, 0f, 1f,
                    0f, 0f, 1f,
            });
        }));
        canvas.addRes(new IBO_D((Raw iboRaw) -> {
            iboRaw.add("name", "uiTextIBO_Triangles");
            iboRaw.add("raw", new int[]{
                    0, 1, 3, 3, 1, 2
            });
        }));
        canvas.addRes(new IBO_D((Raw iboRaw) -> {
            iboRaw.add("name", "uiTextIBO_Lines");
            iboRaw.add("raw", new int[]{
                    0, 1, 1, 2, 2, 3, 3, 0
            });
        }));
        canvas.addRes(new VBO_D((VBORaw positionData) -> {
            positionData.add("name", "uiTextVertexPos_VBO");
            positionData.add("pointNum", 4);
            positionData.setDataAmount("a_Position", 3);

            positionData.add("raw", new float[]{
                    0f, 1f, 0f,
                    0f, 0f, 0f,
                    1f, 0f, 0f,
                    1f, 1f, 0f,
            });

        }));
    }

    static void addTextures() {
        //textures
        ;
        canvas.addRes(new FontTexture((Raw raw) -> {
            raw.add("name", "DebugTextFontTexture");/*Showcard Gothic*//*Ethnocentric*//*a*/
            raw.add("font", new Font("ProFontWindows", Font.PLAIN, 12));
            raw.add("charSetName", "ISO-8859-1");

        }));
        canvas.addRes(new FontTexture((Raw raw) -> {
            raw.add("name", "InGameTextFontTexture");/*Showcard Gothic*//*Ethnocentric*//*a*/
            raw.add("font", new Font("Showcard Gothic", Font.PLAIN, 40));
            raw.add("charSetName", "ISO-8859-1");

        }));
        canvas.addRes(new TextureAtlas((Raw raw) -> {
            raw.add("name", "/textures/font_texture.png");
            raw.add("row", 16);
            raw.add("col", 16);
        }));
        canvas.addRes(new Texture((Raw raw) -> {
            raw.add("name", "/textures/grassblock.png");
        }));
        canvas.addRes(new Texture((Raw raw) -> {
            raw.add("name", "/textures/stone.png");
        }));

        canvas.addRes(new Texture((Raw raw) -> {
            raw.add("name", "/textures/default.png");
        }));
    }
}





