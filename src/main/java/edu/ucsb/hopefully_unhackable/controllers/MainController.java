package edu.ucsb.hopefully_unhackable.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.BasicDBList;
import edu.ucsb.hopefully_unhackable.mongodb.InvertedIndex;
import edu.ucsb.hopefully_unhackable.mongodb.InvertedIndexRepository;
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
        List<Set<Object>> resultSets = new ArrayList<>();
        for (String w : keywords) {
        	BasicDBList res = retrieve(w);
        	if (res != null) {
        		resultSets.add(new HashSet<>(res));
        	}
        }
        if (resultSets.size() < 1) {
        	return "[]";
        }
        
        System.out.println(Arrays.toString(keywords));
        Set<Object> result = intersect(resultSets);
        try {
        	String json = mapper.writeValueAsString(result);
        	System.out.println(json);
			return json;
		} catch (JsonProcessingException e) {
			return "[]";
		}
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
		/*Map<String, String> testMap = new HashMap<>();
		testMap.put("key1", "value1");
		testMap.put("key2", "value2");
		testMap.put("key3", "value3");

        List<String> testList = new ArrayList<>();
        testList.add("testkey");
		
		try {
			System.out.println("Serializing Map...");
			String jsonOut = mapper.writeValueAsString(testMap);
			System.out.println(jsonOut);
			System.out.println();
			System.out.println("Deserializing Map...");
			Map<String, String> obj = mapper.readValue(jsonOut, new TypeReference<Map<String, String>>(){});
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
		}*/
		
		Set<Object> set1 = new HashSet<>(Arrays.asList(new Object[]{"1", "2", "3", "4", "5"}));
		Set<Object> set2 = new HashSet<>(Arrays.asList(new Object[]{"1", "6", "7", "8", "5"}));
		Set<Object> set3 = new HashSet<>(Arrays.asList(new Object[]{"1", "1", "1", "5", "5", "7"}));
		Set<Object> set4 = new HashSet<>(Arrays.asList(new Object[]{"2", "7", "12", "5", "5", "7"}));
		
		List<Set<Object>> list = new ArrayList<>();
		list.add(set1);
		list.add(set2);
		list.add(set3);
		list.add(set4);
		
		System.out.println(intersect(list));
	}
	
	public static Set<Object> intersect(List<Set<Object>> sets) {
		if (sets.size() < 1) {
			return null;
		}
		// Sort sets by size (ascending)
		Collections.sort(sets, new Comparator<Set<Object>>() {
			@Override
			public int compare(Set<Object> o1, Set<Object> o2) {
				return Integer.compare(o1.size(), o2.size());
			}
		});
		
		Set<Object> newSet = sets.get(0);
		for (Set<Object> set : sets) {
			if (set == sets.get(0)) continue;
			newSet.retainAll(set);
			// System.out.println(set.size());
		}
		
		return newSet;
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
