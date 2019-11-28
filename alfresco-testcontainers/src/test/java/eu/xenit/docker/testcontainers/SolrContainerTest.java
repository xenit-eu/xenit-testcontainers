package eu.xenit.docker.testcontainers;

import static eu.xenit.docker.testcontainers.util.ContainerTestUtil.startValidateAndStop;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import eu.xenit.docker.testcontainers.SolrContainer.Version;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class SolrContainerTest {

    private static Stream<Arguments> images() {
        return Stream.of(
                arguments("docker.io/xenit/alfresco-solr1:4.2", Version.SOLR1),
                arguments("docker.io/xenit/alfresco-solr4:5.2", Version.SOLR4),
                arguments("docker.io/xenit/alfresco-solr6:1.4.0", Version.SOLR6),
                arguments("docker.io/alfresco/alfresco-search-services:1.4.0", Version.SOLR6)
        );
    }

    @ParameterizedTest(name = "[{index}] image: {0}, version: {1}")
    @MethodSource("images")
    void testSolrContainer(final String image, final Version version) {
        runAndValidateContainer(new SolrContainer(image, version));
    }

    private void runAndValidateContainer(final SolrContainer solrContainer) {
        startValidateAndStop(solrContainer, this::validateSolrContainer);
    }

    private void validateSolrContainer(final SolrContainer container) {
        assertThat(container.isRunning(), is(true));

        assertThat(container.getMappedPort(SolrContainer.SOLR_PORT), is(not(nullValue())));
        assertThat(container.getMappedPort(SolrContainer.SOLR_SSL_PORT), is(not(nullValue())));
    }

}