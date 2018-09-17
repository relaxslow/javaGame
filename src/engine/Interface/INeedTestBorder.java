package engine.Interface;

import org.joml.Matrix4f;

public interface INeedTestBorder extends  IPOOL{
    Matrix4f getModelMatrix();

    boolean isOutOfBorder();
}
