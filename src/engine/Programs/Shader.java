package engine.Programs;

import engine.Interface.IHasName;
import engine.Interface.INeedClean;
import engine.Interface.INeedCreate;
import engine.Interface.InputProperty;
import engine.Util.Error;
import engine.Util.Raw;
import engine.Util.Res;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import static org.lwjgl.opengl.GL20.*;

public class Shader extends Res implements INeedCreate, INeedClean {
    public static String loadFromAbsolutePath(String fileName) {
        String result = null;
        try {

            InputStream is = new FileInputStream(fileName);
            Scanner scanner = new Scanner(is, "UTF-8");
            result = scanner.useDelimiter("\\A").next();
            scanner.close();
            return result;
        } catch (FileNotFoundException e) {
            Error.fatalError(e, "error in load file" + fileName);
        }
        return result;
    }

    public static String loadFromRelatedPath(String fileName) {
        String result = null;
        try (InputStream in = Shader.class.getResourceAsStream(fileName);
             Scanner scanner = new Scanner(in, "UTF-8")) {
            result = scanner.useDelimiter("\\A").next();
        } catch (IOException e) {
            Error.fatalError(e, "error in load file " + fileName);
        }
        return result;
    }

    //
    int id;


    Raw raw = new Raw("shader raw data");

    public Shader(InputProperty<Raw> input) {
        input.run(raw);

    }

    public Shader(int type, String name) {
        this.name = name;
        String src = Shader.loadFromRelatedPath(name);
        id = glCreateShader(type);
        if (id == 0) {
            Error.fatalError(new Exception("Error creating shader. Type: " + type), null);
        }
        glShaderSource(id, src);
        glCompileShader(id);

        if (glGetShaderi(id, GL_COMPILE_STATUS) == 0) {
            Error.fatalError(new Exception("Error compiling engine.Shader code: " + glGetShaderInfoLog(id, 1024)), null);
        }
        canvas.allRes.add(name, this);
    }

    @Override
    public void create() {
        name = raw.getX("name");
        int type = raw.getX("type");

//        String src = engine.Shader.loadFromAbsolutePath(file);

        String src = Shader.loadFromRelatedPath(name);
        id = glCreateShader(type);
        if (id == 0) {
            Error.fatalError(new Exception("Error creating shader. Type: " + type), null);
        }

        glShaderSource(id, src);
        glCompileShader(id);

        if (glGetShaderi(id, GL_COMPILE_STATUS) == 0) {
            Error.fatalError(new Exception("Error compiling engine.Shader code: " + glGetShaderInfoLog(id, 1024)), null);
        }

    }

    @Override
    public void clean() {
        glDeleteShader(id);
    }


}