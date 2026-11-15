package com.varicent.common.query.xml;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

public class XMLStreamUtility {
	public static String getCurrentNodeValue(XMLStreamReader streamReader) throws XMLStreamException {
		StringBuilder sb = new StringBuilder();
		streamReader.next();

		while (streamReader.getEventType() == 4) {
			sb.append(streamReader.getText());
			streamReader.next();
		}

		streamReader.nextTag();
		return sb.toString();
	}

	public static String removeInvalidXMLCharacters(String xml) {
		return xml != null && xml.length() != 0 ? new String(removeInvalidXMLCharacters(xml.toCharArray())) : xml;
	}

	public static char[] removeInvalidXMLCharacters(char[] xmlCharArray) {
		if (xmlCharArray != null && xmlCharArray.length != 0) {
			for (int i = 0; i < xmlCharArray.length; ++i) {
				char c = xmlCharArray[i];
				if (!isValidXMLCharacter(c)) {
					xmlCharArray[i] = '?';
				}
			}

			return xmlCharArray;
		} else {
			return xmlCharArray;
		}
	}

	public static boolean isValidXMLCharacter(char c) {
		return c == '\t' || c == '\n' || c == '\r' || c >= ' ' && c <= '\ud7ff' || c >= '\ue000' && c <= '�'
				|| c >= 65536 && c <= 1114111;
	}
}
