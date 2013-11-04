package com.example.TrafficJam;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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

                // Perform action on click
                puzzleAdapter.clearData();
                showToast("Progress has been reset.");
                Intent intent = new Intent(OptionsActivity.this, MyActivity.class);
                startActivity(intent);
            }
        });
    }
    public void showToast(String message) {
        if (!isFinishing()) {
            Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }
}