/*
 * Copyright 2013-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package sentinel.core.demo;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author xiaojing
 */
@RestController
public class TestController {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private CircuitBreakerFactory circuitBreakerFactory;

    @GetMapping("/test")
    public String test1() {
        return "Hello test";
    }

    @GetMapping("/hello")
//	@SentinelResource("resource")
    public String hello() {
        return "Hello";
    }


    /**
     * 测试限流熔断，exceptionHandler是它的异常处理
     */
    @RequestMapping(value = "/aa",produces="application/json;charset=UTF-8")
    @SentinelResource(value = "aa", blockHandler = "exceptionHandler")
    public Map<String, Object> aa() {
        Map<String, Object> resp = new HashMap<>();
        resp.put("code", "200");
        resp.put("mgs", "成功返回值");
        return resp;
    }

    public Map<String, Object> exceptionHandler(BlockException e) {
        System.out.println("被限流了..." + e);
        Map<String, Object> resp = new HashMap<>();
        resp.put("code", "500");
        resp.put("mgs", "服务器繁忙，请稍后再试");
        return resp;
    }

    /**
     * 测试降级熔断，bbexceptionHandler是它的异常处理
     */
    @RequestMapping(value = "/bb",produces="application/json;charset=UTF-8")
    @SentinelResource(value = "bb", fallback = "failBackHandler")
    public Map<String, Object> bb(String name) {
        try{
            TimeUnit.MILLISECONDS.sleep(5);
        }catch (Exception e){

        }
        Map<String, Object> resp = new HashMap<>();
        resp.put("code", "200");
        resp.put("mgs", "成功返回值");
        return resp;
    }

    public Map<String, Object> failBackHandler(String name,Throwable e) {
        System.out.println("被降级了..." + e);
        Map<String, Object> resp = new HashMap<>();
        resp.put("code", "500");
        resp.put("mgs", "被降级了"+e);
        return resp;
    }

    @GetMapping("/template")
    public String client() {
        return restTemplate.getForObject("http://www.taobao.com/test", String.class);
    }

    //熔断
    @GetMapping("/slow")
    public String slow() {
        return circuitBreakerFactory.create("slow").run(() -> {
            try {
                Thread.sleep(500L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "slow";
        }, throwable -> "fallback");
    }

}
