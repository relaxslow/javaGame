package engine.Util;

public class Error {
    public static void fatalError(Exception e, String msg) {
        System.out.println(msg);
        e.printStackTrace();
        System.exit(-1);
    }

}
