package zh.lingvo.rest.util;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;
import zh.lingvo.data.model.User;

@Component
@RequestScope
@Getter
@Setter
public class RequestContext {
    private User user = User.NULL;
}
