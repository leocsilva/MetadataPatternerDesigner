package util;

import java.io.File;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import br.unesp.repositorio.base.xmlschema.metadatapatterner.ObjectFactory;
import br.unesp.repositorio.base.xmlschema.metadatapatterner.Patterns;


public class UtilidadesXML {
	
	static JAXBContext context;
	static Unmarshaller unmarshaller;
	static Marshaller marshaller;
	
	static{
		try {
			context = JAXBContext.newInstance(ObjectFactory.class);
			unmarshaller = context.createUnmarshaller();
			marshaller = context.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		} catch (JAXBException e) {
			
		}
		
	}
	
	@SuppressWarnings("restriction")
	public static Patterns carregaXML(String string) throws JAXBException {
		 
		@SuppressWarnings("unchecked")
		JAXBElement<Patterns> element = (JAXBElement<Patterns>) unmarshaller.unmarshal(new File(string));
		Patterns patterns = element.getValue();
		return patterns;
	}
	
	public static String toXML(Patterns patterns) throws JAXBException{
		StringWriter xml = new StringWriter();
		marshaller.marshal(patterns, xml);
		return xml.toString();
	}
	
	public static void toXML(Patterns patterns,File file) throws JAXBException{
		StringWriter xml = new StringWriter();
		marshaller.marshal(patterns, file);
		
	}
	
	
}
