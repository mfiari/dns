package com.esgi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;


public class Server {

	public int port;
	public byte[] receivedMessage;
	public byte[] sendMessage;
	public Message message;
	public Client client;
	public HashMap<String, String> correspondanceUrlIp;
	

	public Server(int port) {
		this.port = port;

		correspondanceUrlIp = new HashMap<String, String>();
		correspondanceUrlIp.put("www.9gag.com", "192.15.227.125");
		correspondanceUrlIp.put("www.google.fr", "90.145.146.12");
		correspondanceUrlIp.put("www.myges.com", "91.135.121.15");
	}

	public void getIp() {
		System.out.println("\nRecherche correspondance IP:");

		Header header = new Header();
		header.decodeByte(Arrays.copyOfRange(receivedMessage, 0, 96));

		String domainName="";
		String qType="";
		String qclass="";
		int start = 96;
		int gap = 0;
		int end = receivedMessage.length;

		Question question = new Question();
		gap = question.decodeByteNomDomaine(Arrays.copyOfRange(receivedMessage, start, end));
		start = start + gap; 
		domainName=question.domainName;
		qType = new String(question.QTYPE);
		qclass = new String(question.QCLASS);

		ArrayList<Response> listeResponse = new ArrayList<Response>();

		if(qType.contains("IN") && qclass.contains("A")){
			if(this.correspondanceUrlIp.containsKey(domainName)){				
				Response reponse = new Response();
				reponse.constructResponse("response","A","IN",5000,this.correspondanceUrlIp.get(domainName));
				listeResponse.add(reponse);
				System.out.println("\nCorrespondance : " + domainName + " = " + this.correspondanceUrlIp.get(domainName)+"");
			}			
		}
				
		Header headerResponse = new Header(true,false,1,0,listeResponse.size());
		
		Message messageResponse = new Message(0,listeResponse.size());
		messageResponse.header = headerResponse;
		messageResponse.responses = listeResponse;
			
		sendMessage = messageResponse.getMessageInByte();
		
		this.client.recevoirMessage(this.sendMessage);	
	}


	public void receiveMessageFromClient(byte[] message, Client client) {
		this.client = client;
		this.receivedMessage = message;
		getIp();
	}
}