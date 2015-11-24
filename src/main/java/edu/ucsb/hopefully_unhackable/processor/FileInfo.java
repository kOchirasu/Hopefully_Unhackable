package edu.ucsb.hopefully_unhackable.processor;

import org.springframework.data.annotation.Id;

public class FileInfo
{
    @Id
    private String id;
    private String key;
    private String bucket;

    public FileInfo(String id, String key, String bucket) {
        this.id = id;
        this.key = key;
        this.bucket = bucket;
    }

    public String getId()
    {
        return id;
    }

    public String getKey()
    {
        return key;
    }

    public String getBucket()
    {
        return bucket;
    }
}
