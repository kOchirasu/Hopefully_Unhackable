package edu.ucsb.hopefully_unhackable.controllers;

import edu.ucsb.hopefully_unhackable.processor.UploadProcessor;
import mongodb.Application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.mongodb.BasicDBList;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

@Controller
//@RestController // Equivalent to @Controller, @ReponseBody returns data rather than a view
public class QueryController
{
	@Autowired
	private InvertedIndexRepository repository;
	
    @RequestMapping("/")
    public String index() {
    	System.out.println(getList(repository, "dog"));
        return "index";
    }
    
	//get list of all files that match keyword
	private static String getList(InvertedIndexRepository rep, String keyword){
		if(rep.exists(keyword) == false){
			return null;
		}
		else if(rep.exists(keyword) == true){
			InvertedIndex tuple = rep.findByKeyword(keyword);
			BasicDBList file_list = tuple.getList();
			return file_list.toString();
		}
		else return null;
	}

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public String search() {
        return "result";
    }

    @RequestMapping(value = "/upload", method = RequestMethod.GET)
    public String upload() {
        return "upload";
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    @ResponseBody
    public String handleUpload(@RequestParam("file") MultipartFile file) {
        if (!file.isEmpty()) {
            String name = file.getOriginalFilename();
            try {
                UploadProcessor.uploadFile(file);
                byte[] bytes = file.getBytes();
                BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(name)));
                stream.write(bytes);
                stream.close();
                return "You successfully uploaded " + name + "!";
            } catch (Exception e) {
                return "You failed to upload " + name + " => " + e.getMessage();
            }
        } else {
            return "You failed to upload because the file was empty.";
        }
    }
}
