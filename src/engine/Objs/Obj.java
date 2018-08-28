package engine.Objs;

import engine.Interface.*;
import engine.Meshes.Mesh;
import engine.Meshes.NullMesh;
import engine.Textures.Texture;
import engine.Util.Raw;
import engine.View.Camera;
import org.joml.Matrix4f;

public class Obj extends SceneObject implements
        IRenderable,
        IParent,
        IChild,
        INeedCreate,
        INeedClean,
        ICanUpdate {
    public Obj() {
    }

    public Obj(InputProperty<Raw> input) throws Exception {
        input.run(raw);
        createName();
    }


    public Mesh mesh;
    public NullMesh originMesh;


    public Matrix4f modelMatrix = new Matrix4f(); //combine by parent|transform|operate|matrix

    Matrix4f parentMatrix = new Matrix4f();//accumulate from parent
    Matrix4f transformMatrix = new Matrix4f();//clear every frame, better use to animate object
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
    public Canvas canvas;

    public void createName() {
        name = raw.get("name");
        if (name == null) {
            name = "obj" + index;
        }
        index++;
    }


    public void create(Raw res) throws Exception {
        mesh = res.getX(raw.getX("meshName"));
        canvas = res.get("canvas");
        attachCamera(res);
        attachCallbacks();
        attachCustomTexture(res);
        calculateMatrix();
    }


   

    void attachCamera(Raw res) {
        camera = raw.get("camera");
        if (camera == null) ;
        camera = canvas.camera;
    }

    public void attachCallbacks() throws Exception {
        initCallBack = raw.get("init");
        if (initCallBack != null) initCallBack.init(this);
        inputCallBack = raw.get("input");
        updateCallBack = raw.get("update");

    }

    public IInput getInputCallBack() {
        return inputCallBack;
    }

    
    public void attachCustomTexture(Raw res) throws Exception {
        Raw defaultTextures = mesh.textures;
        if (defaultTextures == null) return;
        finalTextures = new Raw();
        defaultTextures.iterateKeyValueX((String name, Texture defaultTexture) -> {
            Raw customTextures = raw.get("textures");
            if (customTextures == null) {
                finalTextures.add(name, defaultTexture);
            } else {
                String customTextureName = customTextures.get(name);
                Texture texture = res.getX(customTextureName);
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

    @Override
    public void removeFromRenderGroups() {
        if (mesh != null) {
            removeFromGroup(canvas.renderGroupsNormal, mesh.program.name);
        }
        if (originMesh != null) {
            removeFromGroup(canvas.renderGroupOrigin, originMesh.program.name);
        }
     
    }

    void addToGroup(Raw renderGroups, String programName) throws Exception {
        Raw group = renderGroups.get(programName);
        if (group == null) {
            group = new Raw();
            renderGroups.add(programName, group);
        }
        group.add(this.name, this);
    }

    @Override
    public void addToRenderGroups() throws Exception {
        if (mesh != null) {
            addToGroup(canvas.renderGroupsNormal, mesh.program.name);
        }

        if (originMesh != null) {
            addToGroup(canvas.renderGroupOrigin, originMesh.program.name);
        }
      
    }

    @Override
    public void render() {
//        glUseProgram(mesh.program.id);
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


    IParent parent;
    Raw childs = new Raw();

    @Override
    public void addChild(IChild obj) throws Exception {
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


    @Override
    public IUpdate getUpdateFun() {
        return updateCallBack;
    }

//    @Override
//    public void update(float interval) {
//        updateCallBack.update(interval, this);
//    }


}

