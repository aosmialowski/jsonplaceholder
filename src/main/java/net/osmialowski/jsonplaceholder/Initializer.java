package net.osmialowski.jsonplaceholder;

import ch.qos.logback.classic.Logger;
import net.osmialowski.jsonplaceholder.model.Post;
import net.osmialowski.jsonplaceholder.persistence.FilesystemStorage;
import net.osmialowski.jsonplaceholder.persistence.StorageInterface;
import net.osmialowski.jsonplaceholder.service.PostRepository;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.client.HttpStatusCodeException;

import java.util.List;

@Configuration
@Profile("!test")
public class Initializer implements CommandLineRunner {
    private static final Logger logger = (Logger) LoggerFactory.getLogger(Initializer.class);

    private final PostRepository repository;
    private final StorageInterface storage;

    public Initializer(PostRepository repository, StorageInterface storage) {
        this.repository = repository;
        this.storage = storage;
    }

    public void run(String... args) {
        try {
            List<Post> posts = this.repository.getAll();

            assert posts != null;
            posts.forEach(this.storage::save);
        } catch (HttpStatusCodeException e) {
            logger.error("Error while fetching posts", e);
        }
    }
}

