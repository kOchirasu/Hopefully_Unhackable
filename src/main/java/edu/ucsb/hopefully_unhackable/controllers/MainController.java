package edu.ucsb.hopefully_unhackable.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.BasicDBList;
import edu.ucsb.hopefully_unhackable.mongodb.InvertedIndex;
import edu.ucsb.hopefully_unhackable.mongodb.InvertedIndexRepository;
import edu.ucsb.hopefully_unhackable.mongodb.StringPair;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;

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
			System.out.println(edb);
			Map<String, StringPair> obj = mapper.readValue(edb, new TypeReference<Map<String, StringPair>>(){});
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

    // TODO: search with more than just the first keyword
	@RequestMapping(value = "/searchfile", method = RequestMethod.GET)
    public String searchFile(@RequestParam(value="query") String query) {
		query = query.trim();
		if (query.isEmpty()) {
			return "[]"; // empty list
		}
        String[] keywords = query.split(" ");
        if (keywords.length == 0) { // is this possible
        	return "[]"; // empty list
        }
        // For testing purposes
        for (String w : keywords) {
            System.out.println(retrieve(w));
        }
        System.out.println(Arrays.toString(keywords));
        BasicDBList result = retrieve(keywords[0]);
        if (result == null) {
        	return "[]";
        }
        return result.toString();

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
	
	private void store(Map<String, StringPair> edb) {
		for (Entry<String, StringPair> entry : edb.entrySet()) {
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
					
				if (!new_list.contains(entry.getValue().getFileId())) {
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
		Map<String, StringPair> testMap = new HashMap<>();
		//BasicDBList value1 = new BasicDBList();
		//BasicDBList value2 = new BasicDBList();
		//BasicDBList value3 = new BasicDBList();
		
		//value1.add("id1");
		//value1.add("file1");
		StringPair value1 = new StringPair("id1","file1");
		StringPair value2 = new StringPair("id2","file2");
		StringPair value3 = new StringPair("id3","file3");
		
	
		
		
		testMap.put("key1", value1);
		testMap.put("key2", value2);
		testMap.put("key3", value3);

        List<String> testList = new ArrayList<>();
        testList.add("testkey");
		
		try {
			System.out.println("Serializing Map...");
			String jsonOut = mapper.writeValueAsString(testMap);
			System.out.println(jsonOut);
			System.out.println();
			System.out.println("Deserializing Map...");
			Map<String, StringPair> obj = mapper.readValue(jsonOut, new TypeReference<Map<String, StringPair>>(){});
			System.out.println(obj);

            System.out.println("Serializing List...");
            jsonOut = mapper.writeValueAsString(testList);
            System.out.println(jsonOut);
            System.out.println();
            System.out.println("Deserializing List...");
            List<String> obj2 = mapper.readValue(jsonOut, new TypeReference<List<String>>(){});
            System.out.println(obj2);
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
