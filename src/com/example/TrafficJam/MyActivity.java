package com.example.TrafficJam;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MyActivity extends Activity
{

    Button m_buttonPlay;
    Button m_buttonOptions;
    Button m_buttonPuzzles;
	Puzzle currentPuzzle;
    List<Puzzle> puzzles = new ArrayList<Puzzle>();
    private PuzzleAdapter mStudentsAdapter =  new PuzzleAdapter( this );
    private SimpleCursorAdapter mCursorAdapter;

    /**
	 * Called when the activity is first created.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		try {
			parseXML(this);

		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			System.out.println("XmlPullParserException");
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("IOEXception");
			e.printStackTrace();
		}

        Cursor cursor = mStudentsAdapter.queryPuzzle();


            try {
                parseXMLList(this);
            } catch (XmlPullParserException e) {
                // TODO Auto-generated catch block
                System.out.println("XmlPullParserException");
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                System.out.println("IOEXception");
                e.printStackTrace();
            }
            //ekkert er í töflunni fyllum við hana því af puzzle
        if(!cursor.moveToFirst()){
            for(Puzzle puzzle : puzzles){
               //henda inn í gagnagrunn með false í finished
               mStudentsAdapter.insertPuzzle(puzzle.number, false);
            }
        }



        m_buttonPlay = (Button) findViewById( R.id.button);
        m_buttonPlay.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MyActivity.this, PlayActivity.class);
	            Bundle bundle = new Bundle();
//	            String setup = "(H 1 2 2), (V 0 1 3), (H 0 0 2), (V 3 1 3), (H 2 5 3), (V 0 4 2), (H 4 4 2), (V 5 0 3)";

                for( Puzzle puzzle : puzzles){
                    if( puzzle.number == getLatestPuzzle()){
                        bundle.putString("setup", puzzle.setup);
                        bundle.putInt("puzzleNumber", puzzle.number);
                        break;
                    }
                }

	            intent.putExtras(bundle);
	            startActivity(intent);
            }
        });

        m_buttonOptions = (Button) findViewById( R.id.buttonOptions);
        m_buttonOptions.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyActivity.this, OptionsActivity.class);
                startActivity(intent);
            }
        });

        m_buttonPuzzles = (Button) findViewById( R.id.buttonPuzzles);
        m_buttonPuzzles.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyActivity.this, PuzzlesActivity.class);
                startActivity(intent);
            }
        });

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



    private void parseXML(Activity activity) throws XmlPullParserException, IOException
	{
		System.out.println("er i parse falli");
		int number = 0;
		int level = 0;
		String setup = null;
		boolean blevel = false;
		boolean bsetup = false;
		Resources res = activity.getResources();
		XmlResourceParser xpp = res.getXml(R.xml.current_puzzle);
		xpp.next();
		int eventType = xpp.getEventType();
		while (eventType != XmlPullParser.END_DOCUMENT)
		{
			if(eventType == XmlPullParser.START_TAG)
			{
				if(xpp.getName().equals("puzzle"))
				{
					System.out.println("finn start tag puzzle");
					number = Integer.parseInt(xpp.getIdAttribute());
					//currentPuzzle.setNumber(number);
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
				{
					System.out.println("by til nytt puzzle");

//					currentPuzzle = new Puzzle(number+1, level, setup);
					currentPuzzle = new Puzzle(number, level, setup, false);
				}
					//puzzles.add(new Puzzle(number, level, setup));
			}
			else if(eventType == XmlPullParser.TEXT)
			{
				if(bsetup)
				{
					setup = xpp.getText();
					//currentPuzzle.setSetup(setup);
				}

				if(blevel)
				{
					level = Integer.parseInt(xpp.getText());
					//currentPuzzle.setLevel(level);
				}

				blevel = false;
				bsetup = false;
			}

			eventType = xpp.next();
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
}
