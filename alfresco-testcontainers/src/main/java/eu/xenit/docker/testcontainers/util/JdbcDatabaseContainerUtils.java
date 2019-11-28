package eu.xenit.docker.testcontainers.util;

import org.testcontainers.containers.JdbcDatabaseContainer;

public class JdbcDatabaseContainerUtils {

    private JdbcDatabaseContainerUtils() {
        // private ctor to hide implicit public one
    }

    public static String getInternalJdbcUrl(JdbcDatabaseContainer<?> databaseContainer) {
        String ret = databaseContainer.getJdbcUrl();
        ret = ret.replace(databaseContainer.getContainerIpAddress(), databaseContainer.getNetworkAliases().get(0));

        for (Integer exposedPort : databaseContainer.getExposedPorts()) {
            if (exposedPort == null) {
                continue;
            }
            ret = ret.replaceAll(Integer.toString(databaseContainer.getMappedPort(exposedPort)),
                    Integer.toString(exposedPort));
        }

        return ret;
    }

    public static int getInternalDatabasePort(JdbcDatabaseContainer<?> databaseContainer) {
        final String externalJdbcUrl = databaseContainer.getJdbcUrl();

        for (Integer exposedPort : databaseContainer.getExposedPorts()) {
            if (exposedPort == null) {
                continue;
            }
            if (externalJdbcUrl.contains(":" + databaseContainer.getMappedPort(exposedPort))) {
                return exposedPort;
            }
        }

        return -1;
    }
}