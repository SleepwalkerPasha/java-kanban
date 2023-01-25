package com.taskmanager.exception;

public class ManagerSaveException extends RuntimeException {

    public ManagerSaveException() {

    }

    public ManagerSaveException(final String mes) {
        super(mes);
    }
}
