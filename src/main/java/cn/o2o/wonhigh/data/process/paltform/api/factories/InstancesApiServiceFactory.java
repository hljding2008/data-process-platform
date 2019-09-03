package cn.o2o.wonhigh.data.process.paltform.api.factories;

import cn.o2o.wonhigh.data.process.paltform.api.InstancesApiService;
import cn.o2o.wonhigh.data.process.paltform.api.impl.InstancesApiServiceImpl;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2019-09-03T21:06:45.041+08:00")
public class InstancesApiServiceFactory {
    private final static InstancesApiService service = new InstancesApiServiceImpl();

    public static InstancesApiService getInstancesApi() {
        return service;
    }
}
