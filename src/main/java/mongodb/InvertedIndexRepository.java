package mongodb;
import org.bson.types.ObjectId;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface InvertedIndexRepository extends MongoRepository<InvertedIndex, String> {
	
	public InvertedIndex findByKeyword(String keyword);
	//public List<InvertedIndex> findByIndex(String );
	//public Object findByKeyword(String keyword);
	
	

}