package com.mahesh.myJavaAgent.MyTestJavaAgent;
import java.util.HashSet;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

class ClassTransformVisitor extends ClassVisitor implements Opcodes {
	
	int lineNum = 0;
	String className;
	public HashSet<Integer> lineNumHash;
    public ClassTransformVisitor(final ClassVisitor cv, String className, HashSet<Integer> lineNumHash) {
    	
        super(ASM5, cv);
        this.className = className;
        this.lineNumHash= lineNumHash;
//    	WriteToFile.write("FIRST LINE");
    }

    @Override
    public MethodVisitor visitMethod(final int access, final String name,
            final String desc, final String signature, final String[] exceptions) {
        MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);
        if (mv==null) {
        	return null;
        } else {
        	return new MethodTransformVisitor(mv,name,this.className,lineNumHash);
        }
    }
}


