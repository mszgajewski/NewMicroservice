package com.mszgajewski.cloudgateway;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class FallbackController {

    @RequestMapping("/orderFallback")
    public Mono<String> orderServiceFallBack(){
        return Mono.just("Realizacja zamówienia zajmuję zbyt dużo czasu. Prosze spróbować póżniej");
    }
    @RequestMapping("/paymentFallback")
    public Mono<String> paymentServiceFallBack(){
        return Mono.just("Realizacja płatności zajmuję zbyt dużo czasu. Prosze spróbować póżniej");
    }
}
