package net.osmialowski.jsonplaceholder.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.osmialowski.jsonplaceholder.model.Post;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureMockRestServiceServer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.HttpServerErrorException;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withServerError;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@ActiveProfiles("test")
@AutoConfigureMockRestServiceServer
@SpringBootTest
class PostRepositoryTest {

    @Autowired
    private MockRestServiceServer mockRestServiceServer;

    @Autowired
    private PostRepository repository;

    @Autowired
    private ObjectMapper objectMapper;

    @AfterEach
    void reset() {
        this.mockRestServiceServer.reset();
    }

    @Test
    void shouldReturnPostsList() throws Exception {
        // given
        this.mockRestServiceServer
            .expect(requestTo("/posts"))
            .andRespond(withSuccess(this.objectMapper.writeValueAsString(
                Arrays.asList(
                    new Post(1, 1, "Title 1", "Body 1"),
                    new Post(2, 2, "Title 2", "Body 2")
                )
            ), MediaType.APPLICATION_JSON));

        // when
        List<Post> posts = this.repository.getAll();

        // then
        assertThat(posts.getFirst().getId()).isEqualTo(1);
        assertThat(posts.getFirst().getUserId()).isEqualTo(1);
        assertThat(posts.getFirst().getTitle()).isEqualTo("Title 1");
        assertThat(posts.getFirst().getBody()).isEqualTo("Body 1");
        assertThat(posts.getLast().getId()).isEqualTo(2);
        assertThat(posts.getLast().getUserId()).isEqualTo(2);
        assertThat(posts.getLast().getTitle()).isEqualTo("Title 2");
        assertThat(posts.getLast().getBody()).isEqualTo("Body 2");
    }

    @Test
    void shouldThrowException() throws Exception {
        // given
        this.mockRestServiceServer
            .expect(requestTo("/posts"))
            .andRespond(withServerError());

        // then
        assertThatThrownBy(() -> this.repository.getAll())
            .isInstanceOf(HttpServerErrorException.class);
    }
}
