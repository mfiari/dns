package com.esgi;

import java.util.Arrays;

public class Client {
	public static int PORT_DNS = 68;

	public String adresseIp;
	public int port;
	public Server server;
	public byte[] receivedMessage;
	public Response response;

	public Client(String ip, int port) {
		this.adresseIp = ip;
		this.port = port;
	}

	public void linkToServer(Server server){
		this.server = server;
	}


	public void sendRequestWithDomainName(String domainName){

		// En tête
		Header header = new Header();
		header.createHeader(false, false, 1, 2, 0);
		
		// Question
		Question question = new Question();
		question.constructQuestionSection(domainName,"IN","A");
		
		// Message
		Message message = new Message(header.getQDCOUNT(),header.getANCOUNT());
		message.header = header;
		message.question = question;
		
		server.receiveMessageFromClient(message.getMessageInByte(), this);
	}

	
	public void displayResult(){
		Header header = new Header();
		header.decodeByte(Arrays.copyOfRange(receivedMessage, 0, 96));
		int start = 96;
		int end = receivedMessage.length;
	
		for(int i=0;i<header.getANCOUNT();i++){
			Response response = new Response();
			start = start + response.decodeByte(Arrays.copyOfRange(receivedMessage, start, end));
			this.response = response;
		}
	}

	public void recevoirMessage(byte[] messageInByte) {
		receivedMessage = messageInByte;
	}
}