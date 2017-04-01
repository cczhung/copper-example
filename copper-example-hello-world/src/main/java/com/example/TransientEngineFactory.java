package com.example;

import org.copperengine.core.DependencyInjector;
import org.copperengine.core.EngineIdProvider;
import org.copperengine.core.EngineIdProviderBean;
import org.copperengine.core.common.*;
import org.copperengine.core.monitoring.NullRuntimeStatisticsCollector;
import org.copperengine.core.monitoring.RuntimeStatisticsCollector;
import org.copperengine.core.tranzient.*;
import org.copperengine.core.util.PojoDependencyInjector;
import org.copperengine.core.wfrepo.CompilerOptionsProvider;
import org.copperengine.core.wfrepo.FileBasedWorkflowRepository;
import org.copperengine.core.wfrepo.URLClassloaderClasspathProvider;

import java.util.ArrayList;
import java.util.List;

public class TransientEngineFactory {

    private static DependencyInjector dependencyInjector = null;

    /**
     * 创建非持久化工作流引擎
     *
     * @param sourceDir 工作流源码存放的路径
     * @param targetDir 编译后存放的路径
     * @return
     */
    public static TransientScottyEngine create(String sourceDir, String targetDir) {
        TransientScottyEngine engine = new TransientScottyEngine();
        engine.setIdFactory(createIdFactory());
        engine.setEngineIdProvider(createEngineIdProvider());
        engine.setDependencyInjector(createDependencyInjector());
        engine.setPoolManager(createProcessorPoolManager());
        engine.setTicketPoolManager(createTicketPoolManager());
        engine.setTimeoutManager(createTimeoutManager());
        engine.setStatisticsCollector(createRuntimeStatisticsCollector());
        engine.setEarlyResponseContainer(createEarlyResponseContainer());
        engine.setWfRepository(createWorkflowRepository(sourceDir, targetDir));
        return engine;
    }

    public static PojoDependencyInjector getPojoDependencyInjector() {
        if (dependencyInjector == null)
            dependencyInjector = createDependencyInjector();
        return (PojoDependencyInjector) dependencyInjector;
    }

    private static IdFactory createIdFactory() {
        return new JdkRandomUUIDFactory();
    }

    private static EngineIdProvider createEngineIdProvider() {
        return new EngineIdProviderBean("TransientEngine#DEFAULT");
    }

    private static DependencyInjector createDependencyInjector() {
        if (dependencyInjector == null)
            dependencyInjector = new PojoDependencyInjector();
        return dependencyInjector;
    }

    private static ProcessorPoolManager<TransientProcessorPool> createProcessorPoolManager() {
        DefaultProcessorPoolManager<TransientProcessorPool> ppm = new DefaultProcessorPoolManager<TransientProcessorPool>();
        TransientPriorityProcessorPool defaultPP = new TransientPriorityProcessorPool(
                TransientProcessorPool.DEFAULT_POOL_ID);
        ppm.addProcessorPool(defaultPP);
        return ppm;
    }

    private static TicketPoolManager createTicketPoolManager() {
        return new DefaultTicketPoolManager();
    }

    private static TimeoutManager createTimeoutManager() {
        return new DefaultTimeoutManager();
    }

    private static RuntimeStatisticsCollector createRuntimeStatisticsCollector() {
        return new NullRuntimeStatisticsCollector();
    }

    private static EarlyResponseContainer createEarlyResponseContainer() {
        return new DefaultEarlyResponseContainer();
    }

    private static WorkflowRepository createWorkflowRepository(String sourceDir, String targetDir) {
        FileBasedWorkflowRepository repo = new FileBasedWorkflowRepository();
        repo.setSourceDirs(sourceDir);
        repo.setTargetDir(targetDir);
        List<CompilerOptionsProvider> compilerOptionsProviders = new ArrayList<>();
        compilerOptionsProviders.add(new URLClassloaderClasspathProvider());
        repo.setCompilerOptionsProviders(compilerOptionsProviders);
        repo.setLoadNonWorkflowClasses(true);
        return repo;
    }

}