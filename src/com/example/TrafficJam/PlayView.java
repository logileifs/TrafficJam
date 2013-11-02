package com.example.TrafficJam;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.RectShape;
import android.graphics.drawable.shapes.Shape;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Óli og Logi
 * Date: 24.10.2013
 * Time: 17:51
 * To change this template use File | Settings | File Templates.
 */
public class PlayView extends View {

    int m_cellWidth;
    int m_cellHeight;

    private class MyShape {

        MyShape( Rect r, int c, boolean v, int i ) {
            rect = r;
            color = c;
            isVertical = v;
	        id = i;
        }
        Rect rect;
        int  color;
        boolean isVertical;
	    int id;
    }

    private char[][] m_board = new char[6][6];
    List<Car> mCars;
    Paint mPaint = new Paint();
    Paint movingPaint = new Paint();
    ArrayList<MyShape> mShapes = new ArrayList<MyShape>();
    MyShape mMovingShape = null;
    Rect mRect = new Rect();
    ShapeDrawable m_shape = new ShapeDrawable( new RectShape() );

    public PlayView(Context context, AttributeSet attrs) {
        super(context, attrs);

        PlayView pl = (PlayView) findViewById(R.id.play_view);
        m_cellHeight = getMeasuredHeight();
        m_cellWidth = getMeasuredWidth();
        //(H 1 2 2), (V 0 1 3), (H 0 0 2), (V 3 1 3), (H 2 5 3), (V 0 4 2), (H 4 4 2), (V 5 0 3)
        setBoard("HH...VV..V.vvHHv.VV..V..V...HHV.HhH.");
        mPaint.setColor( Color.WHITE );
        mPaint.setStyle( Paint.Style.STROKE );
    }

    public void setBoard( String string )
    {
        for ( int idx=0, i=0 ; i < 6 ;i++ ) {
            for (int c = 0; c < 6 ; c++, idx++) {
                m_board[c] [i] = string.charAt( idx );

            }
        }
        invalidate();
    }

    public void setCars(List<Car> Cars){
        m_cellHeight = getMeasuredHeight();
        m_cellWidth = getMeasuredWidth();
        mCars = Cars;

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int size = Math.min(getMeasuredWidth(), getMeasuredHeight());
        setMeasuredDimension(size, size);
    }

    @Override
    protected void onSizeChanged(int xNew, int yNew, int xOld, int yOld) {
        m_cellWidth = xNew / 6;
        m_cellHeight = yNew / 6;
        for(Car car : mCars){

          if(!car.isVertical()){
            mShapes.add(new MyShape(new Rect(car.x * m_cellWidth, car.y * m_cellHeight,
		            (car.x+car.length-1) * m_cellWidth + m_cellWidth, car.y * m_cellHeight + m_cellHeight),  Color.GREEN, car.isVertical(), car.id));
          }else{
            mShapes.add(new MyShape(new Rect(car.x * m_cellWidth, car.y * m_cellHeight,
                    car.x * m_cellWidth + m_cellWidth, (car.y+car.length-1) * m_cellHeight + m_cellHeight),  Color.BLUE, car.isVertical(), car.id));
          }
        }
    }

    protected void onDraw(Canvas canvas){
        for ( MyShape shape : mShapes ) {
            movingPaint.setColor( shape.color );
            canvas.drawRect( shape.rect, movingPaint );
        }
    }

    public boolean onTouchEvent(MotionEvent event) {

        int x = (int) event.getX();
        int y = (int) event.getY();

        switch ( event.getAction() ) {
            case MotionEvent.ACTION_DOWN:
                mMovingShape = findShape( x, y );
                break;
            case MotionEvent.ACTION_UP:
                updateCars(mMovingShape.id, x, y);
	            if ( mMovingShape != null ) {
                    mMovingShape = null;
                }
                break;
            case MotionEvent.ACTION_MOVE:
            if ( mMovingShape != null ) {
                if(!        mMovingShape.isVertical){
                    x = Math.min( x, getWidth() - mMovingShape.rect.width() );
                    y = mMovingShape.rect.top;
                    mMovingShape.rect.offsetTo( x, y );
                    invalidate();
                }else{
                    x = mMovingShape.rect.left;
                    mMovingShape.rect.offsetTo( x, y );
                    invalidate();
                }
              }
                break;
        }
        return true;
    }

    private int xToCol( int x ) {
        return x / m_cellWidth;
    }

    private int yToRow( int y ) {
        return y / m_cellHeight;
    }


    private MyShape findShape( int x, int y ) {
        for ( MyShape shape : mShapes ) {
            if ( shape.rect.contains( x, y ) ) {
                return shape;
            }
        }

        return null;
    }

	private void updateCars(int id, int x, int y)
	{
		System.out.println("update cars " + id);
		if(findCar(id) != null)
		{
			System.out.println("found car " + id);
			findCar(id).x = Math.round(x/m_cellWidth);
			findCar(id).y = Math.round(y/m_cellHeight);
			System.out.println("car x: " + x/m_cellWidth + " car y: " + y/m_cellHeight);
		}
	}

	private Car findCar(int id)
	{
		for(Car car : mCars)
		{
			if(car.id == id)
				return car;
		}

		System.out.println("findCar returns null");

		return null;
	}

	public List<Car> getCars()
	{
		return mCars;
	}
}
