package mongodb;

import org.bson.types.ObjectId;
import com.mongodb.BasicDBList;
import java.net.UnknownHostException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.mongodb.MongoClient;

import edu.ucsb.hopefully_unhackable.processor.FileInfo;

@SpringBootApplication
public class Application implements CommandLineRunner {

	@Autowired
	private InvertedIndexRepository repository;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Override
	public void run(String...args) throws Exception {
	
		repository.deleteAll(); 
		
		BasicDBList file_list = new BasicDBList();
		BasicDBList file_list2 = new BasicDBList();
		
		file_list.add("blah1");
		file_list.add("blah2");
		file_list2.add("blah3");
		file_list2.add("blah4");
		
		
		
		repository.save(new InvertedIndex("dog", file_list));
		repository.save(new InvertedIndex("cat", file_list2));

		
		// fetch all tuples
		System.out.println("Files found with findAll():");
		System.out.println("-------------------------------");
		for (InvertedIndex invertedIndex : repository.findAll()) {
			System.out.println(invertedIndex);
		}
		System.out.println();

	
		

		if(repository.exists("dog") == true){System.out.println("EXISTS");}
		store(repository, "fish", "blah5");
		store(repository, "dog", "blah5");
	
	
	}
	
	private static void store(InvertedIndexRepository repository,
			String keyword, String file_id){
		
		//check if keyword is contained in database 
		//if not keyword not contained, make new list with file_id
		if(repository.exists(keyword) == false){

			BasicDBList file_list = new BasicDBList();
			file_list.add(file_id);
			repository.save(new InvertedIndex(keyword, file_list));
		}
		//if keyword contained, update existing list
		else if(repository.exists(keyword) == true){
			InvertedIndex tuple = repository.findByKeyword(keyword);
			BasicDBList new_list = tuple.getList();
			
			if(new_list.contains(file_id) == false){
				new_list.add(file_id);
				repository.save(new InvertedIndex(keyword, new_list));
			}
			
		}
	
	
	}
}