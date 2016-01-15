package edu.ucsb.hopefully_unhackable.controllers;
import org.bson.types.ObjectId;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.mongodb.BasicDBList;

public interface InvertedIndexRepository extends MongoRepository<InvertedIndex, String> {
	
	public InvertedIndex findByKeyword(String keyword);
	//public List<InvertedIndex> findByIndex(String file_id);
	
}
