package eu.xenit.docker.testcontainers.util;

public class ImageUtils {

    private ImageUtils() {
        // private ctor to hide implicit public one
    }

    public static String extractVersion(final String image) {
        if (image == null || image.trim().isEmpty()) {
            throw new IllegalArgumentException("image");
        }

        String[] parts = image.split(":");
        if (parts.length < 2) {
            throw new IllegalArgumentException("image: '" + image + "', must contain a version");
        }

        return parts[1];
    }
}
