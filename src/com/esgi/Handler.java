package com.esgi;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.esgi.core.Pool;
import com.esgi.job.SendRequestJob;

public class Handler {
	
	private final Pool pool;
	private final Map<String, Map<String, Object>> session;
	
	public Handler (Pool pool) {
		this.pool = pool;
		this.session = new HashMap<>();
	}
	
	public void handle (String domain) {
		
		if (this.session.containsKey(domain)) {
			Date now = new Date();
			Date finish = (Date) this.session.get(domain).get("FINISH");
			if (now.after(finish)) {
				this.pool.addJob(new SendRequestJob(this, domain));
			} else {
				System.out.println("Get From Session : ");
				System.out.println(" - NAME " + this.session.get(domain).get("NAME"));
				System.out.println(" - TYPE " + this.session.get(domain).get("TYPE"));
				System.out.println(" - CLASS " + this.session.get(domain).get("CLASS"));
				System.out.println(" - TTL " + this.session.get(domain).get("TTL"));
				System.out.println(" - LONGUEUR " + this.session.get(domain).get("LONGUEUR"));
				System.out.println(" - RDATA " + this.session.get(domain).get("RDATA"));
			}
			
		} else {
			this.pool.addJob(new SendRequestJob(this, domain));
		}
		
	}
	
	public void runRequest (String domain) {
		Client client = new Client("127.0.0.1",8181);
		Server server = new Server("127.0.0.1",8282);
		
		client.linkToServer(server);
		
		client.sendRequestWithDomainName(domain);
		
		client.displayResult();
		
		int ttl = client.response._ttl;
		
		Date now = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        calendar.add(Calendar.SECOND, ttl);
        Date dateTTL = calendar.getTime();
		
		this.session.put(domain, new HashMap<String, Object>());
		this.session.get(domain).put("NAME", client.response._name);
		this.session.get(domain).put("TYPE", client.response._type);
		this.session.get(domain).put("CLASS", client.response._class);
		this.session.get(domain).put("TTL", client.response._ttl);
		this.session.get(domain).put("LONGUEUR", client.response._longueur);
		this.session.get(domain).put("RDATA", client.response._rdata);
		this.session.get(domain).put("FINISH", dateTTL);
	}

}
