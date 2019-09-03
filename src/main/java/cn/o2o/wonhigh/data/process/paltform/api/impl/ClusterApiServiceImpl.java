package cn.o2o.wonhigh.data.process.paltform.api.impl;

import cn.o2o.wonhigh.data.process.paltform.api.ApiResponseMessage;
import cn.o2o.wonhigh.data.process.paltform.api.ClusterApiService;
import cn.o2o.wonhigh.data.process.paltform.api.NotFoundException;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2019-09-03T21:06:45.041+08:00")
public class ClusterApiServiceImpl extends ClusterApiService {
    @Override
    public Response getClusterInfo(SecurityContext securityContext)
    throws NotFoundException {
        // do some magic!
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
}
