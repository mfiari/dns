package com.esgi;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class Message {
    public Header header;
    public Question question;
    public ArrayList<Response> responses;
    public int numberOfQuestions;
    public int numberOfResponses;

    public Message(int _numberOfQuestions,int _numberOfResponses){
    	numberOfQuestions = _numberOfQuestions;
    	numberOfResponses = _numberOfResponses;
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