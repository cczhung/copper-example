package com.example.workflow;

import com.example.WorkflowDef;
import org.copperengine.core.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WorkflowDescription(alias = WorkflowDef.HELLO_WORLD, majorVersion = 1, minorVersion = 0, patchLevelVersion = 0)
public class HelloWorldWorkflow extends Workflow<String> {

    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(HelloWorldWorkflow.class);

    @Override
    public void main() throws Interrupt {
        String cid = "1";
        logger.info(String.format("Workflow{def=%s, cid=%s}: %s", WorkflowDef.HELLO_WORLD, cid, "Waiting"));

        wait(WaitMode.ALL, 9 * 1000, cid);
        Response<String> response = getAndRemoveResponse(cid);
        logger.info(String.format("Workflow{def=%s, cid=%s}: %s", WorkflowDef.HELLO_WORLD, cid, response.getResponse()));
    }

}