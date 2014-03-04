package com.era7.era7xmlapi.interfaces;

public interface INameSpace {

	String getPrefix();
    String getUri();

    void setPrefix(String newValue);
    void setUri(String newValue);
	
    org.jdom2.Namespace asJdomNamespace();
}
