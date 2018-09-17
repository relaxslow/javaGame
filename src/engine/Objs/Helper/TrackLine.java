package engine.Objs.Helper;

import engine.Meshes.LineMesh;
import engine.Objs.LineSegment3D;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class TrackLine extends LineSegment3D {
    public TrackLine(String name, Vector4f color)  {
        this.name = name;
        setObjColor(color);
        camera = canvas.camera;
        points = new Vector3f[2];
        points[0] = new Vector3f();
        points[1] = new Vector3f();
        mesh = new LineMesh(
                "lineMesh" + LineMesh.lineIndex,
                getFloatArr(points),
                points.length,
                getIntArr(points)
        );
        attachParent(canvas);
        addToRenderGroups();

    }

    @Override
    public void addToRenderGroups()  {
        addToGroup(canvas.renderGroup_TrackLine,mesh.program.name);
    }
}
