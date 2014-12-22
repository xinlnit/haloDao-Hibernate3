package com.ht.halo.hibernate3.map;

import java.util.Map;

public class HashMap <K,V>
extends java.util.HashMap<K,V>
implements Map<K,V> {

/**
* @Fields serialVersionUID : TODO
*/
private static final long serialVersionUID = -7709853214708221134L;
public HashMap() {
    super();
}
public HashMap(Map<? extends K, ? extends V> map) {
    super(map);
}
public HashMap<K, V>  set(K key, V value) {
            super.put(key, value);
            return this;  
          }
}
