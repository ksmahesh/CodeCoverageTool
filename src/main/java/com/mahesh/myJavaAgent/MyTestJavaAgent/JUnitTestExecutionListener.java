package com.mahesh.myJavaAgent.MyTestJavaAgent;

import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

public class JUnitTestExecutionListener extends RunListener {
	static {
        MyJavaAgent.initialize();
    }
    public void testRunStarted(Description description) throws Exception {
//        System.out.println("Number of tests to execute: " + description.testCount());
//        WriteToFile.write("Number of tests to execute: " + description.testCount());
    }

    public void testRunFinished(Result result) throws Exception {
//        System.out.println("XXX Number of tests executed: " + result.getRunCount());
        WriteToFile.addToQueue("Number of tests executed: " + result.getRunCount());
        WriteToFile.addToQueue("XXX");
        while (!WriteToFile.bqOutput.isEmpty()) {
        	Thread.sleep(10);
        }
    }

    public void testStarted(Description description) throws Exception {
    	
//        System.out.println("[TEST] "+description.getClassName()+": " + description.getMethodName());
        WriteToFile.addToQueue("[TEST] "+description.getClassName()+": " + description.getMethodName());
    }

    public void testFinished(Description description) throws Exception {
//        System.out.println("Finished: " + description.getMethodName());
//        WriteToFile.write("Finished: " + description.getMethodName());
    }

    public void testFailure(Failure failure) throws Exception {
//        System.out.println("Failed: " + failure.getDescription().getMethodName());
        WriteToFile.addToQueue("[TEST FAILURE]Failed: " + failure.getDescription().getMethodName());
    }

    public void testAssumptionFailure(Failure failure) {
//        System.out.println("Failed: " + failure.getDescription().getMethodName());
        WriteToFile.addToQueue("Failed: " + failure.getDescription().getMethodName());
    }

    public void testIgnored(Description description) throws Exception {
//        System.out.println("Ignored: " + description.getMethodName());
        WriteToFile.addToQueue("[TEST IGNORED] Ignored: " + description.getMethodName());
    }
}
