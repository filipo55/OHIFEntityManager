package com.mycompany.myapp.service;

//import org.apache.spark.SparkConf;
import com.google.gson.Gson;
import com.mycompany.myapp.jms.JmsConfig;
import com.mycompany.myapp.jms.MyMessageCreator;
import org.apache.activemq.command.ActiveMQMapMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import com.mycompany.myapp.*;

import javax.jms.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPException;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;


//import org.apache.spark.api.java.JavaPairRDD;
//import org.apache.spark.api.java.JavaRDD;
//import org.apache.spark.api.java.JavaSparkContext;
//import org.apache.spark.api.java.function.Function2;
//import org.apache.spark.api.java.function.PairFunction;


@Component
public class CalculationService {


    List<TwoDimensionSpatialCoordinate> vertices = new ArrayList<>();


     @Autowired
    JmsConfig jmsConfig;
//    SparkConf sparkConf;
//    JavaSparkContext javaSparkContext;

//
//    public CalculationService()
//    {
//        sparkConf = new SparkConf().setMaster("local[*]").setAppName("SparkLesionCalculation");
//        javaSparkContext = new JavaSparkContext(sparkConf);
//
//    }

    public double CalculateVolume(double area, int height)
    {
        return (double) area* (float) height;
    }

    public List<String> generateMeasurements(String xml) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        InputSource is = new InputSource(new StringReader(xml));
        Document doc = dBuilder.parse(is);

        NodeList imageAnnotations = doc.getElementsByTagName("ImageAnnotation");
        NodeList measurements = doc.getElementsByTagName("name");

        List<String> measurementsList = new ArrayList<>();
        for(int i =0; i< imageAnnotations.getLength();i++)
        {
            NodeList children = imageAnnotations.item(i).getChildNodes();
            for(int j =0; j< children.getLength();j++)
            {
                if(children.item(j).getAttributes() != null)
                {
                    if(children.item(j).getAttributes().getLength() > 0)
                    {
                        String name = children.item(j).getAttributes().item(0).getNodeValue();
                        if(name.contains("LESION") || name.contains("ORGAN")|| name.contains("segment") || name.contains("contour"))
                            measurementsList.add(name);
                    }
                }


            }
        }


        return measurementsList;
    }



    public double CalculateDataFromFile(String xml, String name) throws ParserConfigurationException, IOException, SAXException, SOAPException, JMSException {

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        InputSource is = new InputSource(new StringReader(xml));
        Document doc = dBuilder.parse(is);


        //THIS IS MAGIC -> DON'T EVENT TRY TO ANALYZE
        boolean compute = false;
        NodeList imageAnnotations = doc.getElementsByTagName("ImageAnnotation");
        for(int i =0; i< imageAnnotations.getLength();i++)
        {
            NodeList children = imageAnnotations.item(i).getChildNodes();
            for(int j =0; j< children.getLength();j++)
            {
                if(children.item(j).getAttributes() != null)
                {
                    if(children.item(j).getAttributes().getLength() > 0)
                    {
                        String measurementName = children.item(j).getAttributes().item(0).getNodeValue();
                        if(measurementName.equals(name))
                            compute = true;
                    }
                }

            }
            if(compute)
            {
                    if(children.item(9) != null)
                    {
                        if(children.item(9).getChildNodes().getLength()>0)
                        {
                            if(children.item(9).getChildNodes().item(1).getChildNodes().getLength()>10)
                            {

                                if(children.item(9).getChildNodes().item(1).getChildNodes().item(11).getChildNodes().getLength()>0)
                                {
                                    for(int j=0;j<children.item(9).getChildNodes().item(1).getChildNodes().item(11).getChildNodes().getLength();j++)
                                    {
                                        if(children.item(9).getChildNodes().item(1).getChildNodes().item(11).getChildNodes().item(j) != null)
                                        {
                                            if(children.item(9).getChildNodes().item(1).getChildNodes().item(11).getChildNodes().item(j).getChildNodes().getLength() > 0)
                                            {
                                                if(children.item(9).getChildNodes().item(1).getChildNodes().item(11).getChildNodes().item(j).getChildNodes().item(1).getAttributes().getLength() > 0)
                                                {
                                                    String coordinateIndex = children.item(9).getChildNodes().item(1).getChildNodes().item(11).getChildNodes().item(j).getChildNodes().item(1).getAttributes().item(0).getNodeValue();
                                                    String x = children.item(9).getChildNodes().item(1).getChildNodes().item(11).getChildNodes().item(j).getChildNodes().item(3).getAttributes().item(0).getNodeValue();
                                                    String y = children.item(9).getChildNodes().item(1).getChildNodes().item(11).getChildNodes().item(j).getChildNodes().item(5).getAttributes().item(0).getNodeValue();
                                                    TwoDimensionSpatialCoordinate temp = new TwoDimensionSpatialCoordinate();
                                                    temp.setCoordinateIndex(Integer.parseInt(coordinateIndex));
                                                    temp.setX(Float.parseFloat(x));
                                                    temp.setY(Float.parseFloat(y));
                                                    vertices.add(temp);
                                                }
                                            }


                                        }

                                    }
                                }


                            }
                        }
                    }


                break;
            }
        }



        //JavaRDD<TwoDimensionSpatialCoordinate> coordinateJavaRDD = javaSparkContext.parallelize(vertices);


        String json = new Gson().toJson(vertices);
        jmsConfig.jmsTemplate().convertAndSend("VerticesQueue",json);


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
