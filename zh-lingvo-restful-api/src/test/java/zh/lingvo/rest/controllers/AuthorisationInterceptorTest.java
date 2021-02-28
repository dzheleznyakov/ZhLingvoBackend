package zh.lingvo.rest.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import zh.lingvo.data.model.User;
import zh.lingvo.data.services.UserService;
import zh.lingvo.rest.annotations.ApiController;
import zh.lingvo.rest.util.RequestContext;

import java.util.Optional;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@DisplayName("Test Authorisation interceptor")
class AuthorisationInterceptorTest {
    private static final String API_URL = "/api/auth/on";
    private static final String AUTH_TOKEN = "token";
    private static final String COOKIES_HEADER = "Cookies";
    private static final String COOKIES = "cookie1=value1; cookie2=value2; authToken=" + AUTH_TOKEN + "; cookie3=value3";
    private static final User user = User.builder().id(1L).name("test").build();

    @Mock
    private UserService userService;

    private RequestContext requestContext;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        requestContext = new RequestContext();
        mockMvc = MockMvcBuilders.standaloneSetup(new TestController())
                .addInterceptors(new AuthorisationInterceptor(userService, requestContext))
                .setControllerAdvice(new ExceptionsAdvice())
                .build();
    }

    @Test
    @DisplayName("Should return UNAUTHORIZED 401 if the auth token is missing")
    void missingAuthToken_ReturnUnauthorized() throws Exception {
        when(userService.findByAuthToken(null)).thenReturn(Optional.empty());

        mockMvc.perform(get(API_URL).header(COOKIES_HEADER, "cookie1=value1;"))
                .andExpect(status().isUnauthorized());

        verify(userService, times(1)).findByAuthToken(null);
        verifyNoMoreInteractions(userService);
    }

    @Test
    @DisplayName("Should return UNAUTHORIZED 401 if the auth token is invalid")
    void invalidAuthToken_ReturnUnauthorized() throws Exception {
        when(userService.findByAuthToken(AUTH_TOKEN)).thenReturn(Optional.empty());

        mockMvc.perform(get(API_URL).header(COOKIES_HEADER, COOKIES))
                .andExpect(status().isUnauthorized());

        verify(userService, times(1)).findByAuthToken(AUTH_TOKEN);
        verifyNoMoreInteractions(userService);
    }

    @Test
    @DisplayName("Should return OK 200 if the auth token is valid")
    void validAuthToken_ReturnOk() throws Exception {
        when(userService.findByAuthToken(AUTH_TOKEN)).thenReturn(Optional.of(user));

        mockMvc.perform(get(API_URL).header(COOKIES_HEADER, COOKIES))
                .andExpect(status().isOk());

        verify(userService, times(1)).findByAuthToken(AUTH_TOKEN);
        verifyNoMoreInteractions(userService);
    }

    @ApiController
    private static class TestController {
        @GetMapping(API_URL)
        public void handler() {
        }
    }
}