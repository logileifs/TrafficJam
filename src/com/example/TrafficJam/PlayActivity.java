package com.example.TrafficJam;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
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

		writeXML();
		//TODO: write to XML
	}

	@Override
	public void onStop()
	{
		super.onStop();
		//TODO: write current puzzle setup to XML file
		System.out.println("On Stop");
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

	public void writeXML()
	{
		System.out.println("writeXML function");
//		String path = getClass().getClassLoader().getResource(".").getPath();
/*
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		URL url = classLoader.getResource("res/xml/");
		File file = null;
		try {
			file = new File(url.toURI());
		} catch (URISyntaxException e) {
			e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
		}

		System.out.println("path: " + file);*/

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
			StreamResult result = new StreamResult(new File("~/GitRepository/TrafficJam/res/xml/file.xml"));

			// Output to console for testing
			// StreamResult result = new StreamResult(System.out);

			System.out.println("transformer");

			transformer.transform(source, result);

			System.out.println("File saved!");

		}
		catch (ParserConfigurationException pce)
		{
			System.out.println("ParserConfiguration Exception");
			pce.printStackTrace();
		}
		catch (TransformerException tfe)
		{
			System.out.println("Transformer Exception");
			tfe.printStackTrace();
		}
	}
}