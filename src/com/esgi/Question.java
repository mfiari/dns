package com.esgi;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class Question {
	
	public byte[] QNAME;
	public byte[] QTYPE;
	public byte[] QCLASS;

	public byte[] QuestionInByte;
	
	public String domainName;


	public void constructQuestionSection(String domainName,String type,String classe){
		this.domainName = "";
		QNAME = getQName(domainName);
		QTYPE = new byte[16];
		QTYPE = getBytesFromString(QTYPE,type);
		QCLASS = new byte[16];
		QCLASS = getBytesFromString(QCLASS,classe);
	}

 
	public byte[] getQName (String domain){
		try{
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
		
			byte[] size = ByteBuffer.allocate(4).putInt((byte) domain.getBytes().length).array(); ;
			outputStream.write(size);
			outputStream.write(domain.getBytes());
			
			outputStream.write(ByteBuffer.allocate(4).putInt((byte)0).array());
			QNAME = outputStream.toByteArray( );
		}catch(Exception e){
			e.printStackTrace();
		}
		return QNAME;
	}

	public byte[] getBytesFromString(byte[] field, String s){
		byte[] tempString = s.getBytes();
		for(int i=0;i<field.length;i++){
			if(i<tempString.length){
				field[i] = tempString[i];
			}else{
				field[i] = 0;
			}
			
		}
		return field;
	}

	public byte[] getQuestionInByte(){
		try{
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
			outputStream.write(QNAME);
			outputStream.write(QTYPE);
			outputStream.write(QCLASS);
			byte[] questionInByte = outputStream.toByteArray( );
			return questionInByte;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}


	public int decodeByteNomDomaine(byte[] bs) {
		System.out.println("\nDécodage de la question: ");
		domainName ="";
		int start = 0;
		int end = 4;
		boolean stop = false;
		while(!stop){
			int longueur = ByteBuffer.wrap(Arrays.copyOfRange(bs,start,end)).getInt();
			if(longueur != 0){
				start = end;
				end = longueur + start;
				domainName += new String(Arrays.copyOfRange(bs,start,end));
				start = end;
				end = start+4;
			}else{
				stop = true;
			}
		}
		
		byte[] qnameByte= new byte[domainName.length()];
		QNAME = getBytesFromString(qnameByte,domainName);		
		QTYPE = Arrays.copyOfRange(bs,end,end+16);
		QCLASS= Arrays.copyOfRange(bs,end+16,end+32);
		String qType = new String(QTYPE);		
		String qClass = new String(QCLASS);
		
		int response = QCLASS.length + QTYPE.length + QNAME.length + 8;
		
		System.out.println(" - QNAME " + domainName);
		System.out.println(" - QTYPE " + qType);
		System.out.println(" - QCLASS "+qClass);
		
		return response;
	}
	
}