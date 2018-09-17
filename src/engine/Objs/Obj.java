package engine.Objs;

import engine.Interface.*;
import engine.Meshes.Mesh;
import engine.Meshes.NullMesh;
import engine.Textures.Texture;
import engine.Util.Raw;
import engine.View.Camera;
import org.joml.Matrix4f;
import org.joml.Vector4f;

public class Obj extends SceneObject implements
        IRenderable,
        IParent,
        IChild,
        INeedCreate,
        INeedClean {
    public Obj() {
        index++;
//        Debug.log(this.getClass().getName());
    }

    public Obj(String meshName) {
        name = "obj" + index;
        index++;

        mesh = canvas.allRes.getX(meshName);
        camera = canvas.camera;
        attachCustomTexture();
    }

    public Obj(InputProperty<Raw> input) {
        input.run(raw);
        createNameByRaw();
        index++;
    }


    public Mesh mesh;
    public NullMesh originMesh;


    public Matrix4f modelMatrix = new Matrix4f(); //combine by parent|transform|operate|matrix

    Matrix4f parentMatrix = new Matrix4f();//accumulate from parent
    Matrix4f transformMatrix = new Matrix4f();//clear every frame, better use to animate object by a variable
    public Matrix4f operateMatrix = new Matrix4f();//not clear every frame, use to place object or transform  object by user input
    public Matrix4f matrix = new Matrix4f();//most private Matrix, define object center


    public Matrix4f normalMatrix = new Matrix4f();//use to calculate light


    public Raw finalTextures;

    @Override
    public Matrix4f getParentMatrix() {
        return parentMatrix;
    }


    @Override
    public IParent getParent() {
        return parent;
    }

    public Camera camera;
    public static Canvas canvas;

    public void createNameByRaw() {
        name = raw.get("name");
        if (name == null) {
            createDefaultName();
        }

    }

    void createName(String name) {
        this.name = name;
        if (name == null) {
            createDefaultName();
        }

    }

    void createDefaultName() {
        name = "obj" + index;
    }

    public void create() {
        mesh = canvas.allRes.getX(raw.getX("meshName"));
        attachCamera();
        attachCallbacks();
        attachCustomTexture();
        calculateMatrix();
    }


    public void attachCamera() {
        camera = raw.get("camera");
        if (camera == null)
            camera = canvas.camera;
    }

    public void attachInputFun(IInput fun) {
        inputCallBack = fun;
        canvas.inputGroup.add(name, this);
    }

    public void attachUpdateFun(IUpdate fun) {
        updateCallBack = fun;
        canvas.updateGroup.add(name, this);
    }

    public void attachCallbacks() {
        initCallBack = raw.get("init");
        if (initCallBack != null) initCallBack.init(this);
        inputCallBack = raw.get("input");
        updateCallBack = raw.get("update");

    }

    public IInput getInputCallBack() {
        return inputCallBack;
    }


    public void attachCustomTexture() {
        Raw defaultTextures = mesh.textures;
        if (defaultTextures == null) return;
        finalTextures = new Raw("final textures");
        defaultTextures.iterateKeyValueX((String name, Texture defaultTexture) -> {
            Raw customTextures = raw.get("textures");
            if (customTextures == null) {
                finalTextures.add(name, defaultTexture);
            } else {
                String customTextureName = customTextures.get(name);
                Texture texture = canvas.allRes.getX(customTextureName);
                finalTextures.add(name, texture);
            }
        });

    }


    @Override
    public void clean() {

    }

    void removeFromGroup(Raw renderGroups, String programName) {
        Raw group = renderGroups.get(programName);
        group.remove(name);
    }

    public void hide() {
        removeFromRenderGroups();
    }

    public void show() {
        addToRenderGroups();
    }

    @Override
    public void removeFromRenderGroups() {
        if (mesh != null) {
            removeFromGroup(canvas.renderGroups_Normal, mesh.program.name);
        }
        if (originMesh != null) {
            removeFromGroup(canvas.renderGroup_Origin, originMesh.program.name);
        }

    }

    public void addToGroup(Raw renderGroups, String programName) {
        Raw group = renderGroups.get(programName);
        if (group == null) {
            group = new Raw("render group " + programName);
            renderGroups.add(programName, group);
        }
        group.add(this.name, this);
    }

    @Override
    public void addToRenderGroups() {
        if (mesh != null) {
            addToGroup(canvas.renderGroups_Normal, mesh.program.name);
        }

        if (originMesh != null) {
            addToGroup(canvas.renderGroup_Origin, originMesh.program.name);
        }

    }

    Vector4f color = new Vector4f(1f, 1f, 1f, 1f);//default white

    public void setObjColor(Vector4f color) {
        this.color.x = color.x;
        this.color.y = color.y;
        this.color.z = color.z;
        this.color.w = color.w;
    }

    @Override
    public void render() {
//        glUseProgram(mesh.program.id);
        setUColor(color);
        connectUniforms(mesh);
        connectAllTexture();
        mesh.render();
    }


    public void renderOrigin() {

        connectUniforms(originMesh);
        originMesh.render();

    }

    void connectUniforms(Mesh mesh) {
        if (mesh.uniformsFunction == null) return;
        mesh.uniformsFunction.iterateKeyValue((String name, IUniformFunction uniformfun) -> {
            int location = mesh.uniformsLocation.get(name);
            uniformfun.run(this, location, name);

        });

    }

    void connectAllTexture() {
        if (mesh.texturesFunction == null) return;
        mesh.texturesFunction.iterateKeyValue((String name, IUniformFunction uniformfun) -> {
            int location = mesh.texturesLocation.get(name);
            uniformfun.run(this, location, name);
        });
    }

    public void calculateMatrix() {
        parentMatrix.identity().mul(parent.getParentMatrix()).mul(transformMatrix).mul(operateMatrix);
        modelMatrix.identity().mul(parentMatrix).mul(matrix);
    }


    public IParent parent;
    public Raw childs = new Raw("obj childs");

    @Override
    public void addChild(IChild obj) {
        childs.add(obj.getName(), obj);

    }

    @Override
    public void removeChild(IChild obj) {
        childs.remove(obj.getName());
    }

    @Override
    public Raw getChilds() {
        return childs;
    }

    @Override
    public void setParent(IParent parent) {
        this.parent = parent;
    }

    @Override
    public String getParentName() {
        String parentName = raw.get("parent");
        if (parentName == null)
            parentName = "canvas";
        return parentName;
    }


    public IUpdate getUpdateFun() {
        return updateCallBack;
    }


    String createFrom;

    public void setCreateFrom(String createFrom) {
        this.createFrom = createFrom;
    }


    public String getCreateFrom() {
        return createFrom;
    }
//    @Override
//    public void update(float interval) {
//        updateCallBack.update(interval, this);
//    }


    public void setPos(float x, float y, float z) {
        operateMatrix.identity();
        operateMatrix.translate(x, y, z);
    }

    public void attachParent(IParent obj) {
        obj.addChild(this);
        setParent(obj);
    }

    public static Matrix4f MVPMatrix = new Matrix4f();

    public Matrix4f getMVPMatrix() {
        MVPMatrix.set(camera.matrix).mul(modelMatrix);
        return MVPMatrix;
    }

    Vector4f uColor = new Vector4f();

    public void setUColor(Vector4f color) {
        uColor.x = color.x;
        uColor.y = color.y;
        uColor.z = color.z;
        uColor.w = color.w;
    }

    public Vector4f getUColor() {
        return uColor;
    }
}

