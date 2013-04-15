package com.csc591.utils;

import java.util.ArrayList;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class GoogleDistanceMatrixReader {

	public ArrayList<Integer> getDurationValue(Document doc)
	{
		 XPathFactory factory = XPathFactory.newInstance();
		 XPath xpath = factory.newXPath();

		/* try {
			 DOMSource source = new DOMSource(doc);
			 StringWriter xmlAsWriter = new StringWriter();
			 StreamResult result = new StreamResult(xmlAsWriter);
			 TransformerFactory.newInstance().newTransformer().transform(source, result);
			 StringReader xmlReader = new StringReader(xmlAsWriter.toString());
			 InputSource inputXML = new InputSource(xmlReader);
			 
			 // Because the evaluator may return multiple entries, we specify that the expression
		      // return a NODESET and place the result in a NodeList.
		      NodeList nodes = (NodeList) xpath.evaluate("/DistanceMatrixResponse/row//element/duration", inputXML, XPathConstants.NODESET);

		      // We can then iterate over the NodeList and extract the content via getTextContent().
		      // NOTE: this will only return text for element nodes at the returned context.
		      for (int i = 0, n = nodes.getLength(); i < n; i++) {
		        String nodeString = nodes.item(i).getTextContent();
		        System.out.print(nodeString);
		        System.out.print("\n");
		      }
		  */
		 
		 NodeList nodes = doc.getElementsByTagName("duration");
	     // We can then iterate over the NodeList and extract the content via getTextContent().
	      // NOTE: this will only return text for element nodes at the returned context.
	      String nodeString="";
	      ArrayList<Integer> walkingTimes = new ArrayList<Integer>();
	      
	      for(int i = 0; i < nodes.getLength(); i++)
	      {
	    	  Node node1 = nodes.item(i);
	    	  
	    	  NodeList nodeList2 = node1.getChildNodes();
	    	  Node node2 = nodeList2.item(getNodeIndex(nodeList2, "value"));
	    	  nodeString = node2.getTextContent();
	    	  walkingTimes.add(Integer.parseInt(nodeString.toString()));
	      }
	      return walkingTimes;
	}
	
	private int getNodeIndex(NodeList nl, String nodename) {
		for(int i = 0 ; i < nl.getLength() ; i++) {
			if(nl.item(i).getNodeName().equals(nodename))
				return i;
		}
		return -1;
	}
}