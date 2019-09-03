package cn.o2o.wonhigh.data.process.paltform.api.impl;

import cn.o2o.wonhigh.data.process.paltform.api.*;
import cn.o2o.wonhigh.data.process.paltform.api.*;

import cn.o2o.wonhigh.data.process.platform.PlatformContext;
import com.sun.jersey.multipart.FormDataParam;

import cn.o2o.wonhigh.data.process.paltform.api.ExtendedJobDefinition;
import cn.o2o.wonhigh.data.process.paltform.api.InlineResponse200;
import cn.o2o.wonhigh.data.process.paltform.api.JobDefinition;
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
public class JobsApiServiceImpl extends JobsApiService {
    private PlatformContext ctx;
    public JobsApiServiceImpl(PlatformContext ctx){
        this.ctx = ctx;
    }
    @Override
    public Response allocateNewJob(SecurityContext securityContext)
    throws NotFoundException {
        // do some magic!
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
    @Override
    public Response getJob(UUID jobUUID, SecurityContext securityContext)
    throws NotFoundException {
        // do some magic!
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
    @Override
    public Response listJob(SecurityContext securityContext)
    throws NotFoundException {
        System.out.println("1111111111111");
        ctx.getJobManager().listJobs();
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
    @Override
    public Response removeJob(UUID jobUUID, SecurityContext securityContext)
    throws NotFoundException {
        // do some magic!
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
    @Override
    public Response updateJob(UUID jobUUID, JobDefinition body, SecurityContext securityContext)
    throws NotFoundException {
        // do some magic!
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
}
