package edu.ucsb.hopefully_unhackable.mongodb;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface InvertedIndexRepository extends MongoRepository<InvertedIndex, String> {
	public InvertedIndex findByKeyword(String keyword);
	//public List<InvertedIndex> findByIndex(String file_id);
}
