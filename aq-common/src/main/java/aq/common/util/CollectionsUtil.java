package aq.common.util;


import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class CollectionsUtil {

    public static void listMapSort(List data,String key) {
        Collections.sort(data, new Comparator<Map<String, Object>>() {
            public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                Integer name1 = Integer.valueOf(o1.get(key).toString()) ;//name1是从你list里面拿出来的一个
                Integer name2 = Integer.valueOf(o2.get(key).toString()) ; //name1是从你list里面拿出来的第二个name
                return name1.compareTo(name2);
            }
        });
    }

}
