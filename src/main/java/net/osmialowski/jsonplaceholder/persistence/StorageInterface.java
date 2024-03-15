package net.osmialowski.jsonplaceholder.persistence;

import net.osmialowski.jsonplaceholder.model.Post;

public interface StorageInterface {

    void save(Post post);
}
