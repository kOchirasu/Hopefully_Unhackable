package edu.ucsb.hopefully_unhackable.mongodb;

import com.mongodb.BasicDBObject;

// pair of (file id, file name)
public class StringPair extends BasicDBObject{
	
	public StringPair(){}
	
	public StringPair(String fileId, String fileName){
		super.put("fileId", fileId);
		super.put("fileName", fileName);

	}
	
	public String getFileId(){
		return (String) super.getString("fileId");
	}
	
	public void setFileId(String fileId){
		super.put("fileId", fileId);
	}

	public String getFileName(){
		return (String) super.getString("fileName");
	}
	
	public void setFileName(String fileName){
		super.put("fileName", fileName);
	}


		/*
	public static Book[] createBookArray(){
		Book[] books = new Book[3];
		books[0]=new Book("Introduction Of Spring Framework",500);
		books[1]=new Book("Expert One-on-One J2EE Development without EJB",900);
		return books;
		
	}*/
	
	/*
	@Override
	public String toString() {
		return fileId + ", " + fileName;
	}*/
	
}