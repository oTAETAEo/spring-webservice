package com.jojoldu.book.springboot.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(HelloController.class)
class HelloControllerTest {

    @Autowired
    private MockMvc mvc; // 웹 APi를 테스트할때 사용된다.

    /**
     * @throws Exception
     * perform(get("/hello"))                   // MockMvc 를 통해 /hello 주소로 Http get 요청을 한다.
     * .andExpect(status().isOk())              // mvc.perform() 를 검증한다, Http Header Status (200) (404) 등등 검증, 여기서는 (200) 검증.
     * .andExpect(content().string(hello));     // mvc.perform() 를 검증한다, Controller 에서 hello 를 리턴하기에 값이 맞는지 검증.
     */
    @Test
    void hello가_리턴된다() throws Exception {

        String hello = "hello";

        mvc.perform(get("/hello"))
                .andExpect(status().isOk())
                .andExpect(content().string(hello));
    }

    /**
     * @throws Exception
     *  .param("amount",String.valueOf(amount))         // API 테스트 할때 사용될 져천 파라미머 설정, 단 String 만 허용 문자열로 변환 필요.
     *  .andExpect(jsonPath("$.amount", is(amount)));   // Json 응답 값을 필드별로 검증 가능, $를 기준으로 필드명 명시
     */
    @Test
    void helloDto가_리턴된다() throws Exception {
        // given
        String name = "teat";
        int amount = 100;

        // when
        mvc.perform(
                get("/hello/dto")
                    .param("name",name)
                    .param("amount",String.valueOf(amount))
                )
                // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(name)))
                .andExpect(jsonPath("$.amount", is(amount)));
    }
}

















