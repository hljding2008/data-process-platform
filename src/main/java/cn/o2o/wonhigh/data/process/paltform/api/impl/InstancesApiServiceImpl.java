package cn.o2o.wonhigh.data.process.paltform.api.impl;

import cn.o2o.wonhigh.data.process.paltform.api.*;
import cn.o2o.wonhigh.data.process.paltform.api.*;

import com.sun.jersey.multipart.FormDataParam;

import cn.o2o.wonhigh.data.process.paltform.api.InstanceState;
import cn.o2o.wonhigh.data.process.paltform.api.InstanceStatus;
import java.util.UUID;

import java.util.List;
import cn.o2o.wonhigh.data.process.paltform.api.NotFoundException;

import java.io.InputStream;

import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.validation.constraints.*;
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2019-09-03T21:06:45.041+08:00")
public class InstancesApiServiceImpl extends InstancesApiService {
    @Override
    public Response changeInstanceState(UUID instanceUUID, InstanceState state, SecurityContext securityContext)
    throws NotFoundException {
        // do some magic!
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
    @Override
    public Response getInstanceInfo(UUID instanceUUID, SecurityContext securityContext)
    throws NotFoundException {
        // do some magic!
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
    @Override
    public Response getInstanceState(UUID instanceUUID, SecurityContext securityContext)
    throws NotFoundException {
        // do some magic!
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
}
