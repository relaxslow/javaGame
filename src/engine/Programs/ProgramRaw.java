package engine.Programs;


import engine.Util.Raw;

public class ProgramRaw extends Raw {
    public ProgramRaw(String name) {
        super(name);
    }

    void addFragData(String name, int location) {
        Raw fragDatas = get("fragDatas");
        if (fragDatas == null) {
            fragDatas = new Raw("fragDatas in ProgramRaw");
            add("fragDatas", fragDatas);
        }

        fragDatas.add(name, location);
    }

    Integer getFragData(String name) {
        Raw fragDatas = get("fragDatas");
        if (fragDatas == null) {
            return null;
        } else {
            Integer fragdata = fragDatas.get(name);
            if (fragdata == null) {
                return null;
            } else return fragdata;
        }


    }
}