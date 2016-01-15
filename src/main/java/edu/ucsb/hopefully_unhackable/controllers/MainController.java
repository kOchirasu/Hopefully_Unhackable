package edu.ucsb.hopefully_unhackable.controllers;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.BasicDBList;

import edu.ucsb.hopefully_unhackable.mongodb.InvertedIndex;
import edu.ucsb.hopefully_unhackable.mongodb.InvertedIndexRepository;

@RestController
public class MainController {
	@Autowired
	private InvertedIndexRepository repository;
	
	private static ObjectMapper mapper = new ObjectMapper();
	
	@RequestMapping("/ping")
	public String test() {
		return "pong";
	}
	
	@RequestMapping(value = "/indexfile", method = RequestMethod.POST)
	public int indexFile(@RequestBody String edb) {
		try {
			Map<String, String> obj = mapper.readValue(edb, new TypeReference<Map<String, String>>(){});
			System.out.println(obj);
			System.out.println("Repository is " + repository);
			store(obj);
			// Store entries into database
			return 200; //HTTP: OK
		} catch (IOException e) {
			e.printStackTrace();
			return 500; //HTTP: Internal Server Error
		}
	}
	
	@RequestMapping(value = "/searchfile", method = RequestMethod.GET)
	public String searchFile(@RequestParam(value="query") String query) {
		String[] keywords = query.split("+");
		System.out.println(Arrays.toString(keywords));
		return retrieve(keywords[0]).toString();
		/*Set<String> results = new HashSet<>();
		for (String w : keywords) {
			results.add(getList(w));
		}
		try {
			return mapper.writeValueAsString(results);
		} catch (JsonProcessingException ex) {
			ex.printStackTrace();
			return "Error!";
		}*/
	}
	
	private void store(Map<String, String> edb) {
		for (Entry<String, String> entry : edb.entrySet()) {
			//check if keyword is contained in database 
			//if not keyword not contained, make new list with file_id
			if (!repository.exists(entry.getKey())) {
				BasicDBList file_list = new BasicDBList();
				file_list.add(entry.getValue());
				repository.save(new InvertedIndex(entry.getKey(), file_list));
			} else if (repository.exists(entry.getKey())) {
				//if keyword contained, update existing list
				InvertedIndex tuple = repository.findByKeyword(entry.getKey());
				BasicDBList new_list = tuple.getList();
				
				if (!new_list.contains(entry.getValue())) {
					new_list.add(entry.getValue());
					repository.save(new InvertedIndex(entry.getKey(), new_list));
				}
			}
		}
	}
	
	//get list of all files that match keyword
	private BasicDBList retrieve(String keyword) {
		if (repository.exists(keyword)) {
			InvertedIndex tuple = repository.findByKeyword(keyword);
			return tuple.getList();
		}
		return null;
	}
	
	public static void main(String[] args) {
		Map<String, String> testMap = new HashMap<>();
		testMap.put("key1", "value1");
		testMap.put("key2", "value2");
		testMap.put("key3", "value3");
		
		try {
			System.out.println("Serializing Map...");
			String jsonOut = mapper.writeValueAsString(testMap);
			System.out.println(jsonOut);
			System.out.println();
			System.out.println("Deserializing Map...");
			Map<String, String> obj = mapper.readValue(jsonOut, new TypeReference<Map<String, String>>(){});
			System.out.println(obj);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//check if keyword contains a file
	/*private static boolean isFileContained(InvertedIndexRepository rep, String file_id){
		return false;
	}*/
	
	/*store("dog", "blah7");
	System.out.println(getList(repository, "dog"));
	//repository.deleteAll(); 
	
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
	*/
}