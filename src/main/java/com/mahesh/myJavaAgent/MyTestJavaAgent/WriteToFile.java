package com.mahesh.myJavaAgent.MyTestJavaAgent;
import java.io.*;
import java.nio.charset.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class WriteToFile implements Runnable {
	private boolean init = false;
	public static BlockingQueue<HashMap<String,MapValue>> bqOutput = new ArrayBlockingQueue<HashMap<String,MapValue>>(10000);
	public static BlockingQueue<HashMap<String,String>> testDetails = new ArrayBlockingQueue<HashMap<String,String>>(10000);
	private static Path outputFile;
	private static boolean createdFile; 
	private static final String fileName = "LineCovFile.txt";
	private static Runnable writeThread;
	private static Runnable writeToQ;
	private static HashMap<String,MapValue> currTestHash = new HashMap<String,MapValue>();
	private static String testStart = "TestStart";
	private static String endRun = "EndRun";
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
				for (HashMap<String,MapValue> testHash=bqOutput.take(); !(testHash.containsKey(WriteToFile.endRun)); testHash=bqOutput.take()) {
//					System.out.println("%%%%%%%%%%%% Writing new hashset to a file");
					try {
						//Files.write(outputFile, Arrays.asList(line), Charset.forName("UTF-8"),StandardOpenOption.APPEND);
						//first print the testname and other details in the testStart hash key
						if ( WriteToFile.testDetails.peek()!= null){
							HashMap<String,String> thisTestDetails = WriteToFile.testDetails.take();
							writer.write(thisTestDetails.get(WriteToFile.testStart));
						}
						//now write every hash
						
						for (String className: testHash.keySet()) {
							String packageName = testHash.get(className).getPackageName();
							HashSet<Integer> itrHash = testHash.get(className).getLineHashSet();
							Iterator<Integer> hashItr = itrHash.iterator();
							while(hashItr.hasNext()) {
								writer.write(packageName +" : " + hashItr.next()+"\n");
							}
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
//				System.out.println("%%%%%%%%%%%% came out of the for loop");
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
	public static void addToQueue(String className, String packName, int num) {
		//start of the class line so skipping it 
		if (num == 0) {
			return;
		}
		if (num==-100) {
//			System.out.println("^^^^^^^^^^^^got [TEST");
			try {
				bqOutput.put(currTestHash);
//				System.out.println("^^^^^^^^^^added hash to the queue");
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (className.startsWith("XXX")) {
//				System.out.println("^^^^^^^^^^End of the test run");
				HashMap<String,MapValue> endRunHash = new HashMap<String,MapValue>();
				endRunHash.put(WriteToFile.endRun,new MapValue());
				bqOutput.add(endRunHash);
				return;
			}
			currTestHash = new HashMap<String,MapValue>();
			HashMap<String,String> testStart = new HashMap<String,String>();
			testStart.put(WriteToFile.testStart, className);
			WriteToFile.testDetails.add(testStart);
			return;
		}
		// TODO: seeing negative numbers to adding this 
		num = num<0? -1*num : num;
//		System.out.println("^^^^^^^^^^^^got text as "+text);
//		System.out.println("^^^^^^^^^^^^got num as "+num);
		//HashSet<Integer> existingList = currTestHash.get(className);
		MapValue existingMap = currTestHash.get(className);
		if (existingMap != null && existingMap.PackageName.equals(packName)) {
			HashSet<Integer> existingList = existingMap.getLineHashSet();
//			System.out.println("^^^^^^^^^^^^EXISITING TEXT HASH CHECK");
				existingList.add(num);
		} else {
			MapValue newMap = new MapValue(packName);
			newMap.getLineHashSet().add(num);
			currTestHash.put(className,newMap);
		}
	}
}
