package lepdv.todolistweb.unit.service;

import lepdv.todolistweb.service.ImageService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import static lepdv.todolistweb.Constants.BYTE_ARRAY;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ImageServiceTest {

    private final ImageService imageServiceForUploadMethod = new ImageService("src/test/resources/static/test");
    private final ImageService imageServiceForGetMethod = new ImageService("src/test/resources/static");

    private static final String IMAGE_PATH_FOR_UPLOAD_METHOD = "checkUpload.png";
    private static final String IMAGE_PATH_FOR_GET_METHOD = "lock.png";

    public static final Path CHECK_PATH =
            Path.of("src", "test", "resources", "static", "test", "checkUpload.png");

    private static final FileInputStream INPUT_STREAM;
    static {
        try {
            INPUT_STREAM = new FileInputStream("src/test/resources/static/lock.png");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    @Test
    void upload_shouldUploadFile() {
        imageServiceForUploadMethod.upload(IMAGE_PATH_FOR_UPLOAD_METHOD, INPUT_STREAM);

        assertTrue(Files.exists(CHECK_PATH));
    }


    @Test
    void get_shouldGetOptionalOfByteArray_whenPathIsValid() {
        Optional<byte[]> actualResult = imageServiceForGetMethod.get(IMAGE_PATH_FOR_GET_METHOD);

        assertTrue(actualResult.isPresent());
        actualResult.ifPresent(actual -> assertArrayEquals(BYTE_ARRAY, actual));
    }

    @Test
    void get_shouldGetEmptyOptional_whenPathIsNotValid() {
        Optional<byte[]> actualResult = imageServiceForGetMethod.get("dummy");

        assertTrue(actualResult.isEmpty());
    }

    @AfterAll
    static void closeResources() {
        try {
            INPUT_STREAM.close();
            if (Files.exists(CHECK_PATH)) {
                Files.delete(CHECK_PATH);
                Files.delete(CHECK_PATH.getParent());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}