package engine.Programs;

import engine.Interface.INeedClean;
import engine.Interface.INeedCreate;
import engine.Interface.InputProperty;
import engine.Util.Raw;
import engine.Util.Res;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Scanner;

import static org.lwjgl.opengl.GL20.*;

public class Shader extends Res implements INeedCreate, INeedClean {
    public static String loadFromAbsolutePath(String fileName) throws Exception {
        String result;
        InputStream is = new FileInputStream(fileName);
        Scanner scanner = new Scanner(is, "UTF-8");
        result = scanner.useDelimiter("\\A").next();
        scanner.close();
        return result;
    }

    public static String loadFromRelatedPath(String fileName) throws Exception {
        String result;
        try (InputStream in = Shader.class.getResourceAsStream(fileName);
             Scanner scanner = new Scanner(in, "UTF-8")) {
            result = scanner.useDelimiter("\\A").next();
        }
        return result;
    }

    //
    int id;


    Raw raw = new Raw();

    public Shader(InputProperty<Raw> input) throws Exception {
        input.run(raw);

    }

    @Override
    public void create(Raw allRes) throws Exception {
        name = raw.getX("name");
        int type = raw.getX("type");

//        String src = engine.Shader.loadFromAbsolutePath(file);

        String src = Shader.loadFromRelatedPath(name);
        id = glCreateShader(type);
        if (id == 0) {
            throw new Exception("engine.Util.Error creating shader. Type: " + type);
        }

        glShaderSource(id, src);
        glCompileShader(id);

        if (glGetShaderi(id, GL_COMPILE_STATUS) == 0) {
            throw new Exception("engine.Util.Error compiling engine.Shader code: " + glGetShaderInfoLog(id, 1024));
        }

    }

    @Override
    public void clean() {
        glDeleteShader(id);
    }


}