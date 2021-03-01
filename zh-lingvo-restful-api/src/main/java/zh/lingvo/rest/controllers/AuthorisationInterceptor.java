package zh.lingvo.rest.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import zh.lingvo.data.model.User;
import zh.lingvo.data.services.UserService;
import zh.lingvo.rest.exceptions.RequestNotAuthorised;
import zh.lingvo.rest.util.RequestContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

@Slf4j
@Component
public class AuthorisationInterceptor extends HandlerInterceptorAdapter {
    private static final String AUTH_TOKEN_COOKIE_PREFIX = "authToken=";

    private final UserService userService;
    private final RequestContext requestContext;

    public AuthorisationInterceptor(UserService userService, RequestContext requestContext) {
        this.userService = userService;
        this.requestContext = requestContext;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (request.getMethod().equals("OPTIONS")) {
            return true;
        }

        String cookies = request.getHeader("Cookies");
        String authToken = getAuthToken(cookies);
        User user = userService.findByAuthToken(authToken)
                .orElseThrow(() -> new RequestNotAuthorised("Auth token is invalid"));
        requestContext.setUser(user);

        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
        return true;
    }

    private String getAuthToken(String cookiesString) {
        return Arrays.stream(cookiesString.split(";"))
                .map(String::trim)
                .filter(s -> s.startsWith(AUTH_TOKEN_COOKIE_PREFIX))
                .findAny()
                .map(s -> s.substring(AUTH_TOKEN_COOKIE_PREFIX.length()))
                .orElse(null);
    }
}
