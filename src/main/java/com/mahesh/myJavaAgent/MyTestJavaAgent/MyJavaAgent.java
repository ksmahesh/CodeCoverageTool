package com.mahesh.myJavaAgent.MyTestJavaAgent;

import java.lang.instrument.Instrumentation;
public class MyJavaAgent {

    private static Instrumentation instrumentation;

    /**
     * JVM hook to statically load the javaagent at startup.
     * 
     * After the Java Virtual Machine (JVM) has initialized, the premain method
     * will be called. Then the real application main method will be called.
     * 
     * @param args
     * @param inst
     * @throws Exception
     */
    public static void premain(String args, Instrumentation inst) throws Exception {
        System.out.println("premain method invoked with args: {} "+ args+" and inst: {}"+ inst);
        System.out.println("Starting writer thread");
        WriteToFile writer = new WriteToFile();
        System.out.println("Writer thread started");
        instrumentation = inst;
        instrumentation.addTransformer(new MyClassFileTransformer(args));
    }

    /**
     * JVM hook to dynamically load javaagent at runtime.
     * 
     * The agent class may have an agentmain method for use when the agent is
     * started after VM startup.
     * 
     * @param args
     * @param inst
     * @throws Exception
     */
    public static void agentmain(String args, Instrumentation inst) throws Exception {
        System.out.println("agentmain method invoked with args: {}"+ args+" and inst: {}"+ inst);
        instrumentation = inst;
        instrumentation.addTransformer(new MyClassFileTransformer(args));
    }

    /**
     * Programmatic hook to dynamically load javaagent at runtime.
     */
    public static void initialize() {
        if (instrumentation == null) {
//            MyJavaAgentLoader.loadAgent();
        }
    }

}