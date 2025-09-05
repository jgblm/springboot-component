package top.jgblm.nacos.config.controller;

import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.jgblm.nacos.config.model.ConfigProperties;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/nacos/bean")
public class BeanAutoRefreshExample {

    @Resource
    private ConfigProperties configProperties;

    @GetMapping
    public Map<String, String> getConfigInfo() {
        Map<String, String> result = new HashMap<>();
        result.put("serverAddr", configProperties.getServerAddr());
        result.put("prefix", configProperties.getPrefix());
        result.put("group", configProperties.getGroup());
        result.put("namespace", configProperties.getNamespace());
        return result;
    }
}
