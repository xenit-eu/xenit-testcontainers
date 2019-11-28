package eu.xenit.docker.testcontainers;

import static eu.xenit.docker.testcontainers.util.ContainerTestUtil.startValidateAndStop;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

import eu.xenit.docker.testcontainers.AlfrescoContainer.Edition;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class AlfrescoContainerTest {

    private static Stream<String> alfrescoImages() {
        return Stream.of(
                "docker.io/alfresco/alfresco-content-repository-community:6.0.7-ga",
//                "docker.io/alfresco/alfresco-content-repository:6.0.0.5",
                "docker.io/alfresco/alfresco-content-repository-community:6.1.2-ga",
//                "docker.io/alfresco/alfresco-content-repository:6.1.1.2",
                "docker.io/alfresco/alfresco-content-repository-community:6.2.0-ga"
//                "docker.io/alfresco/alfresco-content-repository:6.2.0"
        );
    }

    @ParameterizedTest(name = "[{index}] image: {0}")
    @MethodSource("alfrescoImages")
    void worksWithAlfrescoImage(final String image) {
        runAndValidateContainer(new AlfrescoContainer(image));
    }

    private static Stream<Arguments> xenitImages() {
        return Stream.of(
//                Arguments.arguments(Edition.COMMUNITY, "5.2"),
                Arguments.arguments(Edition.ENTERPRISE, "5.2"),
//                Arguments.arguments(Edition.COMMUNITY, "6.0"),
                Arguments.arguments(Edition.ENTERPRISE, "6.0"),
//                Arguments.arguments(Edition.COMMUNITY, "6.1"),
                Arguments.arguments(Edition.ENTERPRISE, "6.1")
        );
    }

    @ParameterizedTest(name = "[{index}] edition: {0}, version: {1}")
    @MethodSource("xenitImages")
    void worksWithXenitImage(final Edition edition, final String version) {
        runAndValidateContainer(new AlfrescoContainer(edition, version));
    }

    private void runAndValidateContainer(final AlfrescoContainer alfrescoContainer) {
        alfrescoContainer.withDebug();
        startValidateAndStop(alfrescoContainer, this::validateAlfrescoContainer);
    }

    private void validateAlfrescoContainer(final AlfrescoContainer container) {

        assertThat(container.isRunning(), is(true));

        assertThat(container.getMappedPort(AlfrescoContainer.ALFRESCO_PORT), is(not(nullValue())));
        assertThat(container.getMappedPort(AlfrescoContainer.ALFRESCO_DEBUG_PORT), is(not(nullValue())));

        // TODO do smoke request to Alfresco

    }
}