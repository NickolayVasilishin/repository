package com.epam.nv.processor;

import org.apache.hadoop.fs.Path;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Nikolay_Vasilishin on 5/23/2016.
 */
public class IdSorter {
    private Map<String, Long> idMap;
    private boolean full;
    private long limit;
    private long count;

    //TODO proper limit value
    public IdSorter(long limit) {
        this.limit = limit;
        idMap = new HashMap<>();
//        Map<String, Long> m = new HashMap<>();
//        m.entrySet().stream().sorted((o1, o2) -> o2.getValue().compareTo(o1.getValue())).collect(Collectors.toCollection(idMap));
    }

    public void put(String id) {
        if (limit == count++) {
            full = true;
        }
        if (idMap.containsKey(id))
            idMap.put(id, idMap.get(id) + 1);
        else
            idMap.put(id, 1L);
    }

    public static List<Map.Entry<String, Long>> sortByValue(Map<String, Long>map) {
        List<Map.Entry<String, Long>> list = new LinkedList<>(map.entrySet());
        Collections.sort(list, (o2, o1) -> o1.getValue().compareTo(o2.getValue()));
        return list;
    }

    /* Stream<Map.Entry<K,V>> sorted =
    map.entrySet().stream()
       .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()));
       */

    public Map<String, Long> getIdMap() {
        full = false;
        count = 0L;
        return andClear();
    }

    private Map<String, Long> andClear() {
        Map<String, Long> map = idMap;
        idMap = new HashMap<>();
        return map;
    }

    public boolean isFull() {
        return full;
    }
}
