package sentinel.demo.nacos.datasource.config;


import com.alibaba.csp.sentinel.datasource.WritableDataSource;
import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import org.springframework.beans.factory.annotation.Value;

public class WriteableNacos<T> implements WritableDataSource<T> {

    private String serverAddr;
    private String group;
    private String dataId;

    public WriteableNacos(String serverAddr, String group, String dataId) {
        this.serverAddr = serverAddr;
        this.group = group;
        this.dataId = dataId;
    }

    //当sentinel update 时候
    @Override
    public void write(T value) throws Exception {
        System.out.println("-------------write-----------");

        String s = JSON.toJSONString(value);
        System.out.println(s);
        ConfigService configService = NacosFactory.createConfigService(serverAddr);
        boolean isPublishOk = configService.publishConfig(dataId, group, s);
        System.out.println(isPublishOk);
    }

    @Override
    public void close() throws Exception {

    }
}
