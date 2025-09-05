package top.jgblm.nacos.gateway.provider;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProviderController {
    @GetMapping("/echo")
    public String echo(@RequestParam("name") String name) {
        return "hello Nacos Discovery " + name;
    }
}
