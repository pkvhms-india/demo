package com.varicent.common.query.xml;

import java.io.StringReader;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.varicent.common.query.domain.QueryDefinition;
import com.varicent.common.query.domain.SourceDefinition;

public class SourceDefinitionParser {
	public static SourceDefinition getSourceDefinition(XMLStreamReader streamReader) throws XMLStreamException {
		streamReader.nextTag();
		streamReader.nextTag();
		QueryDefinition query = QueryDefinitionParser.getQueryDefinition(streamReader);
		SourceDefinition source = new SourceDefinition(query);
		streamReader.nextTag();

		while (streamReader.getEventType() == 1) {
			streamReader.nextTag();
			String alias = XMLStreamUtility.getCurrentNodeValue(streamReader);
			String name = XMLStreamUtility.getCurrentNodeValue(streamReader);
			source.addSourceName(alias, name);
			streamReader.nextTag();
		}

		streamReader.nextTag();
		return source;
	}

	public static SourceDefinition getSourceDefinition(String xml) throws XMLStreamException {
		XMLInputFactory factory = XMLInputFactory.newInstance();
		XMLStreamReader streamReader = factory.createXMLStreamReader(new StringReader(xml));
		return getSourceDefinition(streamReader);
	}
}
