package engine.Buffer;

import engine.Util.Raw;

public class VBORaw extends Raw {
    VBORaw(String name) {
        super(name);
    }

    public void setDataAmount(String name, int number) {

        VBOAttribute attributes = get("attributes");
        if (attributes == null) {
            attributes = new VBOAttribute("VBOAttribute");
            add("attributes", attributes);

        }
        attributes.add(name, number);
    }

}
