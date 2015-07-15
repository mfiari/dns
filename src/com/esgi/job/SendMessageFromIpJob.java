package com.esgi.job;

import com.esgi.Client;
import com.esgi.Server;

public class SendMessageFromIpJob implements Runnable {
	
	private Server server;
	private String domainName;
	private String qType;
	private String qclass;
	private Client client;
	
	public SendMessageFromIpJob (Server server, Client client, String domainName, String qType, String qclass) {
		this.server = server;
		this.client = client;
		this.domainName = domainName;
		this.qType = qType;
		this.qclass = qclass;
	}

    @Override
    public void run() {
    	server.getFromIp(client, domainName, qType, qclass);
    }

}