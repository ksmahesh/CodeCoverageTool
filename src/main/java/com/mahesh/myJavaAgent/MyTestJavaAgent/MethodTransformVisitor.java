package com.mahesh.myJavaAgent.MyTestJavaAgent;

import java.util.HashSet;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

class MethodTransformVisitor extends MethodVisitor implements Opcodes {
	
	String mName;
	int line;
	String className;
	HashSet<Integer> lineNumHash;
	
    public MethodTransformVisitor(final MethodVisitor mv, String name, String className,HashSet<Integer> lineNumHash ) {
        super(ASM5, mv);
        this.mName=className;//+":"+name;
        this.lineNumHash = lineNumHash;
    }

 // method coverage collection
//    @Override
//    public void visitCode(){
//    	//mv.visitFieldInsn(GETSTATIC, "java/lang/System", "logger", "L org/slf4j/Logger;");
////	    mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
//	   
////	    mv.visitFieldInsn(GETSTATIC, "com/mahesh/myJavaAgent/MyTestJavaAgent/MethodTransformVisitor", "out", "Ljava/io/PrintStream;");
////	    mv.visitLdcInsn(mName+" executed");
////	    mv.visitMethodInsn(INVOKEVIRTUAL, " com/mahesh/myJavaAgent/MyTestJavaAgent/MethodTransformVisitor ", "info", "(Ljava/lang/String;)V", false);
////	    mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
////	    mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
////	    mv.visitLdcInsn(mName+" executed");
////	    mv.visitMethodInsn(INVOKESTATIC, "com/mahesh/myJavaAgent/MyTestJavaAgent/WriteToFile", "write", "(Ljava/lang/String;)V", false);
//    super.visitCode();
//    }
    @Override
    public void visitLineNumber(int line, Label start) {
	    this.line=line;
//	    mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
//	     mv.visitLdcInsn("line "+line+" executed");
//	     mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
	    if (!this.lineNumHash.contains(line)) {
	    	mv.visitLdcInsn(this.mName+": "+line);
		    mv.visitMethodInsn(INVOKESTATIC, "com/mahesh/myJavaAgent/MyTestJavaAgent/WriteToFile", "addToQueue", "(Ljava/lang/String;)V", false);
		    this.lineNumHash.add(line);
//		    System.out.println("added visit line number to"+line);
	    } 
//	    else {
//	    	System.out.println("SKIPPED    ******* added visit line number to"+line);
//	    }
	    
//	     super.visitLineNumber(line, start);
    }
//	@Override
//	public void visitLabel(Label label){
////	mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
////	 mv.visitLdcInsn("line "+line+" executed");
////	 mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
////	 mv.visitLdcInsn(this.mName+": "+line);
////	 mv.visitMethodInsn(INVOKESTATIC, "com/mahesh/myJavaAgent/MyTestJavaAgent/WriteToFile", "addToQueue", "(Ljava/lang/String;)V", false);
//	 super.visitLabel(label);
//	}
}