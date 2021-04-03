package zh.lingvo.rest.annotations;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@RestController
@ResponseBody
@CrossOrigin(origins = {
        "http://localhost:3000",
        "http://localhost:8080",
        "http://192.168.1.116:3000",
        "http://192.168.1.116:8080"})
public @interface ApiController {
}
