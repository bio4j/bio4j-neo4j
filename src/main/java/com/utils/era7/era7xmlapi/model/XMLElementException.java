package com.era7.era7xmlapi.model;

import com.era7.era7xmlapi.interfaces.IXmlThing;

public class XMLElementException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static String WRONG_TAG_NAME = "Wrong tag name";
	public static String WRONG_CONSTRUCTOR_PARAMETER = "The parameter passed as an argument for the constructor was unexpected";
	
	protected String exception;
	protected IXmlThing source = null;;
	
	public XMLElementException(){
		super();
		exception="Unknown";
	}
	
	public XMLElementException(String exp,IXmlThing value){
		super(exp);
		this.exception=exp;
		this.source = value;
	}
	
	public String getException(){
		return this.exception;
	}

	public IXmlThing getSource() {
		return source;
	}
	
}
