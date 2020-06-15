package sentinel.demo.nacos.datasource.config;

import com.alibaba.csp.sentinel.datasource.ReadableDataSource;
import com.alibaba.csp.sentinel.datasource.WritableDataSource;
import com.alibaba.csp.sentinel.datasource.nacos.NacosDataSource;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.alibaba.csp.sentinel.transport.util.WritableDataSourceRegistry;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.nacos.api.exception.NacosException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
public class SentinelUtil {

    @Value("${nacos.config.server-addr}")
    private String serverAddr;
    @Value("${nacos.config.dataId}")
    private String dataId;
    @Value("${nacos.config.group}")
    private String group;

    @PostConstruct
    public void  init() {
        System.out.println("-------------init-----------");

        //sentinel配置信息的读数据源，即从哪里读取sentinel配置信息
        ReadableDataSource<String, List<FlowRule>> flowRuleDataSource = new NacosDataSource<>(serverAddr, group, dataId,
                source -> JSON.parseObject(source, new TypeReference<List<FlowRule>>() {}));
        FlowRuleManager.register2Property(flowRuleDataSource.getProperty());

        // sentinel持久化写数据源，即sentinel配置信息保存到哪里；
        // 当sentinel配置信息有变化时，会调用WritableDataSource实现类的write方法
        WritableDataSource writableDataSource = new WriteableNacos<List<FlowRule>>(serverAddr,group,dataId);
        WritableDataSourceRegistry.registerFlowDataSource(writableDataSource);

    }
}
