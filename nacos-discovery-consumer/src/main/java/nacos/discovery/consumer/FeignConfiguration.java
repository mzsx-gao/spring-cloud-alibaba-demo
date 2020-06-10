package nacos.discovery.consumer;


import org.springframework.context.annotation.Bean;

class FeignConfiguration {

    @Bean
    public EchoServiceFallback echoServiceFallback() {
        return new EchoServiceFallback();
    }

}
