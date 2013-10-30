package com.example.TrafficJam;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MyActivity extends Activity
{
	/**
	 * Called when the activity is first created.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
	}

	public void buttonPlay(View view)
	{
		Intent intent = new Intent(MyActivity.this, PlayActivity.class);
		startActivity(intent);
	}

	public void buttonPuzzles(View view)
	{
		Intent intent = new Intent(MyActivity.this, PuzzlesActivity.class);
		startActivity(intent);
	}

	public void buttonOptions(View view)
	{
		Intent intent = new Intent(MyActivity.this, OptionsActivity.class);
		startActivity(intent);
	}
}
