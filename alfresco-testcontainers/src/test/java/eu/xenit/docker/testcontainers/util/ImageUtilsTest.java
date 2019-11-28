package eu.xenit.docker.testcontainers.util;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class ImageUtilsTest {

    @ParameterizedTest(name = "[{index}] image: {0}, expected version: {1}")
    @MethodSource("imagesWithVersion")
    void extractVersion(final String image, final String expectedVersion) {
        assertThat(ImageUtils.extractVersion(image), is(equalTo(expectedVersion))
        );
    }

    private static Stream<Arguments> imagesWithVersion() {
        return Stream.of(
                arguments("docker.io/alfresco/alfresco-content-repository-community:6.0.7-ga", "6.0.7-ga"),
                arguments("docker.io/xenit/alfresco-repository-community:5.2", "5.2")
        );
    }


}