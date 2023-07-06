package proj.bbs;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SecurityTestController {

    @GetMapping("/security_test")
    public String security() {
        return "security test";
    }
}
