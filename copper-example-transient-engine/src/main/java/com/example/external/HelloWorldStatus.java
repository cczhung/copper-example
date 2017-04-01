package com.example.external;

public enum HelloWorldStatus {
    // 等待发送HelloWorld
    PEND_SEND,
    // 等待回应HelloWorld
    PEND_REPLY,
    // 结束
    DONE,
    // 超时
    TIMEOUT
}
