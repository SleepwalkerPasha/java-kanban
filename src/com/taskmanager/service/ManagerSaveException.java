package com.taskmanager.service;

public class ManagerSaveException extends RuntimeException {

    public ManagerSaveException() {

    }

    public ManagerSaveException(final String mes) {
        super(mes);
    }
}
