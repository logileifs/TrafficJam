package com.example.TrafficJam;

import android.app.Activity;
import android.content.Intent;
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
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Lenovo
 * Date: 31.10.2013
 * Time: 14:28
 * To change this template use File | Settings | File Templates.
 */
public class PuzzlesActivity extends Activity {
	ListView listView;
	String[] values;
	List<Puzzle> puzzles = new ArrayList<Puzzle>();

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.puzzles);

		// Get ListView object from xml
		listView = (ListView) findViewById(R.id.list);

	    try {
		    parseXML(this);
	    } catch (XmlPullParserException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
	    } catch (IOException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
	    }

	    int index = 0;
		values = new String[puzzles.size()];

		// Defined Array values to show in ListView
		for(Puzzle puzzle : puzzles)
		{
			values[index] = puzzle.description;
			index++;
		}

	    // Define a new Adapter
	    // First parameter - Context
	    // Second parameter - Layout for the row
	    // Third parameter - ID of the TextView to which the data is written
	    // Forth - the Array of data
	    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, android.R.id.text1, values);

	    // Assign adapter to ListView
	    listView.setAdapter(adapter);

	    // ListView Item Click Listener
	    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
		    @Override
		    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

			    // ListView Clicked item value
			    String setup = puzzles.get(position).setup;

			    Intent intent = new Intent(PuzzlesActivity.this, PlayActivity.class);
			    Bundle bundle = new Bundle();
			    bundle.putString("setup", setup);
			    intent.putExtras(bundle);
			    startActivity(intent);
		    }
		});
    }

	private void parseXML(Activity activity) throws XmlPullParserException, IOException
	{
		int number = 0;
		int level = 0;
		String setup = null;
		boolean blevel = false;
		boolean bsetup = false;
		Resources res = activity.getResources();
		XmlResourceParser xpp = res.getXml(R.xml.challenge_classic40);
		xpp.next();
		int eventType = xpp.getEventType();
		while (eventType != XmlPullParser.END_DOCUMENT)
		{
			if(eventType == XmlPullParser.START_TAG)
			{
				if(xpp.getName().equals("puzzle"))
				{
					number = Integer.parseInt(xpp.getIdAttribute());
				}
				if(xpp.getName().equals("level"))
				{
					blevel = true;
				}
				if(xpp.getName().equals("setup"))
				{
					bsetup = true;
				}
			}
			else if(eventType == XmlPullParser.END_TAG)
			{
				if(xpp.getName().equals("puzzle"))
					puzzles.add(new Puzzle(number, level, setup));
			}
			else if(eventType == XmlPullParser.TEXT)
			{
				if(bsetup)
					setup = xpp.getText();

				if(blevel)
				{
					level = Integer.parseInt(xpp.getText());
				}

				blevel = false;
				bsetup = false;
			}

			eventType = xpp.next();
		}
	}
}