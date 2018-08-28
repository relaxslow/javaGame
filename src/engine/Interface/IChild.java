package engine.Interface;

public interface IChild {
    String getName();

    void setParent(IParent parent);

    String getParentName();

    void calculateMatrix();
}
