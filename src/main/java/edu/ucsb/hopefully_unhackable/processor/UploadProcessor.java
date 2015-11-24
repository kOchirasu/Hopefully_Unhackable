package edu.ucsb.hopefully_unhackable.processor;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.mongodb.MongoClient;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.UnknownHostException;
import java.util.UUID;

public class UploadProcessor
{
    private static final String BUCKET = "ucsb-temp-bucket-name";
    private static final String DB_NAME = "temp-db-name";

    // Map all keys to file in db
    public static void uploadFile(MultipartFile file) {
        String key = indexFile(file.getOriginalFilename());
        storeFile(key, file);
    }

    // TODO: Allow multiple files with same name
    private static String indexFile(String fileName) {
        // Generate unique key
        String key = UUID.randomUUID().toString();
        try {
            // Create Mongo client
            MongoClient mongoClient = new MongoClient("127.0.0.1", 27017);
            MongoOperations mongoOps = new MongoTemplate(mongoClient, DB_NAME);
            // Index the file into database
            FileInfo fileInfo = new FileInfo(fileName, key, BUCKET);
            mongoOps.insert(fileInfo);
        } catch (UnknownHostException ex) {
            System.out.println("ERROR" + ex);
        }
        // Returns a key to be used for storing file in S3
        return key;
    }

    // Store with Amazon S3
    private static void storeFile(String key, MultipartFile file) {
        // Load AWS Credentials (This needs to be setup on each computer)
        AWSCredentials credentials;
        try {
            credentials = new ProfileCredentialsProvider().getCredentials();
        } catch (Exception ex) {
            throw new AmazonClientException(
                    "Cannot load the credentials from the credential profiles file. " +
                    "Please make sure that your credentials file is at the correct " +
                    "location (~/.aws/credentials), and is in valid format.", ex);
        }

        // Create S3 client
        AmazonS3 s3 = new AmazonS3Client(credentials);
        Region usWest2 = Region.getRegion(Regions.US_WEST_2);
        s3.setRegion(usWest2);

        // Upload the files
        try {
            byte[] bytes = file.getBytes();
            InputStream stream = new ByteArrayInputStream(bytes);
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(bytes.length);
            s3.putObject(BUCKET, key, stream, metadata);
        } catch (IOException ex) {
            System.out.println("ERROR: " + ex);
        }
    }
}
