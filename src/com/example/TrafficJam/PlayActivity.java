package com.example.TrafficJam;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created with IntelliJ IDEA.
 * User: Lenovo
 * Date: 31.10.2013
 * Time: 14:27
 * To change this template use File | Settings | File Templates.
 */
public class PlayActivity extends Activity {
    PlayView  m_gv;
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.play);
        m_gv = (PlayView) findViewById( R.id.playview);
    }
}