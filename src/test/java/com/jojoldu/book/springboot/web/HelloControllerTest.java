package com.jojoldu.book.springboot.web;

import com.jojoldu.book.springboot.config.auth.SecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * '@WebMvcTest' 는 @Repository, @Service, @Component 는 스캔 대상이 아니다. 하지만 SecurityConfig 는 @Configuration 파일이기 떄문에 읽어 오지만 이것을 생성할때 필요한
 *               CustomOAuth2UserService 클래스는 @Service 이기 때문에 읽어올수 없다. 이로 인해 예외가 발생한다.
 *               별도로 테스트 코드에서 현재 테스트 환경에서 SecurityConfig가 자동으로 로드되지 않거나, 인증이 필요 없는 API만 테스트하고 있어서 excludeFilters가 없어도 정상 실행되는 것.
 *               하지만 보안 설정이 활성화되거나 인증 관련 로직이 추가되면 예외가 발생할 수 있으므로, excludeFilters를 명시해두는 것이 안전함. ✅
 */
@WebMvcTest(value = HelloController.class , excludeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)})
class HelloControllerTest {

    @Autowired
    private MockMvc mvc; // 웹 APi를 테스트할때 사용된다.

    /**
     * @throws Exception
     * perform(get("/hello"))                   // MockMvc 를 통해 /hello 주소로 Http get 요청을 한다.
     * .andExpect(status().isOk())              // mvc.perform() 를 검증한다, Http Header Status (200) (404) 등등 검증, 여기서는 (200) 검증.
     * .andExpect(content().string(hello));     // mvc.perform() 를 검증한다, Controller 에서 hello 를 리턴하기에 값이 맞는지 검증.
     * '@WithMockUser' 는 스프링 시큐리티를 적용하면 인증되지 않은 사용자는 자동으로 걸러주기 때문에 임의로 인증된 사용자를 넣어주어야 한다
     *  즉 이 어노테이션으로 인해 ROLE_USER 권한을 가진 사용자가 API를 호출하는것과 같은 효과를 가지게 된다.
     */
    @Test
    @WithMockUser(roles = "USER")
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
    @WithMockUser(roles = "USER")
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

















