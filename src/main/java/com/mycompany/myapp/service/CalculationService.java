package com.mycompany.myapp.service;

import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

@Component
public class CalculationService {


    List<TwoDimensionSpatialCoordinate> vertices = new ArrayList<>();

    public double CalculateVolume(double area, int height)
    {
        return (double) area* (float) height;
    }


    public double CalculateDataFromFile(String xml) throws ParserConfigurationException, IOException, SAXException {

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        InputSource is = new InputSource(new StringReader(xml));
        Document doc = dBuilder.parse(is);

        NodeList coordinates = doc.getElementsByTagName("TwoDimensionSpatialCoordinate");
        for (int i = 0; i < coordinates.getLength(); i++) {

            Node nNode = coordinates.item(i);

            System.out.println("\nCurrent Element :" + nNode.getNodeName());

            if (nNode.getNodeType() == Node.ELEMENT_NODE)
            {

                Element eElement = (Element) nNode;


                System.out.println("coordinateIndex : " + eElement.getChildNodes().item(1).getAttributes().item(0).getNodeValue());
                System.out.println("x : " + eElement.getChildNodes().item(3).getAttributes().item(0).getNodeValue());
                System.out.println("y : " + eElement.getChildNodes().item(5).getAttributes().item(0).getNodeValue());

                TwoDimensionSpatialCoordinate temp = new TwoDimensionSpatialCoordinate();
                temp.setCoordinateIndex(Integer.parseInt(eElement.getChildNodes().item(1).getAttributes().item(0).getNodeValue()));
                temp.setX(Float.parseFloat(eElement.getChildNodes().item(3).getAttributes().item(0).getNodeValue()));
                temp.setY(Float.parseFloat(eElement.getChildNodes().item(5).getAttributes().item(0).getNodeValue()));


                vertices.add(temp);
            }
        }

        // Initialze area
        double area = 0.0;

        // Calculate value of shoelace formula
        int j = vertices.size() - 1;
        for (int i = 0; i < vertices.size(); i++)
        {
            area += (vertices.get(j).getX() + vertices.get(i).getX()) * (vertices.get(j).getY() - vertices.get(i).getY());
            j = i;  // j is previous vertex to i
        }

        System.out.println("AREA: " + area);

        // Return absolute value
        return java.lang.Math.abs(area / 2.0);
    }

}
