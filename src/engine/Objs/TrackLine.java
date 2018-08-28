package engine.Objs;

import org.joml.Vector4f;

public class TrackLine extends LineSegment3D {
    public TrackLine(String name, Vector4f color) throws Exception {
        super(name, color);
    }

    @Override
    public void addToRenderGroups() throws Exception {
        addToGroup(canvas.renderGroupTrackLine,mesh.program.name);
    }
}
