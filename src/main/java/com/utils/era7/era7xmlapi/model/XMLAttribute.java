package com.era7.era7xmlapi.model;


import org.jdom2.Attribute;
import org.jdom2.Namespace;

import com.era7.era7xmlapi.interfaces.IAttribute;
import com.era7.era7xmlapi.interfaces.INameSpace;

public class XMLAttribute implements IAttribute {

	protected org.jdom2.Attribute attribute;
	
	/**
	 * 
	 * @param attr
	 */
	public XMLAttribute(org.jdom2.Attribute attr){
		this.attribute = attr;
	}
	/**
	 * 
	 * @param name Attribute name
	 * @param value Attribute value
	 */
	public XMLAttribute(String name, String value){
		this.attribute = new org.jdom2.Attribute(name,value);
	}
	
	/**
	 * 
	 * @param name Attribute name
	 * @param value Attribute value
	 * @param ns Namespace
	 */
	public XMLAttribute(String name, String value, INameSpace ns){
		this.attribute = new org.jdom2.Attribute(name,value,Namespace.getNamespace(ns.getPrefix(),ns.getUri()));
	}
	
//	/**
//	 * 
//	 * @param value
//	 * @throws XMLElementException 
//	 * @throws IOException 
//	 * @throws JDOMException 
//	 * @throws ParserConfigurationException 
//	 */
//	public XMLAttribute(XmlValue value) throws XMLElementException{
//		try {
//			if(value.getNodeType() == XmlValue.ATTRIBUTE_NODE){
//				this.attribute = new org.jdom2.Attribute(value.getNodeName(),
//														value.getNodeValue(),
//														Namespace.getNamespace(value.getPrefix(), value.getNamespaceURI()));
//			}else{
//				throw new XMLElementException(XMLElementException.WRONG_CONSTRUCTOR_PARAMETER,null);
//			}
//		} catch (XmlException e) {
//			e.printStackTrace();
//			throw new XMLElementException(XMLElementException.WRONG_CONSTRUCTOR_PARAMETER,null);
//		}
//	}

	@Override
	public String getName() {
		return this.attribute.getName();
	}

	@Override
	public INameSpace getNameSpace() {
		return new NameSpace(this.attribute.getNamespace());
	}

	@Override
	public String getText() {
		return this.attribute.getValue();
	}

	@Override
	public void setName(String newValue) {
		this.attribute.setName(newValue);
	}

	@Override
	public void setNameSpace(INameSpace newValue) {
		this.attribute.setNamespace(Namespace.getNamespace(newValue.getPrefix(), newValue.getUri()));
	}

	@Override
	public void setText(String newValue) {
		this.attribute.setValue(newValue);
	}
	@Override
	public Attribute asJdomAttribute() {
		return this.attribute;
	}
	/*
	@Override
	public XmlValue asXmlvalue() throws XmlException {
		XmlValue temp = new XmlValue(XmlValue.ATTRIBUTE_NODE,this.getName());
		
		return temp;
	}*/
	
	

}
