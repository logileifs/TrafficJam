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

        MyShape( Rect r, int c, boolean v ) {
            rect = r;
            color = c;
            isVertical = v;
        }
        Rect rect;
        int  color;
        boolean isVertical;
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
                    (car.x+car.length-1) * m_cellWidth + m_cellWidth, car.y * m_cellHeight + m_cellHeight),  Color.GREEN, car.isVertical()));
          }else{
            mShapes.add(new MyShape(new Rect(car.x * m_cellWidth, car.y * m_cellHeight,
                    car.x * m_cellWidth + m_cellWidth, (car.y+car.length-1) * m_cellHeight + m_cellHeight),  Color.BLUE, car.isVertical()));
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
                    mMovingShape = null;
                }
                break;
            case MotionEvent.ACTION_MOVE:
            if ( mMovingShape != null ) {
                if(!mMovingShape.isVertical){
                    if(collision(mMovingShape.rect, true)){
                        mMovingShape = null;
                        invalidate();
                    }  else {
                        x = Math.min( x, getWidth() - mMovingShape.rect.width() );
                        y = mMovingShape.rect.top;
                        mMovingShape.rect.offsetTo( x - mMovingShape.rect.width()/2, y );
                        invalidate();
                    }
                    }else{

                    if(collision(mMovingShape.rect, false) ){
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

    private boolean collision(Rect car, boolean isVertical){

       for(MyShape shape : mShapes){
            if(!shape.rect.equals(car)){
                if(car.intersects(shape.rect, car)){
                    if(!isVertical){
                        if(findShape(car.centerX(),car.top-2) != null){  // er eitthvað fyrir ofan
                            car.offsetTo(car.left,shape.rect.bottom+1);
                        } else {   // down
                            car.offsetTo(car.left,shape.rect.top-3*m_cellHeight-1);
                        }
                    } else {
                        if(findShape(car.left-2,car.centerY()) != null){ // er eitthvað til hægrivið kubbinn
                            car.offsetTo(shape.rect.right+1, car.top);
                        } else {   //right
                            car.offsetTo(shape.rect.left-2*m_cellWidth-1, car.top);
                        }
                    }

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

}
