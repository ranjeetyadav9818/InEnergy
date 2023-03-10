package com.inenergis.rest;

import com.inenergis.security.TokenHelper;
import com.inenergis.security.UserDetailsDummy;
import io.jsonwebtoken.ExpiredJwtException;
import org.joda.time.DateTimeUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by fanjin on 2017-09-01.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class AuthenticationControllerTest {

    private MockMvc mvc;

    @Autowired
    private TokenHelper tokenHelper;

    @MockBean
    private UserDetailsService userDetailsService;

    @Autowired
    private WebApplicationContext context;

    @Before
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        DateTimeUtils.setCurrentMillisSystem();
    }

    @Test
    public void shouldGetNorFoundWhenGivenNotValidOldToken() throws Exception {

        String token = tokenHelper.generateToken(new UserDetailsDummy("test-user").getUsername());
        this.mvc.perform(post("/auth/login").header("Authorization", "Bearer " + token)
                .contentType("application/x-www-form-urlencoded")
                .param("username","jacinto")
                .param("password","jacinto"))
                .andExpect(status().is(HttpStatus.UNAUTHORIZED.value()));

    }

    @Test(expected = ExpiredJwtException.class)
    public void shouldNotGet200WhenGivenInvalidOldToken() throws Exception {
        DateTimeUtils.setCurrentMillisFixed(1L); // set time back to 1970
        String token = tokenHelper.generateToken(new UserDetailsDummy("test-user").getUsername());
        DateTimeUtils.setCurrentMillisSystem(); // back to now
        ResultActions action = null;
        this.mvc.perform(get("/test").header("Authorization", "Bearer " + token));
    }

}
