package engine;

import org.joml.Vector3f;

public class Light {
    String name;
    int type;
    Vector3f color;
    Vector3f direct;

    Light(String name) {
        this.name = name;
    }
}
