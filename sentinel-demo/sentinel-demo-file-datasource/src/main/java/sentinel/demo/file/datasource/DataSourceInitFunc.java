package sentinel.demo.file.datasource;

import com.alibaba.csp.sentinel.datasource.FileRefreshableDataSource;
import com.alibaba.csp.sentinel.datasource.FileWritableDataSource;
import com.alibaba.csp.sentinel.datasource.ReadableDataSource;
import com.alibaba.csp.sentinel.datasource.WritableDataSource;
import com.alibaba.csp.sentinel.init.InitFunc;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.alibaba.csp.sentinel.transport.util.WritableDataSourceRegistry;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import java.io.File;
import java.util.List;

/**
 * 名称: DataSourceInitFunc
 * 描述: 这个接口需要在com.alibaba.csp.sentinel.init.InitFunc文件中配置
 *
 * @author gaoshudian
 * @date 2020-06-15 17:37
 */
public class DataSourceInitFunc implements InitFunc {

    @Override
    public void init() throws Exception {

        String flowRuleDir = "/Users/gaoshudian/work/developer/workspace/personal/workspace-springcloud/spring-cloud-alibaba-demo/sentinel-demo/sentinel-demo-file-datasource/src/main/resources";
        String flowRuleFile = "flowRule.json";
        String flowRulePath = flowRuleDir + File.separator + flowRuleFile;

        ReadableDataSource<String, List<FlowRule>> ds = new FileRefreshableDataSource<>(
                flowRulePath, source -> JSON.parseObject(source, new TypeReference<List<FlowRule>>() {})
        );
        FlowRuleManager.register2Property(ds.getProperty());

        WritableDataSource<List<FlowRule>> wds = new FileWritableDataSource<>(flowRulePath, this::encodeJson);
        WritableDataSourceRegistry.registerFlowDataSource(wds);
    }

    private <T> String encodeJson(T t) {
        return JSON.toJSONString(t);
    }
}
