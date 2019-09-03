package cn.o2o.wonhigh.data.process.paltform.api.factories;

import cn.o2o.wonhigh.data.process.paltform.api.ClusterApiService;
import cn.o2o.wonhigh.data.process.paltform.api.impl.ClusterApiServiceImpl;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2019-09-03T21:06:45.041+08:00")
public class ClusterApiServiceFactory {
    private final static ClusterApiService service = new ClusterApiServiceImpl();

    public static ClusterApiService getClusterApi() {
        return service;
    }
}
