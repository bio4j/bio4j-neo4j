package com.era7.era7xmlapi.interfaces;

import com.era7.era7xmlapi.model.XMLAttribute;
import com.era7.era7xmlapi.model.XMLElement;
import java.util.List;

public interface IElement extends IXmlThing {

    List<XMLElement> getChildren();
    List<XMLAttribute> getAttributes();

    void setChildren(List<XMLElement> newValue);
    void setAttributes(List<XMLAttribute> newValue);

    
    List<XMLElement> getChildrenWith(INameSpace ns);
    List<XMLElement> getChildrenWith(String name);

    List<XMLAttribute> getAttributeWith(String name);
    List<XMLAttribute> getAttributeWith(INameSpace ns);
    
    void addChild(XMLElement element);
    void addChildren(List<XMLElement> list);
    
    org.jdom2.Element asJDomElement();
//    XmlValue asXmlvalue() throws Exception;
	
}
