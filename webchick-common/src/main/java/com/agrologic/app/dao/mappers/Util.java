package com.agrologic.app.dao.mappers;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class Util {

    public static <T> Collection getUniqueElements(Collection<T> elements) {
        Set<T> uniqueElements = new HashSet<T>();
        for (T e : elements) {
            uniqueElements.add(e);
        }
        return uniqueElements;
    }
}
