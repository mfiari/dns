package com.esgi.core;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.esgi.interfaces.IPool;

public class Pool implements IPool {
    
    private final List<Worker> workers;
    private Node node;
    private final Object lock;
    
    public Pool (int nbWorker) {
        workers = new ArrayList<>();
        lock = new Object();
        for (int i = 0 ; i < nbWorker ; i++) {
            workers.add(new Worker(this));
        }
        node = null;
        this.init();
    }
    
    private void init () {
        for (Worker worker : workers) {
            worker.start();
        }
    }

    @Override
    public void addJob(Runnable job) {
        synchronized(lock) {
            Node newNode = new Node();
            newNode.addJob(job);
            if (this.node == null) {
                this.node = newNode;
            } else {
                if (this.node.next() == null) {
                   this.node.addNext(newNode);
                } else {
                   this.node.last().addNext(newNode);
                }
                this.node.addLast(newNode);
            }
            lock.notify();
       }
    }

    @Override
    public Runnable nextJob() {
        Node tmp;
        synchronized(lock) {
            if (this.node == null) {
                try {
                    lock.wait();
                } catch (InterruptedException ex) {
                    Logger.getLogger(Pool.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            tmp = node;
            node = node.next();
            if (node != null) {
                node.addLast(tmp.last());
            }
        }
        return tmp.job();
    }
}
