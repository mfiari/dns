package com.esgi;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;


public class Response {
	
	public byte[] NAME;
	public byte[] TYPE;
	public byte[] CLASS;
	public byte[] TTL;
	public byte[] RDLENGTH;
	public byte[] RDATA;


	public void constructResponse(String name,String type,String classe, int ttl,String data){
		NAME = new byte[16];
		NAME = getBytesFromString(NAME, name);
		TYPE = new byte[16];
		TYPE = getBytesFromString(TYPE, type);
		CLASS = new byte[16];
		CLASS = getBytesFromString(CLASS, classe);
		TTL = new byte[32];
		TTL = toByteArray(ttl, 32);
		RDLENGTH = toByteArray (data.getBytes().length,16);
		RDATA = data.getBytes();
	}

	byte[] toByteArray(int value, int longueur) {
		return  ByteBuffer.allocate(longueur).putInt(value).array();
	}


	public byte[] getResponseInByte() {
		try{
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
			outputStream.write(NAME);
			outputStream.write(TYPE);
			outputStream.write(CLASS);
			outputStream.write(TTL);
			outputStream.write(RDLENGTH);
			outputStream.write(RDATA);
			byte[] questionInByte = outputStream.toByteArray( );
			return questionInByte;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	public int decodeByte(byte[] bs) {

		System.out.println("\nDecodage de la réponse: ");
		NAME = Arrays.copyOfRange(bs,0,16);
		String name = new String(NAME);
		
		TYPE = Arrays.copyOfRange(bs,16,32);
		String type = new String(TYPE);
		
		CLASS = Arrays.copyOfRange(bs,32,48);
		String classStr = new String(CLASS);
		
		TTL = Arrays.copyOfRange(bs,48,80);
		int size = ByteBuffer.wrap(TTL).getInt(); 
		
		RDLENGTH = Arrays.copyOfRange(bs,80,96);
		int length = ByteBuffer.wrap(RDLENGTH).getInt(); 
		
		RDATA = Arrays.copyOfRange(bs,96,96+length);
		String rData = new String(RDATA);
		
		int response= NAME.length + TYPE.length + CLASS.length + TTL.length + RDLENGTH.length + RDATA.length;
		
		System.out.println(" NAME " + name );
		System.out.println(" TYPE " + type );
		System.out.println(" CLASS " + classStr);
		System.out.println(" TTL " + size);
		System.out.println(" LONGUEUR " + length);
		System.out.println(" RDATA " + rData);
		
		return response;
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
}