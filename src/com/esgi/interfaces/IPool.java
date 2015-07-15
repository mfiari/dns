package com.esgi.interfaces;

public interface IPool {
    
    public void addJob (Runnable job);
    
    public Runnable nextJob ();

}
