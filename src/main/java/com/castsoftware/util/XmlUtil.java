package com.castsoftware.util;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.sax.SAXSource;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;

public class XmlUtil {
    public static void convertToXml(String fileName, Object source, Class<?>... type) 
    throws JAXBException, IOException{
        StringWriter sw = new StringWriter();
        JAXBContext context = JAXBContext.newInstance(type);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.marshal(source, sw);
	 	BufferedWriter out = new BufferedWriter(new FileWriter(fileName));
	 	out.write(sw.toString());
	 	out.close();
    }
    
    public static Object convertfromXml(String fileName, Object source, Class<?>... type) 
    		throws JAXBException, SAXException, ParserConfigurationException, FileNotFoundException{
        JAXBContext context = JAXBContext.newInstance(type);
        
        //Prevent DTD from being checked in case they are web ones and we don't have internet access...
        SAXParserFactory parser = SAXParserFactory.newInstance();        
        parser.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
        parser.setFeature("http://xml.org/sax/features/validation", false);
        
        XMLReader xmlReader = parser.newSAXParser().getXMLReader();
        InputSource inputSource = new InputSource(new FileReader(fileName));
        SAXSource saxSource = new SAXSource(xmlReader, inputSource);
        
        Unmarshaller unmarshaller = context.createUnmarshaller();
        source = unmarshaller.unmarshal(saxSource);
        
        return source;
    }
    
}
