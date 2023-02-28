package org.moderd.core;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.FileSystemNotFoundException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;
import java.util.stream.Stream;
import org.moderd.core.annotation.Model;

public final class AnnotationBasedScanner {

    private static final Logger log = Logger.getLogger("org.moderd.core.AnnotationBasedScanner");

    private static final String EXTENSION = ".class";
    private static final char DOT = '.';
    private static final char MAC_SEPARATOR = '/';
    private static final char WINDOW_SEPARATOR = '\\';
    private static final String JAR_PREFIX = "jar:";
    private static final String DOT_ASTERISK = ".*";

    static List<Class<?>> getAllClassesFromBasePackage(String pkgName) throws IOException, URISyntaxException {

        if (pkgName != null && pkgName.endsWith(DOT_ASTERISK)) {
            pkgName = pkgName.replace(DOT_ASTERISK, "");
        }

        final String replacedPkgName = pkgName;
        final String pkgPath = replacedPkgName.replace(DOT, MAC_SEPARATOR);
        final URI pkg = Objects.requireNonNull(ClassLoader.getSystemClassLoader().getResource(pkgPath)).toURI();

        Path root = getRootPath(pkgPath, pkg);

        return getAllClassesWithAnnotation(replacedPkgName, root, Model.class);
    }

    private static List<Class<?>> getAllClassesWithAnnotation(
        String replacedPkgName, Path root, Class<? extends Annotation> annotation) {
        final List<Class<?>> allClasses = new ArrayList<>();
        try (final Stream<Path> allPaths = Files.walk(root)) {
            allPaths.filter(Files::isRegularFile).forEach(file -> {
                try {
                    final String path = file.toString().replace(WINDOW_SEPARATOR, DOT).replace(MAC_SEPARATOR, DOT);
                    final String name = path.substring(path.indexOf(replacedPkgName), path.length() - EXTENSION.length());
                    Class<?> clazz = Class.forName(name);

                    if (clazz.isAnnotationPresent(annotation)) {
                        allClasses.add(clazz);
                    }
                } catch (final ClassNotFoundException | StringIndexOutOfBoundsException ignored) {
                    log.warning(ignored.getMessage());
                }
            });
        } catch (IOException e) {
            log.severe(e.getMessage());
        }

        return allClasses;
    }

    private static Path getRootPath(String pkgPath, URI pkg) throws IOException {
        Path root;
        if (pkg.toString().startsWith(JAR_PREFIX)) {
            try {
                root = FileSystems.getFileSystem(pkg).getPath(pkgPath);
            } catch (final FileSystemNotFoundException e) {
                root = FileSystems.newFileSystem(pkg, Collections.emptyMap()).getPath(pkgPath);
            }
        } else {
            root = Paths.get(pkg);
        }
        return root;
    }
}
