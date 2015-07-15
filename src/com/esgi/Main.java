package com.esgi;

public class Main {
	
	public static void main(String[] args) {	
	
		Server serverDNS = new Server(9142);
		Client clientDNS = new Client(9025, serverDNS);
		clientDNS.getIpByDomainName("www.google.fr");
	}

}
