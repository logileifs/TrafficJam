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
         //int x, int y, int length, char alignment
        mCars.add(new Car(1,2,2,'H'));
        mCars.add(new Car(0,1,3,'V'));
        mCars.add(new Car(0,0,2,'H'));
        mCars.add(new Car(3,1,3,'V'));
        mCars.add(new Car(2,5,3,'H'));
        mCars.add(new Car(0,4,2,'V'));
        mCars.add(new Car(4,4,2,'H'));
        mCars.add(new Car(5,0,3,'V'));
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
    }


    private void addShapes(Canvas canvas){
        //m_unitWidth = canvas.getWidth()/6;
      //  m_unitHeight = canvas.getHeight()/6;
      //  mShapes.add(new MyShape(new Rect(0, 0,   m_unitWidth, m_unitHeight ), Color.BLACK, true));
       // mShapes.add(new MyShape(new Rect(0, m_unitHeight*2, m_unitWidth, m_unitHeight*3), Color.GREEN, false));
    }

    protected void onDraw(Canvas canvas){
        if(mShapes.isEmpty()){
            addShapes(canvas);
        }

        for ( MyShape shape : mShapes ) {
            movingPaint.setColor( shape.color );
            canvas.drawRect( shape.rect, movingPaint );
        }
        for (int i=0 ; i < 6 ;i++ ) {
            for (int c = 0; c < 6 ; c++) {
                mRect.set( c * m_cellWidth, i * m_cellHeight,
                        c * m_cellWidth + m_cellWidth, i * m_cellHeight + m_cellHeight );
                canvas.drawRect( mRect, mPaint );
                //mRect.inset( (int)(mRect.width() * 0.1), (int)(mRect.height() * 0.1) );
                m_shape.setBounds( mRect );


                switch ( m_board[c][i] ) {
                    case 'H':
                        m_shape.getPaint().setColor( Color.GREEN );
                        //mRect.inset( (int)(mRect.width() * 0.1), (int)(mRect.height() * 0.1) );
                        m_shape.draw(canvas);
                        break;
                    case 'V':
                        m_shape.getPaint().setColor( Color.BLUE );
                        //mRect.inset( (int)(mRect.width() * 0.1), (int)(mRect.height() * 0.1) );
                        m_shape.draw(canvas);
                        break;
                    case 'v':
                        m_shape.getPaint().setColor( Color.BLUE );
                        //mRect.inset( (int)(mRect.width() * 0.1), (int)(mRect.height() * 0.1) );
                        m_shape.draw(canvas);
                        break;
                    case 'h':
                        m_shape.getPaint().setColor( Color.GREEN );
                        //mRect.inset( (int)(mRect.width() * 0.1), (int)(mRect.height() * 0.1) );
                        m_shape.draw(canvas);
                        break;
                    default:
                        break;
                }
            }
        }

    }

    public boolean onTouchEvent(MotionEvent event) {

        int x = (int) event.getX();
        int y = (int) event.getY();

        switch ( event.getAction() ) {
            case MotionEvent.ACTION_DOWN:


                if( m_board [xToCol(x)][yToRow(y)] != '.'){

                    if(m_board [xToCol(x)][yToRow(y)] == 'v' | m_board [xToCol(x)][yToRow(y)] == 'V' ){
                        mMovingShape =  new MyShape(new Rect( xToCol(x) * m_cellWidth, yToRow(y) * m_cellHeight,
                                xToCol(x) * m_cellWidth + m_cellWidth, yToRow(y) * m_cellHeight + m_cellHeight ), Color.BLACK, false);
                    }

                    if(m_board [xToCol(x)][yToRow(y)] == 'h' | m_board [xToCol(x)][yToRow(y)] == 'H' ){
                        mMovingShape =  new MyShape(new Rect( xToCol(x) * m_cellWidth, yToRow(y) * m_cellHeight,
                                xToCol(x) * m_cellWidth + m_cellWidth, yToRow(y) * m_cellHeight + m_cellHeight ), Color.BLACK, true);
                    }

                    m_board [xToCol(x)][yToRow(y)] = '.';
                    mShapes.add(mMovingShape);
                }

                break;
            case MotionEvent.ACTION_UP:
                if ( mMovingShape != null ) {
                    m_board [xToCol(x)][yToRow(y)] = 'v';
                    mMovingShape = null;
                    mShapes.clear();
                }
                break;
            case MotionEvent.ACTION_MOVE:
            if ( mMovingShape != null ) {
                if(mMovingShape.isVertical){
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

}
