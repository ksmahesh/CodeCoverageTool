package com.mahesh.myJavaAgent.MyTestJavaAgent;

import java.util.HashSet;

public class MapValue {
	String PackageName; 
	public String getPackageName() {
		return PackageName;
	}
	public HashSet<Integer> lineHashSet;
	
	public HashSet<Integer> getLineHashSet() {
		return lineHashSet;
	}
	public void setLineHashSet(HashSet<Integer> lineHashSet) {
		this.lineHashSet = lineHashSet;
	}
	public MapValue() {
		this.lineHashSet = new HashSet<Integer>();
	}
	public MapValue(String packName) {
		this.lineHashSet = new HashSet<Integer>();
		this.PackageName =  packName;
	}
}
