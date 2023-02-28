package org.moderd.core;

import java.util.Collections;
import java.util.List;

public final class ModelContainer {

    private List<Class<?>> classesWithModelAnnotation;

    ModelContainer(List<Class<?>> classesWithModelAnnotation) {
        this.classesWithModelAnnotation = Collections.unmodifiableList(classesWithModelAnnotation);
    }

    public List<Class<?>> getClassesWithModelAnnotation() {
        return classesWithModelAnnotation;
    }
}
