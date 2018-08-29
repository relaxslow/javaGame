package test;

import java.util.*;

public class properWayToDeleteWhenIterate {
    public static void main(String[] args) {
        LinkedList<Integer> testarray = new LinkedList<>();
        testarray.add(1);
        testarray.add(2);
        int i = 0;
        while (i < testarray.size()) {
            int ss = testarray.get(i);
            if (ss == 1) {
                testarray.remove(i);
                i--;
            }
            if (ss == 2)
                testarray.add(3);
            i++;
        }
        LinkedHashMap<String, Integer> testHashMap = new LinkedHashMap<>();
        testHashMap.put("a", 1);
        testHashMap.put("b", 2);
        Collection<Integer> values = testHashMap.values();


        Iterator it = testHashMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            if ((int) pair.getValue() == 2)
                testHashMap.put("c", 3);
            if ((int) pair.getValue() == 1)
                testHashMap.remove(pair.getKey());
        }
    }
}
