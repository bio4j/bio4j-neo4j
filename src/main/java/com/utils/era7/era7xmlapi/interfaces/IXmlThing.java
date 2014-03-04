package com.era7.era7xmlapi.interfaces;


public interface IXmlThing {

	String getName();
    String getText();
    INameSpace getNameSpace();

    void setText(String newValue);
    void setName(String newValue);
    void setNameSpace(INameSpace newValue);	
    
}
