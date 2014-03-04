package com.ohnosequences.xml.api.model;

import java.io.CharArrayReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.jdom2.filter.ElementFilter;
import javax.xml.parsers.ParserConfigurationException;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.XMLOutputter;
import org.xml.sax.InputSource;

import com.ohnosequences.xml.api.interfaces.IElement;
import com.ohnosequences.xml.api.interfaces.INameSpace;

public class XMLElement implements IElement {

    protected static XMLOutputter outputter = new XMLOutputter();
    protected Element root = null;
    protected Document doc = null;
    protected SAXBuilder builder = null;

    /**
     *
     * @param elem
     */
    public XMLElement(Element elem) {
        root = elem;
    }

    /**
     * Constructor
     * @param value String representing the XML document
     * @throws Exception
     */
    public XMLElement(String value) throws Exception {
        buildElementFromString(value);
    }

    /**
     * Constructor
     * @param tagName Tag name
     * @param ns Namespace
     */
    public XMLElement(String tagName, INameSpace ns) {
        root = new Element(tagName, Namespace.getNamespace(ns.getPrefix(), ns.getUri()));
    }

    private void initDocBuilder() throws ParserConfigurationException {
        if (builder == null) {
            builder = new SAXBuilder();
        }
    }

    private void buildElementFromString(String value) throws ParserConfigurationException, JDOMException, IOException {
        initDocBuilder();

        Reader reader = new CharArrayReader(value.toCharArray());
        doc = builder.build(new InputSource(reader));
        root = doc.getRootElement();
    }
//
//    /**
//     *
//     * @param value
//     * @throws XMLElementException
//     * @throws IOException
//     * @throws JDOMException
//     * @throws ParserConfigurationException
//     */
//    public XMLElement(XmlValue value) throws XMLElementException, ParserConfigurationException, JDOMException, IOException {
//        try {
//            if (value.getNodeType() == XmlValue.ELEMENT_NODE) {
//                buildElementFromString(value.asString());
//            } else {
//                throw new XMLElementException(XMLElementException.WRONG_CONSTRUCTOR_PARAMETER, null);
//            }
//        } catch (XmlException e) {
//            e.printStackTrace();
//            throw new XMLElementException(XMLElementException.WRONG_CONSTRUCTOR_PARAMETER, null);
//        }
//    }

    @Override
    public String getName() {
        return root.getName();
    }

    @Override
    public NameSpace getNameSpace() {
        return new NameSpace(root.getNamespace());
    }

    @Override
    public String getText() {
        return root.getText();
    }

    @Override
    public void setName(String newValue) {
        this.root.setName(newValue);
    }

    @Override
    public void setNameSpace(INameSpace newValue) {
        this.root.setNamespace(Namespace.getNamespace(newValue.getPrefix(), newValue.getUri()));
    }

    @Override
    public void setText(String newValue) {
        this.root.setText(newValue);
    }

    @Override
    public List<XMLAttribute> getAttributes() {
        List<XMLAttribute> list = new ArrayList<XMLAttribute>();
        for (Object attr : this.root.getAttributes()) {
            XMLAttribute temp = new XMLAttribute((org.jdom2.Attribute) (attr));
            list.add(temp);
        }
        return list;
    }

    @Override
    public List<XMLElement> getChildren() {
        List<XMLElement> list = new ArrayList<XMLElement>();
        for (Object elem : this.root.getChildren()) {
            XMLElement temp = new XMLElement((org.jdom2.Element) (elem));
            list.add(temp);
        }
        return list;
    }

    @Override
    public List<XMLElement> getChildrenWith(INameSpace ns) {
        List<XMLElement> list = new ArrayList<XMLElement>();

        for (Object elem : this.root.getChildren()) {

            org.jdom2.Element e = (org.jdom2.Element) (elem);
            if (e.getNamespacePrefix().equals(ns.getPrefix()) && e.getNamespaceURI().equals(ns.getUri())) {
                XMLElement temp = new XMLElement(e);
                list.add(temp);
            }

        }
        return list;
    }

    @Override
    public List<XMLElement> getChildrenWith(String name) {
        List<XMLElement> list = new ArrayList<XMLElement>();

        ElementFilter filter = new ElementFilter(name);

        for (Object elem : this.root.getContent(filter)) {
            XMLElement temp = new XMLElement((org.jdom2.Element) (elem));
            list.add(temp);

        }
        return list;
    }

    @Override
    public List<XMLAttribute> getAttributeWith(String name) {
        List<XMLAttribute> list = new ArrayList<XMLAttribute>();

        for (Object attr : this.root.getAttributes()) {

            org.jdom2.Attribute a = (org.jdom2.Attribute) (attr);
            if (a.getName().equals(name)) {
                XMLAttribute temp = new XMLAttribute(a);
                list.add(temp);
            }

        }
        return list;
    }

    @Override
    public List<XMLAttribute> getAttributeWith(INameSpace ns) {
        List<XMLAttribute> list = new ArrayList<XMLAttribute>();

        for (Object attr : this.root.getChildren()) {

            org.jdom2.Attribute a = (org.jdom2.Attribute) (attr);
            if (a.getNamespacePrefix().equals(ns.getPrefix()) && a.getNamespaceURI().equals(ns.getUri())) {
                XMLAttribute temp = new XMLAttribute(a);
                list.add(temp);
            }

        }
        return list;
    }

    @Override
    public void setAttributes(List<XMLAttribute> newValue) {
        List<org.jdom2.Attribute> list = new ArrayList<org.jdom2.Attribute>();
        for (XMLAttribute attr : newValue) {
            list.add(new org.jdom2.Attribute(attr.getName(), attr.getText(), attr.getNameSpace().asJdomNamespace()));
        }
        this.root.setAttributes(list);
    }

    @Override
    public void setChildren(List<XMLElement> newValue) {
        //Removing previous children
        for (Object elem : this.root.getChildren()) {
            this.root.removeChildren(((Element) elem).getName());
        }
        //Setting new children
        for (IElement elem : newValue) {
            this.root.addContent(elem.asJDomElement());
        }
    }

    /**
     *	Detach
     *
     *	Detaches the root element so it can be added to other document
     */
    public void detach() {
        root.detach();
    }

    public Element getRoot() {
        return root;
    }

    public void setRoot(Element root) {
        this.root = root;
    }

    /**
     * Returns the cdata text of the first element with tag name = 'tagName'
     * If there is no such node it returns the empty string
     * @param tagName
     */
    protected String getNodeText(String tagName) {
        Element element = this.root.getChild(tagName);

        if (element != null) {
            return element.getText();
        } else {
            return null;
        }
    }

    /**
     * Sets the 'text' for the element with tag node 'tagNameNode' creating the
     * node in case it does not exist yet
     * @param tagNameNode
     */
    protected void setNodeText(String tagNameNode, String text) {
        Element element = this.root.getChild(tagNameNode);

        if (element == null) {
            element = new Element(tagNameNode);
            this.root.addContent(element);
        }
        element.setText(text);
    }

    /**
     * Returns the text of the attribute with name = 'name'
     * If there is no such attribute it returns the empty string
     * @param name
     */
    protected String getAttributeText(String name) {
        return root.getAttributeValue(name);
    }
    /**
     * Sets the 'text' for the attribute with name 'name'
     * @param name
     * @parm text
     */
    protected void setAttributeText(String name, String text) {
       root.setAttribute(name, text);
    }

    /**
     *  To string
     */
    @Override
    public String toString() {
        return outputter.outputString(root);
    }

    @Override
    public Element asJDomElement() {
        return this.root;
    }
//
//    @Override
//    public XmlValue asXmlvalue() throws XmlException, FileNotFoundException {
//        XmlDocument tempDoc = new XmlManager().createDocument();
//        tempDoc.setContent(this.toString());
//        XmlValue temp = new XmlValue(tempDoc);
//        return temp;
//    }

    @Override
    public void addChild(XMLElement element) {
        this.root.addContent(element.asJDomElement());
    }

    @Override
    public void addChildren(List<XMLElement> list) {
        for (XMLElement elem : list) {
            this.root.addContent(elem.asJDomElement());
        }
    }

}
