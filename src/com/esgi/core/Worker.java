package com.esgi.core;

import com.esgi.interfaces.IPool;

public class Worker extends Thread {
    
    private final IPool pool;
    
    public Worker (IPool pool) {
        this.pool = pool;
    }
    
    @Override
    public void run () {
        while (true) {
            this.pool.nextJob().run();
        }
    }
    
}
