package com.mahesh.myJavaAgent.MyTestJavaAgent;
import java.io.*;
import java.nio.charset.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class WriteToFile implements Runnable {
	private boolean init = false;
	public static BlockingQueue<String> bqOutput = new ArrayBlockingQueue<String>(100000);
	private static Path outputFile;
	private static boolean createdFile; 
	private static final String fileName = "LineCovFile.txt";
	private static Runnable writeThread;
	private static Runnable writeToQ;
	private static HashMap<String,ArrayList<Integer>> currTestHash = new HashMap<String,ArrayList<Integer>>();
//	private static File outFile;
	
	public WriteToFile() {
		if (!init) {
//			bqOutput = new ArrayBlockingQueue<String>(100000);
			writeThread = new Runnable() {
				@Override
				public void run(){
					WriteToFile.this.run();
				}
			};
			init();
			init=true;
		}
	}
	public static void init(){
		Thread write = new Thread(writeThread);
		write.start();
	}
	public static void writeToFile(String line) {
		try {
			Files.write(outputFile, Arrays.asList(line), Charset.forName("UTF-8"),StandardOpenOption.APPEND);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void write(String line) {
		File outFile = new File(fileName);
		if (!createdFile) {
			try {
				outFile.createNewFile();
				outputFile = Paths.get(fileName);
				try {
					Files.write(outputFile, Arrays.asList("OUTPUT FILE"), Charset.forName("UTF-8"),StandardOpenOption.WRITE);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			createdFile = true;
		}
		if (createdFile) {
			outputFile = Paths.get(fileName);
			try {
				Files.write(outputFile, Arrays.asList(line), Charset.forName("UTF-8"),StandardOpenOption.APPEND);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	@Override
	public void run() {
		write(" ");
		try {
//			while(bqOutput.isEmpty()) {
//				System.out.println("QueueEmpty so sleeping for 100ms");
//				try {
//					Thread.sleep(100);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
			BufferedWriter writer;
			try {
				writer = new BufferedWriter( new FileWriter( fileName));
				for (String line=bqOutput.take(); !line.startsWith("XXX"); line=bqOutput.take()) {
					try {
						//Files.write(outputFile, Arrays.asList(line), Charset.forName("UTF-8"),StandardOpenOption.APPEND);
						writer.write(line+"\n");
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				writer.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		} catch ( InterruptedException e) {
		      Thread.currentThread().interrupt();
		      System.out.println("Interrupted via InterruptedIOException");
		} 
		
	}
//	public static void addToQueue(String string) {
//		if (currTestHash.contains(string)) {
//			return;
//		}
//		if (string.startsWith("[")||string.startsWith("xxx")) {
//			currTestHash.add(string);
//			if (!currTestHash.isEmpty()) {
//				for (String out: currTestHash) {
//					try {
//						bqOutput.put(out);
//					} catch (InterruptedException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//				}
//			}
//			return;
//		}
//		currTestHash.add(string);
//	}
	public static int getNumber(String text){
		if (text.matches(".*\\d+.*")) {
				return Integer.parseInt(text.replaceAll("\\D", ""));
		} else {
			return -100;
		}
	}

	public static String getChars(String text){
		String test = text.replaceAll("\\d", "");
				
	    return test.substring(test.lastIndexOf("/")+1);
	}
	//OLD addToQueue uncomment for commonUtils in 7 sec
	public static void addToQueue(String string) {
		String text = getChars(string);
		int num = getNumber(string);
		if (num==-100) {
//			System.out.println("^^^^^^^^^^^^got [TEST");
			currTestHash = new HashMap<String,ArrayList<Integer>>();
			try {
				bqOutput.put(string);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return;
		}
//		System.out.println("^^^^^^^^^^^^got text as "+text);
//		System.out.println("^^^^^^^^^^^^got num as "+num);
		if (currTestHash.containsKey(text)) {
//			System.out.println("^^^^^^^^^^^^EXISITING TEXT HASH CHECK");
			ArrayList<Integer> existingList = currTestHash.get(text);
			if (existingList.contains(num)) {
//				System.out.println("^^^^^^^^^^^^EXISITING TEXT AND NUMBER AND HASH CHECK^^^^^");
				return;
			} else {
				try {
					bqOutput.put(string);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				existingList.add(num);
			}
		} else {
			ArrayList<Integer> newList = new ArrayList<Integer>();
			newList.add(num);
			currTestHash.put(text, newList);
			try {
				bqOutput.put(string);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
