package com.mahesh.myJavaAgent.MyTestJavaAgent;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
//
//import java.lang.instrument.ClassFileTransformer;
//import java.lang.instrument.IllegalClassFormatException;
//import java.security.ProtectionDomain;
import java.util.Arrays;
import java.util.HashSet;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class MyClassFileTransformer implements ClassFileTransformer, Opcodes {

    String classBase;
    public MyClassFileTransformer(){
    	super();
    }
    public MyClassFileTransformer(String base){
    	super();
    	this.classBase = base;
//    	System.out.println("Class base got as param is "+this.classBase);
    	while (this.classBase.contains(".")) {
        	this.classBase=this.classBase.replace(".", "/");	
    	}
//    	System.out.println("Working Directory = " +
//                System.getProperty("user.dir"));
//    	System.out.println("modified class base  is "+this.classBase);
    }
    public boolean mutateThisClass(String className){
    	boolean changeClass = false;
    	if (className.matches(".*WriteToFile.*") || className.matches(".*MyMainClass.*") || className.matches(".*/test/.*")
    			|| className.matches(".*/MyRunner.*") || className.matches(".*/JUnitTestExecutionListener.*")) {
    		return changeClass;
    	} 
    	if (className.startsWith(this.classBase)) {
//    		String folderPath = System.getProperty("user.dir")+"/test/"+className.replace(".", "/")+".java";
////    		System.out.println("PATH FORMULATED IS "+folderPath);
//    		if ((new File(folderPath).exists())) {
//    			changeClass = false;
//        		return changeClass;
//    		}
    		changeClass = true;
    		return changeClass;
    	}
    	return changeClass;
    }
    @Override
    public byte[] transform(ClassLoader loader, String className, Class classBeingRedefined,
            ProtectionDomain protectionDomain, byte[] classfileBuffer)
            throws IllegalClassFormatException {
    	
//    	System.out.println("class file transformer invoked for className: {}", className);
//    	System.out.println("classbase is "+this.classBase);
    	
    	
        if (this.mutateThisClass(className)) {
            ClassReader cr;
//            System.out.println("className CURRENTLY BENIG TRANSFORMED is "+className);
			cr = new ClassReader(classfileBuffer);
			ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
			HashSet<Integer> lineNumHash = new HashSet<Integer>(); 
			ClassTransformVisitor ca = new ClassTransformVisitor(cw,className,lineNumHash);
			cr.accept(ca, 0);
			classfileBuffer = cw.toByteArray();
//    		System.out.println();
        } 
//        else {
////        	System.out.println("className NOT TRANSFORMED is "+className);
//        }
        
        return classfileBuffer;
    }

}
