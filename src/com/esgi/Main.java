package com.esgi;

public class Main {
	
	public static void main(String[] args) {
		
		Server serverDNS = new Server(9142);
		Client clientDNS1 = new Client(9025, serverDNS);
		clientDNS1.getIpByDomainName("www.google.fr");
		clientDNS1.getIpByDomainName("www.myges.com");
		Client clientDNS2 = new Client(9025, serverDNS);
		clientDNS2.getIpByDomainName("www.myges.com");
		Client clientDNS3 = new Client(9025, serverDNS);
		clientDNS3.getIpByDomainName("www.google.fr");
		clientDNS3.getIpByDomainName("www.google.fr");
	}

}
