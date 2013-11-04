package com.example.TrafficJam;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Xml;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import org.w3c.dom.*;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
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
	List<Puzzle> puzzles = new ArrayList<Puzzle>();
	Button nextButton;
	Button prevButton;
	String setup;
	int puzzleNumber;
	private PuzzleAdapter mStudentsAdapter =  new PuzzleAdapter( this );
	//Cursor cursor = mStudentsAdapter.queryPuzzle();

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

        prevButton = (Button)findViewById(R.id.previous_button);

        if(puzzleNumber == 1){
            prevButton.setVisibility(View.INVISIBLE);
        }

        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    parseXMLList(PlayActivity.this);
                } catch (XmlPullParserException e) {
                    // TODO Auto-generated catch block
                    System.out.println("XmlPullParserException");
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    System.out.println("IOEXception");
                    e.printStackTrace();
                }


                Intent intent = new Intent(PlayActivity.this, PlayActivity.class);
                Bundle bundle = new Bundle();

                for( Puzzle puzzle : puzzles){
                    if( puzzle.number == puzzleNumber-1){
                        bundle.putString("setup", puzzle.setup);
                        bundle.putInt("puzzleNumber", puzzle.number);
                        break;
                    }
                }

                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

	    nextButton = (Button)findViewById(R.id.next_button);
        if(checkIsFinished(puzzleNumber) && puzzleNumber != 40){
            nextButton.setVisibility(View.VISIBLE);
        }
        else if(m_gv.mWin && puzzleNumber != 40)
        {
            nextButton.setVisibility(View.VISIBLE);
        }
        else {
            nextButton.setVisibility(View.INVISIBLE);
        }

	    nextButton.setOnClickListener( new View.OnClickListener() {
		    @Override
		    public void onClick(View view) {
			    try {
				    parseXMLList(PlayActivity.this);
			    } catch (XmlPullParserException e) {
				    // TODO Auto-generated catch block
				    System.out.println("XmlPullParserException");
				    e.printStackTrace();
			    } catch (IOException e) {
				    // TODO Auto-generated catch block
				    System.out.println("IOEXception");
				    e.printStackTrace();
			    }

			    Intent intent = new Intent(PlayActivity.this, PlayActivity.class);
			    Bundle bundle = new Bundle();

			    for( Puzzle puzzle : puzzles){
				    if( puzzle.number == puzzleNumber+1){
					    bundle.putString("setup", puzzle.setup);
					    bundle.putInt("puzzleNumber", puzzle.number);
					    break;
				    }
			    }

			    intent.putExtras(bundle);
			    startActivity(intent);
		    }
	    });

	    TextView puzzle_title = (TextView) findViewById(R.id.puzzle_title);
	    puzzle_title.setText("Puzzle " + puzzleNumber);
    }


    public boolean checkIsFinished(int number){
        Cursor cursor = mStudentsAdapter.getPuzzleByPuzzleNumber(number);
        if(cursor.moveToFirst()){
            return cursor.getInt(cursor.getColumnIndex("isFinished"))>0;
        }
        return true;
    }

	public void setButtonVisibility()
	{

        //!m_gv.mWin
		if(checkIsFinished(puzzleNumber)){
			nextButton.setVisibility(View.VISIBLE);
		}
		else if(m_gv.mWin)
		{
			nextButton.setVisibility(View.VISIBLE);
		}
        else {
            nextButton.setVisibility(View.INVISIBLE);
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

	private void parseXMLList(Activity activity) throws XmlPullParserException, IOException
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
					puzzles.add(new Puzzle(number, level, setup, false));
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

    public void setPuzzleAsFinished(){
        mPuzzleAdapter.updatePuzzle(puzzleNumber,true);
    }

	public int getLatestPuzzle(){
		Cursor cursor = mStudentsAdapter.queryPuzzle();

		if (cursor.moveToFirst()) {

			while (!cursor.isAfterLast()) {

				if(!(cursor.getInt(cursor.getColumnIndex("isFinished")) > 0)){
					return cursor.getInt(cursor.getColumnIndex("puzzleNumber"));
				}
				cursor.moveToNext();
			}
		}
		return 39;
	}
}