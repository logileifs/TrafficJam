package com.example.TrafficJam;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Óli og Logi
 * Date: 24.10.2013
 * Time: 17:51
 * To change this template use File | Settings | File Templates.
 */
public class PlayView extends View {

    private class MyShape {

        MyShape( Rect r, int c ) {
            rect = r;
            color = c;
        }
        Rect rect;
        int  color;
    }

    Paint mPaint = new Paint();
    ArrayList<MyShape> mShapes = new ArrayList<MyShape>();
    MyShape mMovingShape = null;

    public PlayView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mShapes.add(new MyShape(new Rect(0, 0, 100, 100), Color.RED));
        mShapes.add(new MyShape( new Rect( 200, 300, 300, 350), Color.BLUE ) );
    }

    protected void onDraw( Canvas canvas ) {
        for ( MyShape shape : mShapes ) {
            mPaint.setColor( shape.color );
            canvas.drawRect( shape.rect, mPaint );
        }
    }

    public boolean onTouchEvent( MotionEvent event ) {

        int x = (int) event.getX();
        int y = (int) event.getY();

        switch ( event.getAction() ) {
            case MotionEvent.ACTION_DOWN:
                mMovingShape = findShape( x, y );
                break;
            case MotionEvent.ACTION_UP:
                if ( mMovingShape != null ) {
                    mMovingShape = null;
                    // emit an custom event ....
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if ( mMovingShape != null ) {
                    x = Math.min( x, getWidth() - mMovingShape.rect.width() );
                    mMovingShape.rect.offsetTo( x, y );
                    invalidate();
                }
                break;
        }
        return true;
    }

    private MyShape findShape( int x, int y ) {
        for ( MyShape shape : mShapes ) {
            if ( shape.rect.contains( x, y ) ) {
                return shape;
            }
        }
        return null;
    }
}
