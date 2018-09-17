package engine.Physics;

import engine.Buffer.IBO_D;
import engine.Buffer.VBO;
import engine.Buffer.VBORaw;
import engine.Buffer.VBO_D;
import engine.Meshes.ElementMesh;
import engine.Util.Error;
import engine.Util.Raw;
import engine.Util.Tools;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.GL_LINES;

public class BoundingMesh extends ElementMesh {
    public ArrayList<Vertex> points;
    public ArrayList<Face> faces;

    public BoundingMesh(String file) {
        this.name = file;
        try {
            parseFile(file);
        } catch (Exception e) {
            Error.fatalError(e, "error parse file" + file);
        }
    }

    private void parseFile(String name) throws Exception {
        List<String> lines = Tools.readAllLines(name + ".obj");
        ArrayList<Vector3f> vertices = new ArrayList<>();
        int[] verticeIndice = null;
        for (String line : lines) {
            String[] tokens = line.split("\\s+");
            switch (tokens[0]) {
                case "v":
                    // Geometric vertex
                    Vector3f pos = new Vector3f(
                            Float.parseFloat(tokens[1]),
                            Float.parseFloat(tokens[2]),
                            Float.parseFloat(tokens[3]));
                    vertices.add(pos);
                    break;
                case "f":
                    verticeIndice = new int[tokens.length - 1];
                    for (int i = 0; i < verticeIndice.length; i++) {
                        verticeIndice[i] = Integer.parseInt(tokens[i + 1]) - 1;
                    }

                    break;
            }
        }
        storePointAndFaceInfoForCollision(vertices, verticeIndice);

        float[] vertexPos = new float[vertices.size() * 3];
        for (int i = 0; i < vertices.size(); i++) {
            vertexPos[i * 3 + 0] = vertices.get(i).x;
            vertexPos[i * 3 + 1] = vertices.get(i).y;
            vertexPos[i * 3 + 2] = vertices.get(i).z;
        }

        int indicPointNum = faces.size() * 2;
        int[] indice = new int[indicPointNum];
        for (int i = 0; i < faces.size(); i++) {
            indice[i * 2] = faces.get(i).p1.indice;
            indice[i * 2 + 1] = faces.get(i).p2.indice;
        }
        raw.add("indice", indice);
        raw.add("pointNum", vertices.size());
        raw.add("vertexPos", vertexPos);


    }

    private void storePointAndFaceInfoForCollision(ArrayList<Vector3f> vertices, int[] verticeIndice) {
        points = new ArrayList<>();
        faces = new ArrayList<>();
        for (int i = 0; i < verticeIndice.length; i++) {
            points.add(new Vertex(vertices, verticeIndice[i]));
        }
        int faceIndex = 0;
        for (int i = 0; i < points.size(); i++) {
            if (i < points.size() - 1) {
                faces.add(new Face(faceIndex, points.get(i), points.get(i + 1)));
                faceIndex++;
            } else if (i == points.size() - 1) {
                faces.add(new Face(faceIndex, points.get(i), points.get(0)));
                faceIndex++;
            }
        }

    }


    @Override
    public void create() {

        program = canvas.allRes.getX("simple3DProgram");
        primitiveType = GL_LINES;
        attributesLocation = program.attributes;

        getAttribute(canvas.allRes);
        getUniform(canvas.allRes);
        generateVAO(true);
        count = ibo.pointNum;
        offset = 0;
    }

    @Override
    public void getAttribute(Raw res) {

        vbos = new Raw("vbos in BoundingMesh" );

        vbos.add("a_Position", new VBO_D((VBORaw posData) -> {
            int pointNum = raw.getX("pointNum");
            posData.add("pointNum", pointNum);
            posData.setDataAmount("a_Position", 3);
            float[] vertexPos = raw.getX("vertexPos");
            posData.add("raw", vertexPos);
        }));

        ibo = new IBO_D((Raw iboRaw) -> {
            iboRaw.add("raw", raw.getX("indice"));
        });
    }

    @Override
    public void clean() {
        vbos.iterateValue((VBO vbo) -> {
            vbo.clean();
        });
        ibo.clean();
        super.clean();
    }
}



