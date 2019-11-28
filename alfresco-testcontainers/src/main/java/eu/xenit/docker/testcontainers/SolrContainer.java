package eu.xenit.docker.testcontainers;

import java.time.Duration;
import lombok.NonNull;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;

public class SolrContainer extends GenericContainer<SolrContainer> {

    public static final Integer SOLR_PORT = 8080;
    public static final Integer SOLR_SSL_PORT = 8443;

    private Version version;

    public SolrContainer(@NonNull String dockerImageName, Version version) {
        super(dockerImageName);
        this.version = version;
    }

    @Override
    protected void configure() {
        addExposedPort(SOLR_PORT);
        addExposedPort(SOLR_SSL_PORT);

        if (Version.SOLR6.equals(version)) {
            // Solr 6 image do not (yet) contain a healthcheck
            waitingFor(
                    Wait.forLogMessage(".*o.e.j.s.Server Started.*\\n", 1).withStartupTimeout(Duration.ofSeconds(120)));
        } else {
            waitingFor(Wait.forHealthcheck().withStartupTimeout(Duration.ofSeconds(120)));
        }
    }

    public Version getVersion() {
        return version;
    }

    public enum Version {
        SOLR1("solr"), SOLR4("solr4"), SOLR6("solr6");

        Version(String name) {
            this.name = name;
        }

        private String name;

        public String getName() {
            return name;
        }
    }
}
