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
import android.util.Log;
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

    List<Car> mCars;
    Paint mPaint = new Paint();
    Paint movingPaint = new Paint();
    boolean mWin = false;
    int mMoves;
    PlayActivity activity;
    ArrayList<MyShape> mShapes = new ArrayList<MyShape>();
    MyShape mMovingShape = null;

    public PlayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mMoves = 0;
        activity = (PlayActivity)context;
        m_cellHeight = getMeasuredHeight();
        m_cellWidth = getMeasuredWidth();
        mPaint.setColor( Color.WHITE );
        mPaint.setStyle( Paint.Style.STROKE );
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
		            (car.x+car.length-1) * m_cellWidth + m_cellWidth, car.y * m_cellHeight + m_cellHeight), car.id==0 ? Color.RED : Color.GREEN, car.isVertical(), car.id));
          }else{
            mShapes.add(new MyShape(new Rect(car.x * m_cellWidth, car.y * m_cellHeight,
                    car.x * m_cellWidth + m_cellWidth, (car.y+car.length-1) * m_cellHeight + m_cellHeight),  Color.BLUE, car.isVertical(), car.id));
          }
        }
        for ( MyShape shape : mShapes ) {
            shape.rect.inset( (int)(shape.rect.width() * 0.01), (int)(shape.rect.height() * 0.01) );
        }
    }

    protected void onDraw(Canvas canvas){
        for ( MyShape shape : mShapes ) {
            movingPaint.setColor( shape.color );
            canvas.drawRect(shape.rect, movingPaint);
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mMovingShape = findShape(x, y);
                break;
            case MotionEvent.ACTION_UP:
	            if ( mMovingShape != null ) {
		            updateCars(mMovingShape.id, x, y);
                    mMovingShape = null;
                }
                activity.updateScore(++mMoves);
                break;
            case MotionEvent.ACTION_MOVE:
            if ( mMovingShape != null ) {
                if(!mMovingShape.isVertical){
                    if(collision(mMovingShape.rect, true, mMovingShape.id)){
                        updateCars(mMovingShape.id, x, y);
                        mMovingShape = null;
                        invalidate();
                    }  else {
                        y = mMovingShape.rect.top;
                        mMovingShape.rect.offsetTo( x - mMovingShape.rect.width()/2, y );
                        invalidate();
                    }
                    }else{

                    if(collision(mMovingShape.rect, false, mMovingShape.id)){
                        updateCars(mMovingShape.id, x, y);
                        mMovingShape = null;
                        invalidate();
                    } else{
                        x = mMovingShape.rect.left;
                        mMovingShape.rect.offsetTo( x, y-mMovingShape.rect.height()/2 );
                        invalidate();
                    }
                }
            }
            break;
        }
        return true;
    }

    private boolean collision(Rect car, boolean isVertical, int id){
        int boardWidth = m_cellWidth*6;
        int boardHeight = m_cellHeight*6;

       for(MyShape shape : mShapes){
            if(!shape.rect.equals(car)){
                if(car.intersects(shape.rect, car)){
                    if(!isVertical){
                        if(findShape(car.centerX()-m_cellWidth/2+1,car.top-2) != null | findShape(car.centerX()+m_cellWidth/2-1,car.top-2) != null){
                        // er eitthvað fyrir ofan
                            car.offsetTo(car.left,shape.rect.bottom+1);
                        } else {   // down
                            car.offsetTo(car.left,shape.rect.top-car.height()-1);
                        }
                    } else {
                        if(findShape(car.left-2,car.centerY()-m_cellHeight/2+1) != null | findShape(car.left-2,car.centerY()+m_cellHeight/2-1) != null  ){
                        // er eitthvað til hægrivið kubbinn
                            car.offsetTo(shape.rect.right+1, car.top);
                        } else {   //right
                            car.offsetTo(shape.rect.left-car.width()-1, car.top);
                        }
                    }
                    return true;
                }
                    if(car.top < 0){ // out of bounds check
                        //top of board
                        car.offsetTo(car.left,0);
                        return true;
                    }
                    if(car.left < 0){
                        //left of board
                        car.offsetTo(0,car.top);
                        return true;
                    }
                    if(car.right > boardWidth){
                        //right of board
                        if(id == 0){

                            car.offsetTo(car.left+car.width(),car.top);
                            mWin = true;
	                        activity.setButtonVisibility();
                            activity.showToast("You win");
                            activity.setPuzzleAsFinished();
                            return true;

                        } else{
                            car.offsetTo(boardWidth-car.width(),car.top);
                            return true;
                        }
                    }
                    if(car.bottom > boardHeight){
                        //bottom of board
                        car.offsetTo(car.left, boardHeight-car.height());
                        return true;
                    }
                }
       }
        return false;
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
//			System.out.println("found car " + id);
			findCar(id).x = Math.round(x/m_cellWidth);
			findCar(id).y = Math.round(y/m_cellHeight);
//			System.out.println("car x: " + x/m_cellWidth + " car y: " + y/m_cellHeight);
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
