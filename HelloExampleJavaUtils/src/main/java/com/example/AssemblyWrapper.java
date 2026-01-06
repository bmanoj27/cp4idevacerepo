package com.example;

import com.ibm.broker.plugin.MbException;
import com.ibm.broker.plugin.MbMessageAssembly;

public class AssemblyWrapper {

    private final MbMessageAssembly assembly;

    public AssemblyWrapper(MbMessageAssembly assembly) {
        this.assembly = assembly;
    }

    public String getLocalEnvironmentValueAsString(String path) throws MbException {
        String extractedValue = assembly.getLocalEnvironment().getRootElement().getFirstElementByPath(path).getValueAsString();
        return extractedValue;
    }
}
