package eu.xenit.docker.testcontainers.util;

import java.util.function.Consumer;
import org.testcontainers.containers.GenericContainer;

public class ContainerTestUtil {

    private ContainerTestUtil() {
        // private ctor to hide implicit public one
    }

    public static <T extends GenericContainer> void startValidateAndStop(T container, Consumer<T> validation) {
        try {
            container.start();
            validation.accept(container);
        } finally {
            container.stop();
            for (Object dependency : container.getDependencies()) {
                if (dependency instanceof GenericContainer) {
                    ((GenericContainer) dependency).stop();
                }
            }
        }
    }
}
