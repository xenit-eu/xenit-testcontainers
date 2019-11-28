package eu.xenit.docker.testcontainers;

import eu.xenit.docker.testcontainers.util.ImageUtils;
import eu.xenit.docker.testcontainers.util.JdbcDatabaseContainerUtils;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import lombok.NonNull;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;

public class AlfrescoContainer<SELF extends AlfrescoContainer<SELF>> extends GenericContainer<SELF> {

    public enum Edition {
        COMMUNITY, ENTERPRISE
    }

    private static final String IMAGE_COMMUNITY = "docker.io/xenit/alfresco-repository-community";
    private static final String IMAGE_ENTERPRISE = "hub.xenit.eu/alfresco-enterprise/alfresco-repository-enterprise";

    public static final int ALFRESCO_PORT = 8080;
    public static final int ALFRESCO_SSL_PORT = 8443;
    public static final int ALFRESCO_DEBUG_PORT = 8000;

    private Network network = Network.newNetwork();

    private SolrContainer solrContainer;

    private JdbcDatabaseContainer<?> databaseContainer;

    private String alfrescoVersion;

    public AlfrescoContainer(@NonNull Edition edition, @NonNull String version) {
        this((Edition.COMMUNITY.equals(edition) ? IMAGE_COMMUNITY : IMAGE_ENTERPRISE) + ":" + version);
    }

    public AlfrescoContainer(@NonNull String dockerImageName) {
        this(dockerImageName, ImageUtils.extractVersion(dockerImageName));
    }

    public AlfrescoContainer(@NonNull String dockerImage, @NonNull String alfrescoVersion) {
        super(dockerImage);

        withNetwork(network);

        this.alfrescoVersion = alfrescoVersion;

        // Alfresco cannot run without a DB: we provide PostgresSQL as a default
        withDatabase(defaultDatabase());
    }

    /**
     * In principle this can work with any database, as long as the DB driver is available in the image. TODO maybe it
     * is possible to include the DB driver automatically based on the DB used?
     */
    public SELF withDatabase(JdbcDatabaseContainer<?> database) {
        dependsOn(database);
        database.withNetwork(network);
        this.databaseContainer = database;
        return self();
    }

    public SELF withSolr(SolrContainer solr) {
        solr.dependsOn(this);
        solr.withNetwork(network);
        addExposedPort(ALFRESCO_SSL_PORT);
        this.solrContainer = solr;
        return self();
    }

    public SELF withDebug() {
        final String address =
                ((alfrescoVersion.startsWith("6") && !alfrescoVersion.startsWith("6.0")) ? "*:" : "")
                        + ALFRESCO_DEBUG_PORT;
        withEnv("CATALINA_OPTS",
                "-agentlib:jdwp=transport=dt_socket,address=" + address + ",server=y,suspend=n");
        addExposedPort(ALFRESCO_DEBUG_PORT);
        return self();
    }

    @Override
    protected void configure() {
        this.setJavaOptsEnv();

        addExposedPort(ALFRESCO_PORT);
        waitingFor(Wait.forHttp("/alfresco").withStartupTimeout(Duration.ofSeconds(300)));
    }

    private void setJavaOptsEnv() {
        Map<String, String> javaOpts = new HashMap<>();
        javaOpts.put("messaging.subsystem.autoStart", Boolean.toString(false));
        includeDBOpts(javaOpts);
        includeSolrOpts(javaOpts);

        StringBuilder javaOptsValueBuilder = new StringBuilder();
        javaOpts.forEach((key, value) -> {
            javaOptsValueBuilder.append("-D")
                    .append(key)
                    .append("=")
                    .append(value)
                    .append(" ");
        });
        withEnv("JAVA_OPTS", javaOptsValueBuilder.toString());
    }

    private void includeDBOpts(final Map<String, String> javaOpts) {
        javaOpts.put("db.name", databaseContainer.getDatabaseName());
        javaOpts.put("db.driver", databaseContainer.getDriverClassName());
        javaOpts.put("db.host", databaseContainer.getNetworkAliases().get(0));
        javaOpts.put("db.port",
                Integer.toString(JdbcDatabaseContainerUtils.getInternalDatabasePort(databaseContainer)));
        javaOpts.put("db.username", databaseContainer.getUsername());
        javaOpts.put("db.password", databaseContainer.getPassword());
        javaOpts.put("db.url", JdbcDatabaseContainerUtils.getInternalJdbcUrl(databaseContainer));
    }

    private void includeSolrOpts(final Map<String, String> javaOpts) {
        if (solrContainer == null) {
            javaOpts.put("index.subsystem.name", "noindex");
        } else {
            javaOpts.put("index.subsystem.name", solrContainer.getVersion().getName());
            javaOpts.put("solr.host", solrContainer.getNetworkAliases().get(0));
            javaOpts.put("solr.port", Integer.toString(SolrContainer.SOLR_PORT));
            javaOpts.put("solr.port.ssl", Integer.toString(SolrContainer.SOLR_SSL_PORT));
            javaOpts.put("solr.secureComms", "https");
        }
    }

    public SolrContainer getSolrContainer() {
        return solrContainer;
    }

    public JdbcDatabaseContainer getDatabaseContainer() {
        return databaseContainer;
    }

    private static JdbcDatabaseContainer<?> defaultDatabase() {
        return new PostgreSQLContainer<>("docker.io/xenit/postgres:latest")
                .withDatabaseName("alfresco")
                .withUsername("alfresco")
                .withPassword("admin");
    }
}
