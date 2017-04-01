package com.example;

import org.copperengine.core.Acknowledge;
import org.copperengine.core.ProcessingEngine;
import org.copperengine.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class HelloWorldWorkflowTests {

    private static final Logger logger = LoggerFactory.getLogger(HelloWorldWorkflowTests.class);

    public static void main(String[] args) throws Exception {
        String projectDir = getProjectDir();
        String sourceDir = getSourceDir(projectDir);
        String targetDir = getTargetDir(projectDir);

        // 创建工作流引擎，并启动
        ProcessingEngine engine = TransientEngineFactory.create(sourceDir, targetDir);
        engine.startup();

        // 启动工作流。工作流启动后，会马上进入等待
        engine.run(WorkflowDef.HELLO_WORLD, null);

        // 延迟3秒，模拟业务处理时间
        Thread.sleep(3000);

        // 唤醒工作流
        Response<String> response = new Response<>("1", "Hello, I am the World", null);
        Acknowledge.DefaultAcknowledge ack = new Acknowledge.DefaultAcknowledge();
        engine.notify(response, ack);
        ack.waitForAcknowledge();
    }

    private static String getProjectDir() {
        String classLoaderPath = HelloWorldWorkflowTests.class.getClassLoader().getResource("").getPath();
        String projectPath = classLoaderPath.substring(0, classLoaderPath.lastIndexOf("target") - 1);
        return projectPath;
    }

    private static String getSourceDir(String projectDir) {
        String[] dirs = {projectDir, "src", "main", "java", "com", "example", "workflow"};
        return String.join(File.separator, dirs);
    }

    private static String getTargetDir(String projectDir) {
        String[] dirs = {projectDir, "target", "workflow-classes"};
        return String.join(File.separator, dirs);
    }

}
