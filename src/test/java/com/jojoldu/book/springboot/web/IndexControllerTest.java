package com.jojoldu.book.springboot.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class IndexControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void 메인_페이지_로딩() throws Exception {
        // given when then
        mockMvc.perform(get("/"))
                .andExpect(content().string(containsString("스프링 부트로 시작하는 웹 서비스")));
    }
}