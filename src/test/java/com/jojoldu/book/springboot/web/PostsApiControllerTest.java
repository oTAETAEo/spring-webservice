package com.jojoldu.book.springboot.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jojoldu.book.springboot.domain.posts.Posts;
import com.jojoldu.book.springboot.domain.posts.PostsRepository;
import com.jojoldu.book.springboot.web.dto.PostsSaveRequestDto;
import com.jojoldu.book.springboot.web.dto.PostsUpdateRequestDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * '@WebMvcTest' 는 JPA 기능이 동작하지 않는다. JAP 기능까지 사용하려면 '@SpringBootTest' 사용해야 한다.
 * '@AutoConfigureMockMvc' 스프링부트 3에서 Spring MVC 프로젝트를 할때 권장하는 방법.
 * 'ObjectMapper' JSON → Java 객체 (역직렬화), Java 객체 → JSON (직렬화) 형식으로 바꿔주는 class 이다.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class PostsApiControllerTest {

    @LocalServerPort
    int port;

    @Autowired
    private PostsRepository postsRepository;

    @Autowired
    private MockMvc mockMvc; // 🚀 MockMvc 사용

    @Autowired
    private ObjectMapper objectMapper;

    @AfterEach
    public void tearDown() {
        postsRepository.deleteAll();
    }

    @Test
    void post_등록된다() throws Exception{
        // given
        String title = "title";
        String content = "content";

        PostsSaveRequestDto requestDto = PostsSaveRequestDto.builder()
                .title(title)
                .content(content)
                .author("author")
                .build();

        // when
        String requestBody = objectMapper.writeValueAsString(requestDto);

        mockMvc.perform(post("/api/v1/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(1L)); // JSON 응답이 Long 값이므로


        List<Posts> all = postsRepository.findAll();
        assertThat(all.get(0).getTitle()).isEqualTo(title);
        assertThat(all.get(0).getContent()).isEqualTo(content);
    }

    @Test
    void post_수정된다() throws Exception{
        // given

        Posts savedPosts = postsRepository.save(Posts.builder()
                .title("title")
                .content("content")
                .author("author")
                .build());

        Long updateId = savedPosts.getId();
        String expectedTitle = "title2";
        String expectedContent = "content2";

        PostsUpdateRequestDto requestDto = PostsUpdateRequestDto.builder()
                .title(expectedTitle)
                .content(expectedContent)
                .build();

        //when
        String requestBody = objectMapper.writeValueAsString(requestDto);

        mockMvc.perform(put("/api/v1/posts/" + updateId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(updateId)); // JSON 응답이 Long 값이므로


        List<Posts> all = postsRepository.findAll();
        assertThat(all.get(0).getTitle()).isEqualTo(expectedTitle);
        assertThat(all.get(0).getContent()).isEqualTo(expectedContent);

    }

    @Test
    void BaseTimeEntity_등록() throws Exception{
        // given
        LocalDateTime now = LocalDateTime.of(2025,3,20,0,0,0);
        postsRepository.save(Posts.builder()
                .title("title")
                .author("author")
                .content("content")
                .build());

        // when
        List<Posts> postsList = postsRepository.findAll();

        // then
        Posts posts = postsList.get(0);

        System.out.println("posts.getCreateDate() = " + posts.getCreateDate().toString());

        assertThat(posts.getCreateDate()).isAfter(now);
        assertThat(posts.getModifiedDate()).isAfter(now);
    }
}

















