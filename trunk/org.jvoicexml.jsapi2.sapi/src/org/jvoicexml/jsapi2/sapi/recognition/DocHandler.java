package org.jvoicexml.jsapi2.sapi.recognition;

import java.util.Hashtable;

public interface DocHandler {
  void startElement(String tag, Hashtable h) throws Exception;
  void endElement(String tag) throws Exception;
  void startDocument() throws Exception;
  void endDocument() throws Exception;
  void text(String str) throws Exception;
}
