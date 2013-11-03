package com.example.TrafficJam;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Xml;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
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
	Button nextButton;
	String setup;
	int puzzleNumber;

    private PuzzleAdapter mPuzzleAdapter =  new PuzzleAdapter( this );

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

	    Bundle b = getIntent().getExtras();
	    setup = b.getString("setup");
	    puzzleNumber = b.getInt("puzzleNumber");

	    parseSetup(setup);

        setContentView(R.layout.play);
        m_gv = (PlayView) findViewById( R.id.play_view);
	    m_gv.setCars(cars);

	    nextButton = (Button)findViewById(R.id.next_button);
	    if(!m_gv.mWin){
		    nextButton.setVisibility(View.INVISIBLE);
	    }
	    else
	    {
		    nextButton.setVisibility(View.VISIBLE);
	    }

	    nextButton.setOnClickListener( new View.OnClickListener() {
		    @Override
		    public void onClick(View view) {
			    System.out.println("NEXT PUZZLE");
/*
			    Intent intent = new Intent(MyActivity.this, PlayActivity.class);
			    Bundle bundle = new Bundle();
//	            String setup = "(H 1 2 2), (V 0 1 3), (H 0 0 2), (V 3 1 3), (H 2 5 3), (V 0 4 2), (H 4 4 2), (V 5 0 3)";
			    bundle.putString("setup", currentPuzzle.setup);
			    intent.putExtras(bundle);
			    startActivity(intent);*/
		    }
	    });

	    TextView puzzle_title = (TextView) findViewById(R.id.puzzle_title);
	    puzzle_title.setText("Puzzle " + puzzleNumber);
    }

	public void setButtonVisibility()
	{
		if(!m_gv.mWin){
			nextButton.setVisibility(View.INVISIBLE);
		}
		else
		{
			nextButton.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onPause()
	{
		super.onPause();

		/*for(Car car : m_gv.getCars())
		{
			System.out.println("car " + car.id + " car x: " + car.x + " car y: " + car.y);
		}*/

//		editCurrentPuzzle();
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
	        //TODO: show buttons
        }
    }

    public void updateScore(int score){
        TextView textMoves = (TextView) findViewById(R.id.textMoves);
        textMoves.setText("Moves : "+score);
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

    public void setPuzzleAsFinished(){
        mPuzzleAdapter.updatePuzzle(puzzleNumber,true);
    }
}