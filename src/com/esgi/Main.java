package com.esgi;

import com.esgi.core.Pool;

public class Main {
	
	public static void main(String[] args) {	

		/*Client client = new Client("127.0.0.1",8181);
		Server server = new Server("127.0.0.1",8282);
		
		client.linkToServer(server);
		
		client.sendRequestWithDomainName("www.google.fr");
		
		client.displayResult();*/
		
		Pool pool = new Pool(3);
		
		Handler handler = new Handler(pool);
		handler.handle("www.google.fr");
		handler.handle("www.google.fr");
		
		
	}

}
