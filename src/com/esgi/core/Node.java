package com.esgi.core;

public class Node {
    
    private Runnable job;
    private Node next;
    private Node last;
    
    public Node () {
        this.job = null;
        this.next = null;
        this.last = null;
    }
    
    public void addJob (Runnable job) {
        this.job = job;
    }
    
    public Runnable job () {
        return this.job;
    }
    
    public void addNext (Node node) {
        this.next = node;
    }
    
    public Node next () {
        return this.next;
    }
    
    public void addLast (Node node) {
        this.last = node;
    }
    
    public Node last () {
        return this.last;
    }

}
