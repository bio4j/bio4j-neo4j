/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ohnosequences.xml.api.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.stream.StreamResult;
import org.xml.sax.InputSource;

/**
 *
 * @author Pablo Pareja Tobes <ppareja@era7.com>
 */
public class XMLUtil {

    public static String prettyPrintXML(String xml, int indentAmount) throws TransformerConfigurationException, TransformerException {

        Transformer serializer = SAXTransformerFactory.newInstance().newTransformer();

        serializer.setOutputProperty(OutputKeys.INDENT, "yes");
        serializer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", String.valueOf(indentAmount));
        Source xmlSource = new SAXSource(new InputSource(new ByteArrayInputStream(xml.getBytes())));
        StreamResult res = new StreamResult(new ByteArrayOutputStream());
        serializer.transform(xmlSource, res);

        return new String(((ByteArrayOutputStream) res.getOutputStream()).toByteArray());
    }
}
