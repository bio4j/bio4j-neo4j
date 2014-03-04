package com.ohnosequences.xml.api.interfaces;


public interface IXmlThing {

	String getName();
    String getText();
    INameSpace getNameSpace();

    void setText(String newValue);
    void setName(String newValue);
    void setNameSpace(INameSpace newValue);	
    
}
