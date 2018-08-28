package engine.Interface;

public interface IRenderable {
    void removeFromRenderGroups();
    void addToRenderGroups() throws Exception;
    void render();
    void renderOrigin();
    
}
