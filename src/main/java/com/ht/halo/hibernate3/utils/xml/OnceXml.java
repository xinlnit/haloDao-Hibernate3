package com.ht.halo.hibernate3.utils.xml;

import org.dom4j.Document;

public class OnceXml {
	  private long lastModified;
	  private  Document document;
	public long getLastModified() {
		return lastModified;
	}
	public OnceXml setLastModified(long lastModified) {
		this.lastModified = lastModified;
		return this;
	}
	public Document getDocument() {
		return document;
	}
	public OnceXml setDocument(Document document) {
		this.document = document;
		return this;
	}
	     
}
