package com.taskmanager.server;

public enum Endpoint {
    GET_TASKS, GET_TASK_BY_ID, GET_SUBTASK_BY_ID, GET_EPIC_BY_ID, GET_HISTORY, // get
    POST_TASK, POST_SUBTASK, POST_EPIC, // update and create
    DELETE_TASK, DELETE_EPIC, DELETE_SUBTASK, DELETE_TASKS // delete
}
