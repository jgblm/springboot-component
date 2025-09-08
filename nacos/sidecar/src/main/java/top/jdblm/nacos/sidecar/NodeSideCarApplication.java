package top.jdblm.nacos.sidecar;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClients;

@SpringBootApplication
@LoadBalancerClients({
    @LoadBalancerClient("node-service")
})
public class NodeSideCarApplication {
    public static void main(String[] args) {
        SpringApplication.run(NodeSideCarApplication.class, args);
    }
}
