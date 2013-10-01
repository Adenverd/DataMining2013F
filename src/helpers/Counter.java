package helpers;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Counter<K> {
    Map<K, Integer> map;

    public Counter(){
        map = new HashMap<K, Integer>();
    }

    public void zero(K key){
        map.put(key, 0);
    }

    public Integer increment(K key){
        if (map.containsKey(key)){
            map.put(key, map.get(key)+1);
        }
        else {
            map.put(key, 1);
        }
        return map.get(key);
    }

    public Integer decrement(K key){
        if (map.containsKey(key)){
            map.put(key, map.get(key)-1);
        }
        else {
            map.put(key, 1);
        }
        return map.get(key);
    }

    public Integer get(K key){
        return map.get(key);
    }

    public void remove (K key){
        map.remove(key);
    }

    public Set<Map.Entry<K, Integer>> entries(){
        return map.entrySet();
    }
}
