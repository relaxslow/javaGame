package engine.Interface;

import org.joml.Matrix4f;

import engine.Util.Raw;
import org.joml.Vector3f;
import org.joml.Vector4f;

public interface IParent {
    void addChild(IChild obj) throws Exception;

    void removeChild(IChild obj);

    Raw getChilds();

    Matrix4f getParentMatrix();
    


    IParent getParent();
}
