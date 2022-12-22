package com.taskmanager.service;

import com.taskmanager.interfaces.IHistoryManager;
import com.taskmanager.model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryHistoryManager implements IHistoryManager {

    private CustomLinkedList taskList = new CustomLinkedList();

    public int getSize() {
        return taskList.size();
    }

    @Override
    public void add(Task task) {
        if (taskList.size() >= 10)
            taskList.removeNode(taskList.tail);
        taskList.linkLast(task);
    }

    @Override
    public List<Task> getHistory() {
        return taskList.getTasks();
    }

    @Override
    public void remove(int id) {
        Node<Task> n = taskList.map.remove(id);
        taskList.removeNode(n);
    }

    private class CustomLinkedList {
        private Node<Task> head;

        private Node<Task> tail;

        private HashMap<Integer, Node<Task>> map = new HashMap<>();

        public void linkLast(Task task) {
            if (task == null)
                return;
            Node<Task> n = map.get(task.getId());
            if (n != null)
                removeNode(n);
            Node<Task> oldTail = tail;
            Node<Task> newNode = new Node<>(tail, task, null);
            tail = newNode;
            if (oldTail == null)
                head = newNode;
            else
                oldTail.next = newNode;
            map.put(task.getId(), newNode);
        }

        public List<Task> getTasks() {
            List<Task> tasks = new ArrayList<>();
            Node<Task> tmp = head;
            while (tmp != null) {
                tasks.add(tmp.data);
                tmp = tmp.next;
            }
            return tasks;
        }

        public void removeNode(Node<Task> node) {
            if (head == null || node == null)
                return;
            if (head == node)
                head = node.next;
            if (node.next == null)
                tail = node.prev;
            if (node.next != null)
                node.next.prev = node.prev;
            if (node.prev != null)
                node.prev.next = node.next;
        }

        public int size() {
            return map.size();
        }
    }
}
