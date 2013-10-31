package com.example.TrafficJam;

import android.app.Activity;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.util.Xml;
import android.view.Gravity;
import android.view.View;
import android.widget.*;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: Lenovo
 * Date: 31.10.2013
 * Time: 14:28
 * To change this template use File | Settings | File Templates.
 */
public class PuzzlesActivity extends Activity {
	ListView listView ;
	String[] values;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
	    setContentView(R.layout.puzzles);

	    // Get ListView object from xml
	    //listView = (ListView) findViewById(R.id.list);

	    TextView myXmlContent = (TextView)findViewById(R.id.challenge_classic40);
	    String stringXmlContent;
	    try {
		    stringXmlContent = getEventsFromAnXML(this);
		    myXmlContent.setText(stringXmlContent);
	    } catch (XmlPullParserException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
	    } catch (IOException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
	    }

	    // Defined Array values to show in ListView
	    values = new String[] { "Android List View",
			    "Adapter implementation",
			    "Simple List View In Android",
			    "Create List View Android",
			    "Android Example",
			    "List View Source Code",
			    "List View Array Adapter",
			    "Android Example List View"
	    };

	    // Define a new Adapter
	    // First parameter - Context
	    // Second parameter - Layout for the row
	    // Third parameter - ID of the TextView to which the data is written
	    // Forth - the Array of data
	    //ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, android.R.id.text1, values);

	    // Assign adapter to ListView
	    //listView.setAdapter(adapter);

	    // ListView Item Click Listener
/*	    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
		    @Override
		    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			    // ListView Clicked item index
			    int itemPosition     = position;

			    // ListView Clicked item value
			    String  itemValue    = (String) listView.getItemAtPosition(position);

			    // Show Alert
			    Toast.makeText(getApplicationContext(), "Position :"+itemPosition+"  ListItem : " +itemValue , Toast.LENGTH_LONG).show();
		    }
		});*/
    }

	private void showToast(String message) {
		if (!isFinishing())
		{
			Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
		}
	}

	private String getEventsFromAnXML(Activity activity) throws XmlPullParserException, IOException
	{
		boolean level = false;
		StringBuffer stringBuffer = new StringBuffer();
		Resources res = activity.getResources();
		XmlResourceParser xpp = res.getXml(R.xml.challenge_classic40);
		xpp.next();
		int eventType = xpp.getEventType();
		while (eventType != XmlPullParser.END_DOCUMENT)
		{
//			if(eventType == XmlPullParser.START_DOCUMENT)
//			{
//				stringBuffer.append("--- Start XML ---");
//			}
			if(eventType == XmlPullParser.START_TAG)
			{
				if(xpp.getName().equals("puzzle"))
				{
//					values.add(xpp.getAttributeValue(0));
//					values[0] = xpp.getAttributeValue(0);
					stringBuffer.append("\nPUZZLE " + xpp.getIdAttribute());
				}
				if(xpp.getName().equals("level"))
				{
					level = true;
				}
				//stringBuffer.append("\nSTART_TAG: "+xpp.getName());
			}
			else if(eventType == XmlPullParser.END_TAG)
			{
				//stringBuffer.append("\nEND_TAG: "+xpp.getName());
			}
			else if(eventType == XmlPullParser.TEXT)
			{
				if(level)
					stringBuffer.append("\nLEVEL " + xpp.getText());
				//stringBuffer.append("\nTEXT: "+xpp.getText());
				level = false;
			}
			eventType = xpp.next();
		}
		stringBuffer.append("\n--- End XML ---");
		return stringBuffer.toString();
	}
}