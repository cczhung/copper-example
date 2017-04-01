package com.example.external;

import com.example.WorkflowDef;
import org.copperengine.core.Acknowledge;
import org.copperengine.core.ProcessingEngine;
import org.copperengine.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class HelloWorldAdapter {

    private static final Logger logger = LoggerFactory.getLogger(HelloWorldAdapter.class);

    private List<HelloWorldData> datas = new ArrayList<>();

    private ProcessingEngine engine;

    public HelloWorldAdapter(ProcessingEngine engine) {
        this.engine = engine;
    }

    public void runWorkflow(HelloWorldData data) {
        try {
            engine.run(WorkflowDef.HELLO_WORLD, data);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String initHelloWorld(HelloWorldData data) {
        String cid = "" + (datas.size() + 1);
        data.setCid(cid);
        datas.add(data);
        return cid;
    }

    public void sendHelloWorld(String cid, String sender) {
        HelloWorldData data = getData(cid);
        if (data == null) {
            logger.warn(String.format("Not Exist Workflow %s: cid=%s", WorkflowDef.HELLO_WORLD, cid));
            return;
        }
        data.setSender(sender);

        Response<HelloWorldData> response = new Response<>(data.getCid(), data, null);
        Acknowledge.DefaultAcknowledge ack = new Acknowledge.DefaultAcknowledge();
        engine.notify(response, ack);
        ack.waitForAcknowledge();
    }

    public void replyHelloWorld(String cid, String replier) {
        HelloWorldData data = getData(cid);
        if (data == null) {
            logger.warn(String.format("Not Exist Workflow %s: cid=%s", WorkflowDef.HELLO_WORLD, cid));
            return;
        }
        data.setReplier(replier);

        Response<HelloWorldData> response = new Response<>(data.getCid(), data, null);
        Acknowledge.DefaultAcknowledge ack = new Acknowledge.DefaultAcknowledge();
        engine.notify(response, ack);
        ack.waitForAcknowledge();
    }

    public List<HelloWorldData> listData() {
        return datas;
    }

    private HelloWorldData getData(String cid) {
        if (cid == null)
            return null;

        for (HelloWorldData data : datas) {
            if (cid.equals(data.getCid())) {
                return data;
            }
        }
        return null;
    }

}
