package net.osmialowski.jsonplaceholder.persistence;

import ch.qos.logback.classic.Logger;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.osmialowski.jsonplaceholder.model.Post;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public final class FilesystemStorage implements StorageInterface {
    private static final Logger logger = (Logger) LoggerFactory.getLogger(FilesystemStorage.class);

    private final ObjectMapper objectMapper;
    private final String path;

    public FilesystemStorage(
        ObjectMapper objectMapper,
        @Value("${application.storage.path}") String path
    ) {
        this.objectMapper = objectMapper;
        this.path = path;

        try {
            Files.createDirectories(Paths.get(this.path));
        } catch (IOException exception) {
            logger.error("Unable to create output directory");
            System.exit(1);
        }
    }

    @Override
    public void save(Post post) {
        try {
            String filename = String.format("%d.json", post.getId());
            Path path = Paths.get(Path.of(this.path).toString(), filename);
            objectMapper.writeValue(new File(path.toString()), post);

            logger.info(String.format("Saving post #%s to JSON file", post.getId()));
        } catch (IOException e) {
            logger.error(String.format(
                "Error saving post %s to JSON file: %s",
                post.getId(),
                e.getMessage()
            ));
        }
    }
}
