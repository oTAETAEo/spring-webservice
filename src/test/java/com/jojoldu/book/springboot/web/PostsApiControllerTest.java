package com.jojoldu.book.springboot.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jojoldu.book.springboot.domain.posts.Posts;
import com.jojoldu.book.springboot.domain.posts.PostsRepository;
import com.jojoldu.book.springboot.web.dto.PostsSaveRequestDto;
import com.jojoldu.book.springboot.web.dto.PostsUpdateRequestDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * '@WebMvcTest' 는 JPA 기능이 동작하지 않는다. JAP 기능까지 사용하려면 '@SpringBootTest' 사용해야 한다.
 * '@AutoConfigureMockMvc' 스프링부트 3에서 Spring MVC 프로젝트를 할때 권장하는 방법.
 * '@WithMockUser' 는 스프링 시큐리티를 적용하면 인증되지 않은 사용자는 자동으로 걸러주기 때문에 임의로 인증된 사용자를 넣어주어야 한다
 *  즉 이 어노테이션으로 인해 ROLE_USER 권한을 가진 사용자가 API를 호출하는것과 같은 효과를 가지게 된다.
 * 'ObjectMapper' JSON → Java 객체 (역직렬화), Java 객체 → JSON (직렬화) 형식으로 바꿔주는 class 이다.
 *  mockMvc.perform() ✅apply(springSecurity())가 없으면 Spring Security 필터가 적용되지 않음.
 *                    ✅ 로그인 인증이 필요한 API 테스트에서 로그인 없이 통과할 수도 있음.
 *                    ✅ Spring Security의 인증, 인가 설정을 제대로 테스트하려면 필수적으로 추가해야 함.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class PostsApiControllerTest {

    @LocalServerPort
    int port;

    @Autowired
    private WebApplicationContext context;

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

    // 각각의 테스트가 실행되기 전에 mockMvc 인스턴스 생성
    @BeforeEach
    public void setup(){
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    @WithMockUser(roles = "USER")
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
        mockMvc.perform(post("/api/v1/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                // then
                .andExpect(status().isOk());

        List<Posts> all = postsRepository.findAll();
        assertThat(all.get(0).getTitle()).isEqualTo(title);
        assertThat(all.get(0).getContent()).isEqualTo(content);
    }

    @Test
    @WithMockUser(roles = "USER")
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

        mockMvc.perform(put("/api/v1/posts/" + updateId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(updateId)); // JSON 응답이 Long 값이므로


        List<Posts> all = postsRepository.findAll();
        assertThat(all.get(0).getTitle()).isEqualTo(expectedTitle);
        assertThat(all.get(0).getContent()).isEqualTo(expectedContent);

    }
}

















