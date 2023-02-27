package org.moderd.core;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.logging.Logger;

public class InitializingExecutor {

    private static final Logger log = Logger.getLogger("org.moderd.core.InitializingExecutor");

    private ModelContainer modelContainer;

    public InitializingExecutor(final String basePackage) {
        this.executeLoadAllClasses(basePackage);
    }

    private void executeLoadAllClasses(String basePackage) {
        try {
            List<Class<?>> allClassesFromBasePackage = AnnotationBasedScanner.getAllClassesFromBasePackage(basePackage);
            modelContainer = new ModelContainer(allClassesFromBasePackage);
        } catch (IOException | URISyntaxException e) {
            log.severe(e.getMessage());
        }
    }

    public ModelContainer getModelContainer() {
        return modelContainer;
    }
}
