package engine.Buffer;

import engine.Util.Raw;

public class VBORaw extends Raw {
    public void setDataAmount(String name, int number) throws Exception {

        VBOAttribute attributes = get("attributes");
        if (attributes == null) {
            attributes = new VBOAttribute();
            add("attributes", attributes);

        }
        attributes.add(name, number);
    }

}
