package com.example;

import com.example.external.HelloWorldAdapter;
import com.example.external.HelloWorldData;
import org.copperengine.core.ProcessingEngine;
import org.copperengine.core.util.PojoDependencyInjector;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;
import java.util.Scanner;

public class HelloWorldWorkflowTests {

    private static final Logger logger = LoggerFactory.getLogger(HelloWorldWorkflowTests.class);

    @Test
    public void test() {
    }

    public static void main(String[] args) {
        String projectDir = getProjectDir();
        String sourceDir = getSourceDir(projectDir);
        String targetDir = getTargetDir(projectDir);

        // 创建工作流引擎
        ProcessingEngine engine = TransientEngineFactory.create(sourceDir, targetDir);
        engine.startup();

        // 创建Adapter适合器
        HelloWorldAdapter adapter = new HelloWorldAdapter(engine);

        // 获取注入器，并注册所有的Adapter
        PojoDependencyInjector pojoDependencyInjector = TransientEngineFactory.getPojoDependencyInjector();
        pojoDependencyInjector.register("helloWorldAdapter", adapter);

        logger.info("usage: list | run | send CID NAME | reply CID NAME");
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("please input ：");
            String input = sc.nextLine();
            String[] strs = input.split("\\s");
            if ("list".equals(strs[0])) {
                List<HelloWorldData> datas = adapter.listData();
                for (HelloWorldData data : datas) {
                    logger.info(String.format("Workflow %s: cid=%s, status=%s, sender=%s, replier=%s", WorkflowDef.HELLO_WORLD, data.getCid(), data.getStatus(), data.getSender(), data.getReplier()));
                }
            } else if ("run".equals(strs[0])) {
                HelloWorldData data = new HelloWorldData();
                adapter.runWorkflow(data);
            } else if ("send".equals(strs[0]) && strs.length >= 3) {
                adapter.sendHelloWorld(strs[1], strs[2]);
            } else if ("reply".equals(strs[0]) && strs.length >= 3) {
                adapter.replyHelloWorld(strs[1], strs[2]);
            }
        }
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
