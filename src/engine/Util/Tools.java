package engine.Util;


import org.joml.Vector3f;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Tools {
    public static float[] listToArray(List<Float> list) {
        int size = list != null ? list.size() : 0;
        float[] floatArr = new float[size];
        for (int i = 0; i < size; i++) {
            floatArr[i] = list.get(i);
        }
        return floatArr;
    }

    public static List<String> readAllLines(String fileName) throws Exception {
        List<String> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(Tools.class.getResourceAsStream(fileName)))) {
            String line;
            while ((line = br.readLine()) != null) {
                list.add(line);
            }
        }
        return list;
    }

    public static void roundFloatVec(Vector3f v, int p) {
        v.x = roundFloat(v.x, p);
        v.y = roundFloat(v.y, p);
        v.z = roundFloat(v.z, p);
    }

    public static void roundFloatVec(Vector3f v, int p, Vector3f result) {
        result.x = roundFloat(v.x, p);
        result.y = roundFloat(v.y, p);
        result.z = roundFloat(v.z, p);
    }

    public static float roundFloat(float value, int places) {
        float scale = (float) Math.pow(10, places);
        float result = Math.round(value * scale) / scale;
        return result;
    }

    public static boolean equal2RoundedFloat(float f1, float f2, int places) {
        if (roundFloat(f1, places) == roundFloat(f2, places)) return true;
        return false;

    }

    public static float getRandomNumber(float max, float min) {
        return (float) ((Math.random() * ((max - min))) + min);
    }

    public static int getRandomIntNumber(int max, int min) {
        return (int) ((Math.random() * ((max - min))) + min);
    }
}