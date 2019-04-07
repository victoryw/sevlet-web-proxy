package com.victoryw.picc;

public class UnSupportedServletContainerException extends RuntimeException {
    public UnSupportedServletContainerException(String serverInfo) {
        super(String.format("this server %s is not supported by the IncludedResponsePrintCrossServletContainerProxy", serverInfo));
    }
}
