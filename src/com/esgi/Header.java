package com.esgi;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class Header {

	public final int LONGUEUR_ID = 16;
	public final int LONGUEUR_OPCODE = 4;
	public final int LONGUEUR_Z = 3;
	public final int LONGUEUR_RCODE = 4;
	public final int LONGUEUR_QDCOUNT = 16;
	public final int LONGUEUR_ANCOUNT = 16;
	public final int LONGUEUR_NSCOUNT = 16;
	public final int LONGUEUR_ARCOUNT = 16;
	public final int LONGUEUR_HEADER = 96;
	
	public int idIncrement=0;
	public byte[] ID;
	public byte[] QR;
	public byte[] Opcode;
	public byte[] AA;
	public byte[] TC;
	public byte[] RD;
	public byte[] RA;
	public byte[] Z;
	public byte[] RCODE;
	public byte[] QDCOUNT;
	public byte[] ANCOUNT;
	public byte[] NSCOUNT;
	public byte[] ARCOUNT;

	public byte[] headerInByte;

	public Header(byte[] _headerInByte){
		this.headerInByte = _headerInByte;
	}

	public Header(){
	}

	public Header(boolean isResponse, boolean isRecursif, int codeResponse, int _numberQuestion, int _numberRR){
		
		ID = toByteArray(this.idIncrement++,LONGUEUR_ID);
		
		this.QR = new byte[]{(byte) (isResponse?0:1)};    	
		this.Opcode = toByteArray(0,this.LONGUEUR_OPCODE);
		this.AA = new byte[]{(byte) (0)};
		this.TC = new byte[]{(byte) (0)};
		if(isResponse){
			this.RD = new byte[]{(byte) (isRecursif?1:0)};
			this.RA = new byte[]{(byte) (0)};
		}else{
			this.RD = new byte[]{(byte) (0)};
			this.RA = new byte[]{(byte) (isRecursif?1:0)};
		}
		this.Z = new byte[this.LONGUEUR_Z];
		this.RCODE = toByteArray(codeResponse,this.LONGUEUR_RCODE);
		this.QDCOUNT = toByteArray(_numberQuestion,this.LONGUEUR_QDCOUNT);
		this.ANCOUNT = toByteArray(_numberRR,this.LONGUEUR_QDCOUNT);
		this.NSCOUNT = toByteArray(0,this.LONGUEUR_QDCOUNT);
		this.ARCOUNT = toByteArray(0,this.LONGUEUR_QDCOUNT);

		try{
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
			outputStream.write(ID);
			outputStream.write(QR);
			outputStream.write(Opcode);
			outputStream.write(AA);
			outputStream.write(TC);
			outputStream.write(RD);
			outputStream.write(RA);
			outputStream.write(Z);
			outputStream.write(RCODE);
			outputStream.write(QDCOUNT);
			outputStream.write(ANCOUNT);
			outputStream.write(NSCOUNT);
			outputStream.write(ARCOUNT);

			headerInByte = outputStream.toByteArray( );
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void decodeByte(byte[] header){
		System.out.println("\nDécodage de l'en-tête: ");
		if(header.length == this.LONGUEUR_HEADER){
			this.ID = Arrays.copyOfRange(header, 0, 16);
			
			System.out.println(" ID = " + fromByteArray(this.ID));
			
			this.QR = new byte[]{header[16]};
			boolean testRecursif = QR[0]!=0;
			
			System.out.println(" QR = " + testRecursif);
			
			Opcode = Arrays.copyOfRange(header, 17, 21);
			
			System.out.println(" OpCode = " + fromByteArray(this.Opcode));
			
			this.AA = new byte[]{header[21]};
			this.TC = new byte[]{header[22]};
			this.RD = new byte[]{header[23]};
			testRecursif = RD[0]!=0; // 1: récursif,
			
			System.out.println(" (recursif) RD = " + testRecursif);
			
			this.RA = new byte[]{header[24]};
			Z = Arrays.copyOfRange(header, 25, 28);
			RCODE = Arrays.copyOfRange(header, 28, 32);
			
			System.out.println(" RCODE = " + fromByteArray(this.RCODE));
			
			QDCOUNT = Arrays.copyOfRange(header, 32, 48);
			
			System.out.println(" QDCOUNT= " + fromByteArray(this.QDCOUNT));
			
			ANCOUNT = Arrays.copyOfRange(header, 48, 64);
			
			System.out.println(" ANCOUNT = " + fromByteArray(this.ANCOUNT));
			
			NSCOUNT = Arrays.copyOfRange(header, 64, 80);
			ARCOUNT = Arrays.copyOfRange(header, 80, 96);
		}else{
			System.out.println("Header corrompu.");
		}
	}

	public byte[] getHeaderInByte() {
		return headerInByte;
	}

	public void setHeaderInByte(byte[] headerInByte) {
		this.headerInByte = headerInByte;
	}

	byte[] toByteArray(int value, int longueur) {
		return  ByteBuffer.allocate(longueur).putInt(value).array();
	}

	int fromByteArray(byte[] bytes) {
		return ByteBuffer.wrap(bytes).getInt();
	}

	public int getRcode(){
		return fromByteArray(this.ID);
	}

	public int getQDCOUNT(){
		return fromByteArray(this.QDCOUNT);
	}
	public int getANCOUNT(){
		return fromByteArray(this.ANCOUNT);
	}
}