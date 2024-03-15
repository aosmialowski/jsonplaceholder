package net.osmialowski.jsonplaceholder.service;

import net.osmialowski.jsonplaceholder.model.Post;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Repository
public final class PostRepository {
    private final RestTemplate restTemplate;
    private final String endpoint;

    public PostRepository(
        RestTemplateBuilder restTemplateBuilder,
        @Value("${application.api.baseUrl}") String baseUrl,
        @Value("${application.api.endpoint}") String endpoint
    ) {
        this.restTemplate = restTemplateBuilder.rootUri(baseUrl).build();
        this.endpoint = endpoint;
    }

    public List<Post> getAll() throws HttpStatusCodeException {
        ResponseEntity<List<Post>> response = this.restTemplate
            .exchange(this.endpoint, HttpMethod.GET, null, new ParameterizedTypeReference<>() {});

        return response.getBody();
    }
}
