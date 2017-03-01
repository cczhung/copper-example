package com.example.workflow;

import com.example.WorkflowDef;
import com.example.external.HelloWorldAdapter;
import com.example.external.HelloWorldData;
import com.example.external.HelloWorldStatus;
import org.copperengine.core.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WorkflowDescription(alias = WorkflowDef.HELLO_WORLD, majorVersion = 1, minorVersion = 0, patchLevelVersion = 0)
public class HelloWorldWorkflow extends Workflow<HelloWorldData> {

    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(HelloWorldWorkflow.class);

    private transient HelloWorldAdapter helloWorldAdapter;

    @AutoWire
    public void setHelloWorldAdapter(HelloWorldAdapter helloWorldAdapter) {
        this.helloWorldAdapter = helloWorldAdapter;
    }

    @Override
    public void main() throws Interrupt {
        HelloWorldData data = getData();
        String cid;
        Response<HelloWorldData> response;

        cid = helloWorldAdapter.initHelloWorld(data);
        data.setStatus(HelloWorldStatus.PEND_SEND);
        logger.info(String.format("Init Workflow %s: cid=%s, status=%s", WorkflowDef.HELLO_WORLD, data.getCid(), data.getStatus()));

        boolean done = false;
        while (!done) {
            wait(WaitMode.ALL, 9 * 1000, cid);
            response = getAndRemoveResponse(cid);
            if (response.isTimeout()) {
                data.setStatus(HelloWorldStatus.TIMEOUT);
                logger.info(String.format("Workflow %s Timeout: cid=%s, status=%s, sender=%s, replier=%s", WorkflowDef.HELLO_WORLD, data.getCid(), data.getStatus(), data.getSender(), data.getReplier()));
                done = true;
                continue;
            }
            switch (data.getStatus()) {
                case PEND_SEND:
                    data.setStatus(HelloWorldStatus.PEND_REPLY);
                    logger.info(String.format("Send Workflow %s: cid=%s, status=%s, sender=%s", WorkflowDef.HELLO_WORLD, data.getCid(), data.getStatus(), data.getSender()));
                    break;
                case PEND_REPLY:
                    data.setStatus(HelloWorldStatus.DONE);
                    logger.info(String.format("Reply Workflow %s: cid=%s, status=%s, sender=%s, replier=%s", WorkflowDef.HELLO_WORLD, data.getCid(), data.getStatus(), data.getSender(), data.getReplier()));
                    done = true;
                    break;
                default:
                    logger.warn(String.format("No Action for Workflow %s: cid=%s, status=%s", WorkflowDef.HELLO_WORLD, cid, data.getStatus()));
            }
        }
    }

}