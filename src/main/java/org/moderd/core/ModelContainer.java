package org.moderd.core;

import java.util.Collections;
import java.util.List;

/**
 * bean
 */
public class ModelContainer {

    private final List<Class<?>> classesWithModelAnnotation;

    public ModelContainer(List<Class<?>> classesWithModelAnnotation) {
        this.classesWithModelAnnotation = Collections.unmodifiableList(classesWithModelAnnotation);
    }

    public List<Class<?>> getClassesWithModelAnnotation() {
        return classesWithModelAnnotation;
    }
}
