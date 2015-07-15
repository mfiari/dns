package com.esgi.job;

import com.esgi.Handler;

public class SendRequestJob implements Runnable {
	
	private final Handler handler;
	private final String domain;
	
	public SendRequestJob(Handler handler, String domain) {
		this.handler = handler;
		this.domain = domain;
	}

    @Override
    public void run() {
        this.handler.runRequest(domain);
    }
    
}
