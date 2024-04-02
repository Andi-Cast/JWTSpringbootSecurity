package Springboot.JWT.Controller;

import org.hibernate.event.spi.ResolveNaturalIdEvent;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

    @GetMapping("/demo")
    public ResponseEntity<String> demo() {
        return ResponseEntity.ok("Secured endpoint accessible to all authority types.");
    }

    @GetMapping("/admin")
    public ResponseEntity<String> admin() {
        return ResponseEntity.ok("Secured endpoint accessible to only ADMIN accounts.");
    }
}
