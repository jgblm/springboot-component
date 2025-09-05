package top.jgblm.nacos.gateway.discovery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClients;

// 请求示例 http://localhost:18085/nacos/echo?name=jjking

@SpringBootApplication
@EnableDiscoveryClient
@LoadBalancerClients({
    @LoadBalancerClient("service-gateway-provider")
})
public class DiscoveryApplication {
    public static void main(String[] args) {
        SpringApplication.run(DiscoveryApplication.class, args);
    }
}
