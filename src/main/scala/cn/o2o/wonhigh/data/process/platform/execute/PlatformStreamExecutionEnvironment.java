package cn.o2o.wonhigh.data.process.platform.execute;

import org.apache.flink.annotation.Public;
import org.apache.flink.annotation.PublicEvolving;
import org.apache.flink.api.common.ExecutionConfig;
import org.apache.flink.api.common.InvalidProgramException;
import org.apache.flink.api.common.JobExecutionResult;
import org.apache.flink.api.common.JobSubmissionResult;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.client.program.JobWithJars;
import org.apache.flink.client.program.ProgramInvocationException;
import org.apache.flink.client.program.rest.RestClusterClient;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.configuration.JobManagerOptions;
import org.apache.flink.configuration.RestOptions;
import org.apache.flink.runtime.jobgraph.SavepointRestoreSettings;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.graph.StreamGraph;
import org.apache.flink.util.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Public
public class PlatformStreamExecutionEnvironment extends StreamExecutionEnvironment {
    private static final Logger LOG = LoggerFactory.getLogger(PlatformStreamExecutionEnvironment.class);
    private final String host;
    private final int port;
    private final Configuration clientConfiguration;
    private final List<URL> jarFiles;
    private final List<URL> globalClasspaths;
    private final SavepointRestoreSettings savepointRestoreSettings;

    public PlatformStreamExecutionEnvironment(String host, int port, List<String> jarFiles) {
        this(host, port, (Configuration) null, jarFiles);
    }

    public PlatformStreamExecutionEnvironment(String host, int port, Configuration clientConfiguration, List<String> jarFiles) {
        this(host, port, clientConfiguration, jarFiles, (URL[]) null);
    }

    public PlatformStreamExecutionEnvironment(String host, int port, Configuration clientConfiguration, List<String> jarFiles, URL[] globalClasspaths) {
        this(host, port, clientConfiguration, jarFiles, (URL[]) null, (SavepointRestoreSettings) null);
    }

    @PublicEvolving
    public PlatformStreamExecutionEnvironment(String host, int port, Configuration clientConfiguration, List<String> jarFiles, URL[] globalClasspaths, SavepointRestoreSettings savepointRestoreSettings) {
        if (!ExecutionEnvironment.areExplicitEnvironmentsAllowed()) {
            throw new InvalidProgramException("The RemoteEnvironment cannot be used when submitting a program through a client, or running in a TestEnvironment context.");
        } else if (host == null) {
            throw new NullPointerException("Host must not be null.");
        } else if (port >= 1 && port < 65535) {
            this.host = host;
            this.port = port;
            this.clientConfiguration = clientConfiguration == null ? new Configuration() : clientConfiguration;
            this.jarFiles = jarFiles.stream().map(jarFile->{
                try {
                    URL jarFileUrl = (new File(jarFile)).getAbsoluteFile().toURI().toURL();
                    JobWithJars.checkJarFile(jarFileUrl);
                    return jarFileUrl;
                } catch (MalformedURLException var12) {
                    throw new IllegalArgumentException("JAR file path is invalid '" + jarFile + "'", var12);
                } catch (IOException var13) {
                    throw new RuntimeException("Problem with jar file " + jarFile, var13);
                }
            }).collect(Collectors.toList());

            if (globalClasspaths == null) {
                this.globalClasspaths = Collections.emptyList();
            } else {
                this.globalClasspaths = Arrays.asList(globalClasspaths);
            }

            this.savepointRestoreSettings = savepointRestoreSettings;
        } else {
            throw new IllegalArgumentException("Port out of range");
        }
    }

//    @PublicEvolving
//    public static JobExecutionResult executeRemotely(StreamExecutionEnvironment streamExecutionEnvironment, List<URL> jarFiles, String host, int port, Configuration clientConfiguration, List<URL> globalClasspaths, String jobName, SavepointRestoreSettings savepointRestoreSettings) throws ProgramInvocationException {
//        StreamGraph streamGraph = streamExecutionEnvironment.getStreamGraph(jobName);
//        return executeRemotely(streamGraph, streamExecutionEnvironment.getClass().getClassLoader(), streamExecutionEnvironment.getConfig(), jarFiles, host, port, clientConfiguration, globalClasspaths, savepointRestoreSettings);
//    }

    private static JobSubmissionResult executeRemotely(StreamGraph streamGraph, ClassLoader envClassLoader, ExecutionConfig executionConfig, List<URL> jarFiles, String host, int port, Configuration clientConfiguration, List<URL> globalClasspaths, SavepointRestoreSettings savepointRestoreSettings, boolean isDetached) throws ProgramInvocationException {
        if (LOG.isInfoEnabled()) {
            LOG.info("Running remotely at {}:{}", host, port);
        }

        ClassLoader userCodeClassLoader = JobWithJars.buildUserCodeClassLoader(jarFiles, globalClasspaths, envClassLoader);
        Configuration configuration = new Configuration();
        configuration.addAll(clientConfiguration);
        configuration.setString(JobManagerOptions.ADDRESS, host);
        configuration.setInteger(JobManagerOptions.PORT, port);
        configuration.setInteger(RestOptions.PORT, port);

        RestClusterClient client;
        try {
            client = new RestClusterClient(configuration, "PlatformStreamExecutionEnvironment");
            client.setDetached(isDetached);
        } catch (Exception var23) {
            throw new ProgramInvocationException("Cannot establish connection to JobManager: " + var23.getMessage(), streamGraph.getJobGraph().getJobID(), var23);
        }

        client.setPrintStatusDuringExecution(executionConfig.isSysoutLoggingEnabled());
        if (savepointRestoreSettings == null) {
            savepointRestoreSettings = SavepointRestoreSettings.none();
        }

        JobSubmissionResult jobSubmissionResult;
        try {
            jobSubmissionResult = client.run(streamGraph, jarFiles, globalClasspaths, userCodeClassLoader, savepointRestoreSettings);
        } catch (ProgramInvocationException var24) {
            throw var24;
        } catch (Exception e) {
            String term = e.getMessage() == null ? "." : ": " + e.getMessage();
            throw new ProgramInvocationException("The program execution failed" + term, streamGraph.getJobGraph().getJobID(), e);
        } finally {
            try {
                client.shutdown();
            } catch (Exception var22) {
                LOG.warn("Could not properly shut down the cluster client.", var22);
            }

        }

        return jobSubmissionResult;
    }

    public JobExecutionResult execute(StreamGraph streamGraph) throws Exception {
        this.transformations.clear();
        return this.executeRemotely(streamGraph, this.jarFiles);
    }

    /**
     * 新增异步调用方法
     *
     * @param jobName
     */
    public JobSubmissionResult executeDetached(String jobName) throws Exception {
        Preconditions.checkNotNull(jobName, "Streaming Job name should not be null.");
        StreamGraph streamGraph = this.getStreamGraph(jobName);
        this.transformations.clear();
        return executeRemotely(streamGraph, this.getClass().getClassLoader(), this.getConfig(), jarFiles, this.host, this.port, this.clientConfiguration, this.globalClasspaths, this.savepointRestoreSettings,true);
    }

    /**
     * @deprecated
     */
    @Deprecated
    protected JobExecutionResult executeRemotely(StreamGraph streamGraph, List<URL> jarFiles) throws ProgramInvocationException {
        return executeRemotely(streamGraph, this.getClass().getClassLoader(), this.getConfig(), jarFiles, this.host, this.port, this.clientConfiguration, this.globalClasspaths, this.savepointRestoreSettings,false).getJobExecutionResult();
    }

    public String toString() {
        return "Remote Environment (" + this.host + ":" + this.port + " - parallelism = " + (this.getParallelism() == -1 ? "default" : this.getParallelism()) + ")";
    }

}

