package it.epicode.CAPSTONE_BACK.controller; // adatta il package

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class DebugController {

    @GetMapping("/api/istruttore/debug")
    public Map<String,Object> debug(Authentication auth) {
        if (auth == null) {
            return Map.of("error", "nessuna Authentication in contesto");
        }
        return Map.of(
                "user",        auth.getName(),
                "authorities", auth.getAuthorities().stream()
                        .map(Object::toString)
                        .toList()
        );
    }
}