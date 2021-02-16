package zh.lingvo.rest.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import zh.lingvo.data.model.User;
import zh.lingvo.data.services.UserService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@DisplayName("Test AuthenticationController")
class AuthenticationControllerTest {
    @Mock
    private UserService userService;

    @InjectMocks
    private AuthenticationController controller;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setControllerAdvice(new GeneralExceptionControllerAdvice())
                .build();
    }

    @Nested
    @DisplayName("Test /signup")
    class SignUp {
        private static final String URL = "/signup";
        private static final String USERNAME = "test";

        @Test
        @DisplayName("Should return CONFLICT 409 if the user already exists")
        void userExists() throws Exception {
            when(userService.existsByName(USERNAME)).thenReturn(true);

            mockMvc.perform(post(URL)
                    .content(USERNAME)
            )
                    .andExpect(status().isConflict())
                    .andExpect(content().string("User [" + USERNAME + "] already exists"));

            verify(userService, only()).existsByName(USERNAME);
        }

        @Test
        @DisplayName("Should return OK 200 if the user does not exist")
        void userDoesNotExist() throws Exception {
            when(userService.existsByName(USERNAME)).thenReturn(false);
            when(userService.save(any(User.class))).thenReturn(User.builder().id(1L).name(USERNAME).build());

            mockMvc.perform(post(URL)
                    .content(USERNAME)
            )
                    .andExpect(status().isOk())
                    .andExpect(content().string("1-" + USERNAME));

            verify(userService, times(1)).existsByName(USERNAME);
            verify(userService, times(1)).save(any(User.class));
            verifyNoMoreInteractions(userService);
        }

        @Test
        @DisplayName("Should return SERVICE UNAVAILABLE 503 is there is an unexpected error")
        void errorWithUserService() throws Exception {
            when(userService.existsByName(USERNAME)).thenThrow(new RuntimeException("Something went terribly wrong"));

            mockMvc.perform(post(URL)
                    .content(USERNAME)
            )
                    .andExpect(status().isServiceUnavailable());

            verify(userService, only()).existsByName(USERNAME);
        }
    }
}