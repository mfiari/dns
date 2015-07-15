package com.esgi;

import java.util.Arrays;

public class Client {

	public int port;
	public Server server;
	public byte[] receivedMessage;
	public Response response;

	public Client(int port, Server server) {
		this.port = port;
		this.server = server;
	}

	public void getIpByDomainName(String domainName){

		Header header = new Header(false, false, 1, 2, 0);
		
		Question question = new Question(domainName,"IN","A");

		Message message = new Message(header.getQDCOUNT(),header.getANCOUNT());
		message.header = header;
		message.question = question;
		
		server.receiveMessageFromClient(message.getMessageInByte(), this);
		//displayResponse();
	}

	
	public void displayResponse(){
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
		displayResponse();
	}
}