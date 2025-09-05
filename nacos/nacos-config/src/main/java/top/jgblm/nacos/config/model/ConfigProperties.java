package top.jgblm.nacos.config.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "jgblm.config")
@Getter
@Setter
public class ConfigProperties {
    private String serverAddr;
    private String prefix;
    private String group;
    private String namespace;
}
