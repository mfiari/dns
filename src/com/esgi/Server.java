package com.esgi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.esgi.core.Pool;
import com.esgi.job.SendMessageFromIpJob;
import com.esgi.job.SendMessageFromSessionJob;


public class Server {

	public int port;
	public Message message;
	public HashMap<String, String> correspondanceUrlIp;
	private final Map<String, Map<Date, Response>> session;
	private final Pool pool;
	private final Object lock;
	

	public Server(int port) {
		this.port = port;
		this.session = new HashMap<>();
		this.pool = new Pool(3);
		lock = new Object();

		correspondanceUrlIp = new HashMap<String, String>();
		correspondanceUrlIp.put("www.9gag.com", "192.15.227.125");
		correspondanceUrlIp.put("www.google.fr", "90.145.146.12");
		correspondanceUrlIp.put("www.myges.com", "91.135.121.15");
	}

	public void getIp(Client client, byte[] receivedMessage) {
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
		
		if (this.session.containsKey(domainName)) {
			this.pool.addJob(new SendMessageFromSessionJob(this, client, domainName, qType, qclass));
		} else {
			this.pool.addJob(new SendMessageFromIpJob(this, client, domainName, qType, qclass));
		}
		
	}
	
	public void getFromSession (Client client, String domainName, String qType, String qclass) {
		System.out.println("getFromSession : ");
		ArrayList<Response> listeResponse = new ArrayList<Response>();
		synchronized(lock) {
			Date now = new Date();
			Set<Date> dates = this.session.get(domainName).keySet();
			for (Date date : dates) {
				if (now.after(date)) {
					this.pool.addJob(new SendMessageFromIpJob(this, client, domainName, qType, qclass));
				} else {
					listeResponse.add(this.session.get(domainName).get(date));
				}
			}
			lock.notify();
	    }
		this.sendMessageToClient(client, listeResponse);
	}
	
	public void getFromIp (Client client, String domainName, String qType, String qclass) {
		System.out.println("getFromIp : ");
		ArrayList<Response> listeResponse = new ArrayList<Response>();
		if(qType.contains("IN") && qclass.contains("A")){
			if(this.correspondanceUrlIp.containsKey(domainName)){				
				Response reponse = new Response();
				int ttl = 5000;
				reponse.constructResponse("response","A","IN",ttl,this.correspondanceUrlIp.get(domainName));
				listeResponse.add(reponse);
				synchronized(lock) {
					this.session.put(domainName, new HashMap<Date, Response>());
					Date now = new Date();
			        Calendar calendar = Calendar.getInstance();
			        calendar.setTime(now);
			        calendar.add(Calendar.SECOND, ttl);
			        Date dateTTL = calendar.getTime();
			        this.session.get(domainName).put(dateTTL, reponse);
			        lock.notify();
			    }
				System.out.println("\nCorrespondance : " + domainName + " = " + this.correspondanceUrlIp.get(domainName)+"");
			}			
		}
		this.sendMessageToClient(client, listeResponse);
	}
	
	private void sendMessageToClient (Client client, ArrayList<Response> listeResponse) {
		Header headerResponse = new Header(true,false,1,0,listeResponse.size());
		
		Message messageResponse = new Message(0,listeResponse.size());
		messageResponse.header = headerResponse;
		messageResponse.responses = listeResponse;

		byte[] sendMessage = messageResponse.getMessageInByte();
		synchronized(lock) {
			client.recevoirMessage(sendMessage);
			lock.notify();
	    }
	}


	public void receiveMessageFromClient(byte[] message, Client client) {
		getIp(client, message);
	}
}