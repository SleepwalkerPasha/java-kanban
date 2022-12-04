package com.taskmanager.model;

public class Node<T> {
    public Node<T> prev;

    public Node<T> next;

    public T data;

    public Node(Node<T> prev, T data, Node<T> next) {
        this.data = data;
        this.prev = prev;
        this.next = next;
    }
}
