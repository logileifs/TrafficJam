package com.example.TrafficJam;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created with IntelliJ IDEA.
 * User: Lenovo
 * Date: 31.10.2013
 * Time: 14:27
 * To change this template use File | Settings | File Templates.
 */
public class OptionsActivity extends Activity {
	PuzzleAdapter puzzleAdapter = new PuzzleAdapter(this);

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
	    setContentView(R.layout.options);

	    final Button button = (Button) findViewById(R.id.buttonReset);
	    button.setOnClickListener(new View.OnClickListener() {
		    public void onClick(View v) {
			    System.out.println("on click reset button");
			    // Perform action on click
			    puzzleAdapter.clearData();

			    Intent intent = new Intent(OptionsActivity.this, MyActivity.class);
			    startActivity(intent);
		    }
	    });
    }
}