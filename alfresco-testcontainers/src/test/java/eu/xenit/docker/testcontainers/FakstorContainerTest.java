package eu.xenit.docker.testcontainers;

import static eu.xenit.docker.testcontainers.util.ContainerTestUtil.startValidateAndStop;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

import org.junit.jupiter.api.Test;

class FakstorContainerTest {

    @Test
    void defaultImage() {
        runAndValidateContainer(new FakstorContainer());
    }

    @Test
    void customImage() {
        runAndValidateContainer(new FakstorContainer("hub.xenit.eu/public/fakstor:latest"));
    }


    private void runAndValidateContainer(final FakstorContainer fakstorContainer) {
        startValidateAndStop(fakstorContainer, this::validateFakstorContainer);
    }

    private void validateFakstorContainer(final FakstorContainer container) {
        assertThat(container.isRunning(), is(true));
        assertThat(container.getMappedPort(FakstorContainer.SWARM_PORT), is(not(nullValue())));
    }


}