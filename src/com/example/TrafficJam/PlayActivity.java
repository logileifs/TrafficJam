package com.example.TrafficJam;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

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

	public void parseSetup(String setup)
	{
		Car car = null;
		int count = 0;
		boolean newCar = false;

		for(int i=0; i<setup.length(); i++)
		{
			if(setup.charAt(i) == '(') //new car
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
				cars.add(car);
				newCar = false;
				count = 0;
			}
		}
	}
}