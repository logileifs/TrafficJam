package com.example.TrafficJam;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Xml;
import android.view.Gravity;
import android.widget.Toast;
import org.w3c.dom.*;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlSerializer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Lenovo
 * Date: 31.10.2013
 * Time: 14:27
 * To change this template use File | Settings | File Templates.
 */
public class PlayActivity extends Activity {
    PlayView  m_gv;
	List<Car> cars = new ArrayList<Car>();
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

	    Bundle b = getIntent().getExtras();
	    String setup = b.getString("setup");

	    parseSetup(setup);

        setContentView(R.layout.play);
        m_gv = (PlayView) findViewById( R.id.play_view);
	    m_gv.setCars(cars);
    }

	@Override
	public void onPause()
	{
		super.onPause();

		/*for(Car car : m_gv.getCars())
		{
			System.out.println("car " + car.id + " car x: " + car.x + " car y: " + car.y);
		}*/

		writeXML2();
//		editCurrentPuzzle();
		//TODO: write to XML
	}

	@Override
	public void onStop()
	{
		super.onStop();
		//TODO: write current puzzle setup to XML file
		System.out.println("On Stop");
	}

    public void showToast(String message) {
        if (!isFinishing()) {
            Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }

	public void parseSetup(String setup)
	{
		Car car = null;
		int count = 0;
		int id = 0;
		boolean newCar = false;

		for(int i=0; i<setup.length(); i++)
		{
			if(setup.charAt(i) == '(') //new car begins
			{
				car = new Car();
				newCar = true;
			}
			if(newCar)
			{
				if(setup.charAt(i) == 'H')
					car.alignment = 'H';
				if(setup.charAt(i) == 'V')
					car.alignment = 'V';

				if(Character.isDigit(setup.charAt(i)))
				{
					if(count == 0)
					{
						car.x = Integer.parseInt(String.valueOf(setup.charAt(i)));
						count++;
					}
					else if(count == 1)
					{
						car.y = Integer.parseInt(String.valueOf(setup.charAt(i)));
						count++;
					}
					else if(count == 2)
					{
						car.length = Integer.parseInt(String.valueOf(setup.charAt(i)));
						count++;
					}
				}
			}
			if(setup.charAt(i) == ')')  //new car ends
			{
				car.mainCar = (id == 0 ? true : false); //car.mainCar = true;
				car.id = id;
				cars.add(car);
				newCar = false;
				count = 0;
				id++;
			}
		}
	}

	public void writeXML2()
	{
		final String xmlFile="userData";
		String userNAme="username";
		String password="password";
		try {
			FileOutputStream fileos= getApplicationContext().openFileOutput(xmlFile, Context.MODE_APPEND);
			XmlSerializer xmlSerializer = Xml.newSerializer();
			StringWriter writer = new StringWriter();
			xmlSerializer.setOutput(writer);
			xmlSerializer.startDocument("UTF-8",true);
			xmlSerializer.startTag(null, "userData");
			xmlSerializer.startTag(null, "userName");
			xmlSerializer.text(userNAme);
			xmlSerializer.endTag(null,"userName");
			xmlSerializer.startTag(null,"password");
			xmlSerializer.text(password);
			xmlSerializer.endTag(null, "password");
			xmlSerializer.endTag(null, "userData");
			xmlSerializer.endDocument();
			xmlSerializer.flush();
			String dataWrite=writer.toString();
			fileos.write(dataWrite.getBytes());
			fileos.close();
		} catch (FileNotFoundException e) {
// TODO Auto-generated catch block
			System.out.println("FileNotFoundException");
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
// TODO Auto-generated catch block
			System.out.println("IllegalArgumentException");
			e.printStackTrace();
		} catch (IllegalStateException e) {
// TODO Auto-generated catch block
			System.out.println("IllegalStateException");
			e.printStackTrace();
		} catch (IOException e) {
// TODO Auto-generated catch block
			System.out.println("IOException");
			e.printStackTrace();
		}
	}

	public void writeXML()
	{
		System.out.println("writeXML function");

		try
		{
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

			// root elements
			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("company");
			doc.appendChild(rootElement);

			// staff elements
			Element staff = doc.createElement("Staff");
			rootElement.appendChild(staff);

			// set attribute to staff element
			Attr attr = doc.createAttribute("id");
			attr.setValue("1");
			staff.setAttributeNode(attr);

			// shorten way
			// staff.setAttribute("id", "1");

			// firstname elements
			Element firstname = doc.createElement("firstname");
			firstname.appendChild(doc.createTextNode("yong"));
			staff.appendChild(firstname);

			// lastname elements
			Element lastname = doc.createElement("lastname");
			lastname.appendChild(doc.createTextNode("mook kim"));
			staff.appendChild(lastname);

			// nickname elements
			Element nickname = doc.createElement("nickname");
			nickname.appendChild(doc.createTextNode("mkyong"));
			staff.appendChild(nickname);

			// salary elements
			Element salary = doc.createElement("salary");
			salary.appendChild(doc.createTextNode("100000"));
			staff.appendChild(salary);

			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			//StreamResult result = new StreamResult(new File("~/tmp/file.xml"));

			// Output to console for testing
			StreamResult result = new StreamResult(System.out);

			System.out.println("transformer");

			transformer.transform(source, result);

			System.out.println("File saved!");

		}
		catch (ParserConfigurationException pce)
		{
			System.out.println("ParserConfiguration Exception " + pce.getMessage());
			pce.printStackTrace();
		}
		catch (TransformerException tfe)
		{
			System.out.println("Transformer Exception");
			tfe.printStackTrace();
		}
	}

	public void editCurrentPuzzle()
	{
		try {
			String filepath = "\\tmp\\file.xml";
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(filepath);

			// Get the root element
			Node company = doc.getFirstChild();

			// Get the staff element , it may not working if tag has spaces, or
			// whatever weird characters in front...it's better to use
			// getElementsByTagName() to get it directly.
			// Node staff = company.getFirstChild();

			// Get the staff element by tag name directly
			Node staff = doc.getElementsByTagName("staff").item(0);

			// update staff attribute
			NamedNodeMap attr = staff.getAttributes();
			Node nodeAttr = attr.getNamedItem("id");
			nodeAttr.setTextContent("2");

			// append a new node to staff
			Element age = doc.createElement("age");
			age.appendChild(doc.createTextNode("28"));
			staff.appendChild(age);

			// loop the staff child node
			NodeList list = staff.getChildNodes();

			for (int i = 0; i < list.getLength(); i++) {

				Node node = list.item(i);

				// get the salary element, and update the value
				if ("salary".equals(node.getNodeName())) {
					node.setTextContent("2000000");
				}

				//remove firstname
				if ("firstname".equals(node.getNodeName())) {
					staff.removeChild(node);
				}

			}

			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(filepath));
			transformer.transform(source, result);

			System.out.println("Done");

		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (TransformerException tfe) {
			tfe.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} catch (SAXException sae) {
			sae.printStackTrace();
		}
	}
}