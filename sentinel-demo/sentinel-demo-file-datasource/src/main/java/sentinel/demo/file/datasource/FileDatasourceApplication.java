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

package sentinel.demo.file.datasource;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author xiaojing
 */
@SpringBootApplication
@RestController
public class FileDatasourceApplication {

	public static void main(String[] args) {
		SpringApplication.run(FileDatasourceApplication.class, args);
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

}
