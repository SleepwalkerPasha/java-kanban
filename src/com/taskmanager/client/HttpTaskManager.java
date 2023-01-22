package com.taskmanager.client;

import com.taskmanager.service.FileBackedTasksManager;

import java.net.URI;

public class HttpTaskManager extends FileBackedTasksManager {

    private URI url;

    private KVSClient kvsClient;
    public HttpTaskManager(String path, URI url) {
        super(path);
        this.url = url;
    }


}
