package edu.ucsb.hopefully_unhackable.mongodb;



public class StringPair{
	
	private String fileId;
	private String fileName;
	
	public StringPair(){}
	
	public StringPair(String fileId, String fileName){
		this.fileId = fileId;
		this.fileName = fileName;
	}
	
	public String getFileId(){
		return this.fileId;
	}
	
	public String getFileName(){
		return this.fileName;
	}
	
}