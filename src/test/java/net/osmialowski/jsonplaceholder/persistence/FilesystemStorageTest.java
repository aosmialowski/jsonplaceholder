package net.osmialowski.jsonplaceholder.persistence;

import net.osmialowski.jsonplaceholder.model.Post;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = {"application.storage.path=/tmp"})
@ActiveProfiles("test")
class FilesystemStorageTest {

    @Autowired
    private FilesystemStorage storage;

    @Test
    void shouldCreateFile() {
        // given
        Post post = new Post(1, 1, "Title", "Body");
        String filename = String.format("%d.json", post.getId());
        Path path = Paths.get(Path.of("/tmp").toString(), filename);

        // when
        storage.save(post);

        // then
        assertThat(Files.exists(path)).isTrue();
    }
}
