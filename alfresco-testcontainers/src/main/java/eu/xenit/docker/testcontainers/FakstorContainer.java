package eu.xenit.docker.testcontainers;

import java.time.Duration;
import lombok.NonNull;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;

public class FakstorContainer extends GenericContainer<FakstorContainer> {

    public static final int SWARM_PORT = 80;

    public FakstorContainer() {
        this("hub.xenit.eu/public/fakstor:latest");
    }

    public FakstorContainer(@NonNull String dockerImageName) {
        super(dockerImageName);
    }

    @Override
    protected void configure() {
        withExposedPorts(SWARM_PORT);
        withEnv("SERVER_PORT", Integer.toString(SWARM_PORT));

        waitingFor(Wait.forListeningPort().withStartupTimeout(Duration.ofSeconds(120)));
    }
}
