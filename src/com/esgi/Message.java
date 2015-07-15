package com.esgi;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class Message {
    public Header header;
    public Question question;
    public ArrayList<Response> responses;
    public int numberOfQuestions;
    public int numberOfResponses;

    public Message(int nbQuestions,int nbResponses){
    	numberOfQuestions = nbQuestions;
    	numberOfResponses = nbResponses;
        responses = new ArrayList<>();
    }
    
    public byte[] getMessageInByte(){
    	
		try{
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
			outputStream.write(header.getHeaderInByte());
			if(question!=null)
			
			outputStream.write(question.getQuestionInByte());	
			
			if(responses!=null)
			for(Response reponsesTemp : responses){
				outputStream.write(reponsesTemp.getResponseInByte());	
			}
			byte[] MessageInByte = outputStream.toByteArray( );
			return MessageInByte;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
    }
}