package top.jgblm.nacos.config.controller;

import com.alibaba.cloud.commons.lang.StringUtils;
import com.alibaba.cloud.nacos.NacosConfigManager;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@RestController
@RequestMapping("/nacos")
public class ConfigController {
    Logger logger = LoggerFactory.getLogger(ConfigController.class );

    public static final String DEFAULT_GROUP = "DEFAULT_GROUP";

    @Resource
    private NacosConfigManager nacosConfigManager;

    @RequestMapping("/getConfig")
    public String getConfig(@RequestParam("dataId") String dataId,
                            @RequestParam(value = "group", required = false) String group)
        throws NacosException {
        if (StringUtils.isEmpty(group)) {
            group = DEFAULT_GROUP;
        }
        ConfigService configService = nacosConfigManager.getConfigService();
        return configService.getConfig(dataId, group, 2000);
    }

    @RequestMapping("/publishConfig")
    public boolean publishConfig(@RequestParam("dataId") String dataId,
                                 @RequestParam(value = "group", required = false) String group,
                                 @RequestParam("content") String content) throws NacosException {
        if (StringUtils.isEmpty(group)) {
            group = DEFAULT_GROUP;
        }
        ConfigService configService = nacosConfigManager.getConfigService();
        return configService.publishConfig(dataId, group, content);
    }

    @RequestMapping("/removeConfig")
    public boolean removeConfig(@RequestParam("dataId") String dataId,
                                @RequestParam(value = "group", required = false) String group)
        throws NacosException {
        if (StringUtils.isEmpty(group)) {
            group = DEFAULT_GROUP;
        }
        ConfigService configService = nacosConfigManager.getConfigService();
        return configService.removeConfig(dataId, group);
    }

    @RequestMapping("/listener")
    public String listenerConfig(@RequestParam("dataId") String dataId,
                                 @RequestParam(value = "group", required = false) String group)
        throws NacosException {
        if (StringUtils.isEmpty(group)) {
            group = DEFAULT_GROUP;
        }
        ConfigService configService = nacosConfigManager.getConfigService();
        configService.addListener(dataId, group, new Listener() {
            @Override
            public Executor getExecutor() {
                return Executors.newSingleThreadExecutor();
            }

            @Override
            public void receiveConfigInfo(String configInfo) {
                logger.info("[Listen for configuration changes]:{}", configInfo);
            }
        });
        return "Add Lister successfully!";
    }
}
