package edu.ucsb.hopefully_unhackable.mongodb;
import org.springframework.data.annotation.Id;

import com.mongodb.BasicDBList;


public class InvertedIndex {
	
	@Id
	private String keyword;
	
	private BasicDBList file_list;
	
	public InvertedIndex() { }
	
	public InvertedIndex(String keyword, BasicDBList file_list) {
		this.keyword = keyword;
		this.file_list = file_list;
	}
	
	public BasicDBList getList() {
		return this.file_list;
	}
	
	
	
	@Override
	public String toString() {
		return String.format(
				"InvertedIndex[id='%s', file_id='%s']",
				keyword, file_list);
	}
}