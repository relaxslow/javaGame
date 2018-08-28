package engine.Meshes;

import engine.Buffer.IBO_D;
import engine.Buffer.VBO;
import engine.Buffer.VBORaw;
import engine.Buffer.VBO_D;

import engine.Interface.InputProperty;

import engine.Util.Raw;
import engine.Util.Tools;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.GL_LINES;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
class Mtl {
    Vector3f diffuse;

}
class Index {
    int pos;// in verteice
    int texCoord;
    int normal;
    Mtl mtl;

    Index(String line) {
        String[] indexArr = line.split("/");
        int length = indexArr.length;
        pos = Integer.parseInt(indexArr[0]) - 1;
        if (length > 1) {
            // It can be empty if the obj does not define text coords
            String textCoord = indexArr[1];
            texCoord = textCoord.length() > 0 ? Integer.parseInt(textCoord) - 1 : -1;
            if (length > 2) {
                normal = Integer.parseInt(indexArr[2]) - 1;
            }
        }

    }
}

public class FileMesh extends ElementMesh {
    static int fileIndex=0;
    public FileMesh(InputProperty<Raw> input) throws Exception {
        input.run(raw);
        fileIndex++;
        name = raw.getX("name");
        parseMtl(name);
        parseModel(name);
    }

    @Override
    public void create(Raw res) throws Exception {
        float[] vertexTexCoords = raw.get("vertexTexCoords");
        if (vertexTexCoords == null)
            program = res.getX("Program-modelNoTex");
        else
            program = res.getX("Program-model");
        getPrimitiveType();
        attributesLocation = program.attributes;
        getAttribute();
        getUniform(res);
        getTexture(res);
       generateVAO(true);
        count = ibo.pointNum;
        offset = 0;
    }

    void parseMtl(String fileName) throws Exception {
        try {
            List<String> lines = Tools.readAllLines(fileName + ".mtl");
            Raw mtls = new Raw();
            raw.add("mtls", mtls);
            Mtl currentMtl = null;
            for (String line : lines) {
                String[] tokens = line.split("\\s+");
                switch (tokens[0]) {
                    case "newmtl":
                        currentMtl = new Mtl();
                        mtls.add(tokens[1], currentMtl);
                        break;
                    case "Kd":
                        currentMtl.diffuse = new Vector3f(
                                Float.parseFloat(tokens[1]),
                                Float.parseFloat(tokens[2]),
                                Float.parseFloat(tokens[3]));
                        break;
                    default:
                        break;

                }
            }
        } catch (Exception e) {

        }


    }


    void parseModel(String fileName) throws Exception {
        List<String> lines = Tools.readAllLines(fileName + ".obj");

        List<Vector3f> vertices = new ArrayList<>();
        List<Vector2f> texCoords = new ArrayList<>();
        List<Vector3f> normals = new ArrayList<>();
        List<Index> indexs = new ArrayList<>();
        List<Edge> edges = new ArrayList<>();
        Mtl currentMtl = null;
        for (String line : lines) {
            String[] tokens = line.split("\\s+");
//                engine.Debug.log(tokens.toString());

            switch (tokens[0]) {
                case "v":
                    // Geometric vertex
                    Vector3f pos = new Vector3f(
                            Float.parseFloat(tokens[1]),
                            Float.parseFloat(tokens[2]),
                            Float.parseFloat(tokens[3]));
                    vertices.add(pos);
                    break;
                case "vt":
                    // engine.Texture coordinate
                    Vector2f coords = new Vector2f(
                            Float.parseFloat(tokens[1]),
                            Float.parseFloat(tokens[2]));
                    texCoords.add(coords);
                    break;
                case "vn":
                    // Vertex normal
                    Vector3f normal = new Vector3f(
                            Float.parseFloat(tokens[1]),
                            Float.parseFloat(tokens[2]),
                            Float.parseFloat(tokens[3]));
                    normals.add(normal);
                    break;
                case "f":
                    if (tokens.length == 5) {
                        Index i0 = new Index(tokens[1]);
                        Index i1 = new Index(tokens[2]);
                        Index i2 = new Index(tokens[3]);
                        Index i3 = new Index(tokens[4]);
                        if (currentMtl != null) {
                            i0.mtl = i1.mtl = i2.mtl = i3.mtl = currentMtl;
                        }
                        indexs.add(i0);
                        indexs.add(i1);
                        indexs.add(i2);
                        indexs.add(i2);
                        indexs.add(i3);
                        indexs.add(i0);
                        addEdge(edges, new Edge(i0.pos, i1.pos));
                        addEdge(edges, new Edge(i1.pos, i2.pos));
                        addEdge(edges, new Edge(i2.pos, i3.pos));
                        addEdge(edges, new Edge(i3.pos, i0.pos));

                    } else if (tokens.length == 4) {
                        Index i0 = new Index(tokens[1]);
                        Index i1 = new Index(tokens[2]);
                        Index i2 = new Index(tokens[3]);
                        if (currentMtl != null) {
                            i0.mtl = i1.mtl = i2.mtl = currentMtl;
                        }
                        indexs.add(i0);
                        indexs.add(i1);
                        indexs.add(i2);
                        addEdge(edges, new Edge(i0.pos, i1.pos));
                        addEdge(edges, new Edge(i1.pos, i2.pos));
                        addEdge(edges, new Edge(i2.pos, i0.pos));

                    } else
                        throw new Exception("model can only contain 4 side or 3 side polygon");

                    break;
                case "usemtl":
                    Raw mtls = raw.getX("mtls");
                    currentMtl = mtls.getX(tokens[1]);
                    break;
                default:// Ignore other lines
                    break;
            }
        }


        int pointNum = indexs.size();
        int[] triangleIndice = new int[pointNum];
        float[] vertexPos = new float[pointNum * 3];
        float[] vertexColor = new float[pointNum * 3];
        float[] vertexTexCoords = null;
        if (texCoords.size() != 0) {
            vertexTexCoords = new float[pointNum * 2];
        }

        float[] vertexNormal = new float[pointNum * 3];

        for (int i = 0; i < pointNum; i++) {
            Index index = indexs.get(i);
            triangleIndice[i] = i;
            for (int j = 0; j < 3; j++) {
                vertexPos[i * 3 + j] = vertices.get(index.pos).get(j);
                if (index.mtl != null)
                    vertexColor[i * 3 + j] = index.mtl.diffuse.get(j);
                else
                    vertexColor[i * 3 + j] = 1.0f;
                vertexNormal[i * 3 + j] = normals.get(index.normal).get(j);
            }
            if (vertexTexCoords != null) {
                for (int j = 0; j < 2; j++) {
                    vertexTexCoords[i * 2 + j] = texCoords.get(index.texCoord).get(j);
                }
            }


        }

        int[] lineIndice = new int[edges.size() * 2];
        for (int i = 0, j = 0; i < edges.size(); i++, j += 2) {
            int indexInVertex = edges.get(i).i1;
            lineIndice[j] = getPosInVertexPosBuffer(indexInVertex, indexs);
            indexInVertex = edges.get(i).i2;
            lineIndice[j + 1] = getPosInVertexPosBuffer(indexInVertex, indexs);
        }

        raw.add("lineIndice", lineIndice);
        raw.add("vertexPos", vertexPos);
        raw.add("vertexColor", vertexColor);
        if (vertexTexCoords != null)
            raw.add("vertexTexCoords", vertexTexCoords);
        raw.add("vertexNormal", vertexNormal);
        raw.add("pointNum", pointNum);
        raw.add("triangleIndice", triangleIndice);
//        engine.Debug.log(indice.toString());


    }

    int getPosInVertexPosBuffer(int indexInVertexs, List<Index> indexs) throws Exception {
        for (int i = 0; i < indexs.size(); i++) {
            if (indexInVertexs == indexs.get(i).pos) {
                return i;
            }
        }
        throw new Exception("can't find vertex index in vertex position raw");
    }

    void addEdge(List<Edge> edgeGroup, Edge edge) {
        boolean alreadyInGroup = false;
        for (int i = 0; i < edgeGroup.size(); i++) {
            if (edge.equals(edgeGroup.get(i))) {
                alreadyInGroup = true;
                break;
            }
        }
        if (!alreadyInGroup)
            edgeGroup.add(edge);
    }

    class Edge {
        int i1;
        int i2;

        Edge(int i1, int i2) {
            this.i1 = i1;
            this.i2 = i2;
        }

        @Override
        public boolean equals(Object obj) {
            Edge other = (Edge) obj;
            if ((this.i1 == other.i1 && this.i2 == other.i2) || (this.i1 == other.i2 && this.i2 == other.i1))
                return true;
            return false;
        }
    }

    void getAttribute() throws Exception {
        int pointNum = raw.getX("pointNum");
        float[] vertexNormal = raw.getX("vertexNormal");
        vbos = new Raw();
        float[] vertexPos = raw.getX("vertexPos");
        vbos.add("a_Position", new VBO_D((VBORaw positionData) -> {
            positionData.add("pointNum", pointNum);
            positionData.setDataAmount("a_Position", 3);
            positionData.add("raw", vertexPos);
        }));
        float[] vertexColor = raw.getX("vertexColor");
        vbos.add("a_Color", new VBO_D((VBORaw positionData) -> {
            positionData.add("pointNum", pointNum);
            positionData.setDataAmount("a_Color", 3);

            positionData.add("raw", vertexColor);

        }));
        float[] vertexTexCoords = raw.get("vertexTexCoords");
        if (vertexTexCoords != null) {
            vbos.add("a_TexCoords", new VBO_D((VBORaw texCoordsData) -> {
                texCoordsData.add("pointNum", pointNum);
                texCoordsData.setDataAmount("a_TexCoords", 2);
                texCoordsData.add("raw", vertexTexCoords);

            }));
        }


        ibo = new IBO_D((Raw iboRaw) -> {
            iboRaw.add("name", "fileModel"+fileIndex+"IBO");

            if (primitiveType == GL_TRIANGLES)
                iboRaw.add("raw", raw.getX("triangleIndice"));
            else if (primitiveType == GL_LINES)
                iboRaw.add("raw", raw.getX("lineIndice"));
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

