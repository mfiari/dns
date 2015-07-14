package com.esgi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;


public class Server {

	public String adresseIp;
	public int port;
	public byte[] receivedMessage;
	public byte[] sendMessage;
	public Message message;
	public Client client;
	public HashMap<String, String> listePlageAdresse;
	

	public Server(String string, int i) {
		this.adresseIp = string;
		this.port = i;

		listePlageAdresse = new HashMap<String, String>();
		listePlageAdresse.put("www.github.com", "192.30.252.131");
		listePlageAdresse.put("www.google.fr", "64.233.166.94");
		listePlageAdresse.put("www.myges.com", "54.155.107.66");
	}

	public void getIp() {
		System.out.println("\nRecherche correspondance ip:");

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
			if(this.listePlageAdresse.containsKey(domainName)){				
				Response reponse = new Response();
				reponse.constructResponse("response","A","IN",3000,this.listePlageAdresse.get(domainName));
				listeResponse.add(reponse);
				System.out.println("\nCorrespondance : " + domainName + " = " + this.listePlageAdresse.get(domainName)+"");
			}			
		}
				
		Header headerResponse = new Header();
		headerResponse.createHeader(true,false,1,0,listeResponse.size());
		
		Message messageResponse = new Message(0,listeResponse.size());
		messageResponse.header = headerResponse;
		messageResponse.responses = listeResponse;
			
		sendMessage = messageResponse.getMessageInByte();
		
		sendResponseToClient();
	}


	public void sendResponseToClient() {		
		this.client.recevoirMessage(this.sendMessage);		
	}

	public void receiveMessageFromClient(byte[] message, Client client) {
		this.client = client;
		this.receivedMessage = message;
		getIp();
	}
}