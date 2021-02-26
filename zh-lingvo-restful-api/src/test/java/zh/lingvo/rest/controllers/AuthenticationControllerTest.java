package zh.lingvo.rest.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import zh.lingvo.data.model.User;
import zh.lingvo.data.services.UserService;
import zh.lingvo.rest.converters.UserToAuthCommand;

import java.util.Optional;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@DisplayName("Test AuthenticationController")
class AuthenticationControllerTest {
    @Mock
    private UserService userService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        AuthenticationController controller = new AuthenticationController(userService, new UserToAuthCommand());
        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setControllerAdvice(new ExceptionsAdvice())
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

            String expectedMessage = "User [" + USERNAME + "] already exists";
            mockMvc.perform(post(URL)
                    .content("{\"username\":\"" + USERNAME + "\"}")
                    .contentType(MediaType.APPLICATION_JSON)
            )
                    .andExpect(status().isConflict())
                    .andExpect(jsonPath("$", is(notNullValue())))
                    .andExpect(jsonPath("$.message", is(equalTo(expectedMessage))));

            verify(userService, only()).existsByName(USERNAME);
        }

        @Test
        @DisplayName("Should return OK 200 if the user does not exist")
        void userDoesNotExist() throws Exception {
            when(userService.existsByName(USERNAME)).thenReturn(false);
            when(userService.save(any(User.class))).thenReturn(User.builder().id(1L).name(USERNAME).build());

            mockMvc.perform(post(URL)
                    .content("{\"username\":\"" + USERNAME + "\"}")
                    .contentType(MediaType.APPLICATION_JSON)
            )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", is(notNullValue())))
                    .andExpect(jsonPath("$.username", is(USERNAME)))
                    .andExpect(jsonPath("$.token", is(USERNAME)));

            verify(userService, times(1)).existsByName(USERNAME);
            verify(userService, times(1)).save(any(User.class));
            verifyNoMoreInteractions(userService);
        }

        @Test
        @DisplayName("Should return SERVICE UNAVAILABLE 503 if there is an unexpected error")
        void errorWithUserService() throws Exception {
            when(userService.existsByName(USERNAME)).thenThrow(new RuntimeException("Something went terribly wrong"));

            mockMvc.perform(post(URL)
                    .content("{\"username\":\"" + USERNAME + "\"}")
                    .contentType(MediaType.APPLICATION_JSON)
            )
                    .andExpect(status().isServiceUnavailable())
                    .andExpect(jsonPath("$", is(notNullValue())))
                    .andExpect(jsonPath("$.message", is(notNullValue())));

            verify(userService, only()).existsByName(USERNAME);
        }
    }

    @Nested
    @DisplayName("Test /signin")
    class SignIn {
        private static final String URL = "/signin";
        private static final String USERNAME = "test";

        private final User USER = User.builder().id(1L).name(USERNAME).build();

        @Test
        @DisplayName("Should return NOT FOUND 404 if user is not found")
        void userNotFound() throws Exception {
            when(userService.findByName(USERNAME)).thenReturn(Optional.empty());

            String expectedMessage = "User [" + USERNAME + "] is not found";
            mockMvc.perform(post(URL)
                    .content("{\"username\":\"" + USERNAME + "\"}")
                    .contentType(MediaType.APPLICATION_JSON)
            )
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$", is(notNullValue())))
                    .andExpect(jsonPath("$.message", is(equalTo(expectedMessage))));

            verify(userService, only()).findByName(USERNAME);
        }

        @Test
        @DisplayName("Should return OK 200 if user is found")
        void userIsFound() throws Exception {
            when(userService.findByName(USERNAME)).thenReturn(Optional.of(USER));

            mockMvc.perform(post(URL)
                    .content("{\"username\":\"" + USERNAME + "\"}")
                    .contentType(MediaType.APPLICATION_JSON)
            )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", is(notNullValue())))
                    .andExpect(jsonPath("$.username", is(USERNAME)))
                    .andExpect(jsonPath("$.token", is(USERNAME)));

            verify(userService, only()).findByName(USERNAME);
        }
    }

    @Nested
    @DisplayName("Test /resign")
    class ReSignIn {
        private static final String URL = "/resign";
        private static final String TOKEN = "token";

        @Test
        @DisplayName("Should return NOT FOUND 404 if there is no user for the token")
        void userNotFound() throws Exception {
            when(userService.findByAuthToken(TOKEN)).thenReturn(Optional.empty());

            String expectedMessage = "User is not found";
            mockMvc.perform(post(URL)
                    .content("{\"token\":\"" + TOKEN + "\"}")
                    .contentType(MediaType.APPLICATION_JSON)
            )
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$", is(notNullValue())))
                    .andExpect(jsonPath("$.message", is(equalTo(expectedMessage))));

            verify(userService, only()).findByAuthToken(TOKEN);
        }

        @Test
        @DisplayName("Should return OK 200 if user is found")
        void userIsFound() throws Exception {
            String username = "username";
            User existingUser = User.builder().id(1L).name(username).build();
            when(userService.findByAuthToken(TOKEN)).thenReturn(Optional.of(existingUser));

            mockMvc.perform(post(URL)
                    .content("{\"token\":\"" + TOKEN + "\"}")
                    .contentType(MediaType.APPLICATION_JSON)
            )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", is(notNullValue())))
                    .andExpect(jsonPath("$.username", is(username)))
                    .andExpect(jsonPath("$.token", is(username)));

            verify(userService, only()).findByAuthToken(TOKEN);
        }
    }
}