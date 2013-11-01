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
    Paint mPaint = new Paint();
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
        setBoard("HH...VV..V.vVHHv.V...V..V...HHVHhH..");
        mPaint.setColor( Color.WHITE );
        mPaint.setStyle( Paint.Style.STROKE );
    }

    public void setBoard( String string )
    {
        for ( int idx=0, r=5; r>=0; --r ) {
            for ( int c=0; c<6; ++c, ++idx ) {
                m_board[c][r] = string.charAt( idx );
            }
        }
        invalidate();
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
            mPaint.setColor( shape.color );
            canvas.drawRect( shape.rect, mPaint );
        }

        for ( int r=5; r>=0; --r ) {
            for ( int c=0; c<6; ++c ) {
                mRect.set( c * m_cellWidth, r * m_cellHeight,
                        c * m_cellWidth + m_cellWidth, r * m_cellHeight + m_cellHeight );
                canvas.drawRect( mRect, mPaint );
                //mRect.inset( (int)(mRect.width() * 0.1), (int)(mRect.height() * 0.1) );
                m_shape.setBounds( mRect );

                switch ( m_board[c][r] ) {
                    case 'x':
                        m_shape.getPaint().setColor( Color.RED );
                        m_shape.draw(canvas);
                        break;
                    case 'o':
                        m_shape.getPaint().setColor( Color.BLUE );
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

    private MyShape findShape( int x, int y ) {
        for ( MyShape shape : mShapes ) {
            if ( shape.rect.contains( x, y ) ) {
                return shape;
            }
        }
        return null;
    }

}
