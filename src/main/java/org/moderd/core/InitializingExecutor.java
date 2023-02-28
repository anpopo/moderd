package org.moderd.core;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.logging.Logger;

public final class InitializingExecutor {

    private static final Logger log = Logger.getLogger("org.moderd.core.InitializingExecutor");
    private ModelContainer modelContainer;

    private InitializingExecutor(final String basePackage) {
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

    public static ModelContainer execute(final String basePackage) {
        InitializingExecutor initializingExecutor = new InitializingExecutor(basePackage);
        return initializingExecutor.getModelContainer();
    }

    private ModelContainer getModelContainer() {
        return modelContainer;
    }
}
