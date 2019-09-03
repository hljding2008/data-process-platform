package cn.o2o.wonhigh.data.process.paltform.api.factories;

import cn.o2o.wonhigh.data.process.paltform.api.JobsApiService;
import cn.o2o.wonhigh.data.process.paltform.api.impl.JobsApiServiceImpl;
import cn.o2o.wonhigh.data.process.platform.PlatformContext;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2019-09-03T21:06:45.041+08:00")
public class JobsApiServiceFactory {
    private final static PlatformContext ctx = PlatformContext.INSTANCE();
    private final static JobsApiService service = new JobsApiServiceImpl(ctx);

    public static JobsApiService getJobsApi() {
        return service;
    }
}
