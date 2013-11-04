package com.example.TrafficJam;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.SimpleCursorAdapter;

/**
 * Created with IntelliJ IDEA.
 * User: Lenovo
 * Date: 3.11.2013
 * Time: 15:06
 * To change this template use File | Settings | File Templates.
 */
public class PuzzleAdapter {

    SQLiteDatabase db;
    DBhelper dbHelper;
    Context  context;

    PuzzleAdapter( Context c ) {
        context = c;
    }

    public PuzzleAdapter openToRead() {
        dbHelper = new DBhelper( context );
        db = dbHelper.getReadableDatabase();
        return this;
    }

    public PuzzleAdapter openToWrite() {
        dbHelper = new DBhelper( context );
        db = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        db.close();
    }

    public long insertPuzzle( int puzzleNumber , boolean isFinished) {
        String[] cols = DBhelper.TableStudentsCols;
        ContentValues contentValues = new ContentValues();
        contentValues.put( cols[1], ((Integer)puzzleNumber).toString() );
        contentValues.put( cols[2], isFinished ? "1" : "0" );
        openToWrite();
        long value = db.insert(DBhelper.TablePuzzles, null, contentValues );
        close();
            return value;
    }

    public long updatePuzzle( int puzzleNumber , boolean isFinished) {
        String[] cols = DBhelper.TableStudentsCols;
        ContentValues contentValues = new ContentValues();
        contentValues.put( cols[1], ((Integer)puzzleNumber).toString() );
        contentValues.put( cols[2], isFinished ? "1" : "0" );
        openToWrite();
        long value = db.update(DBhelper.TablePuzzles, contentValues, cols[1] + "=" + puzzleNumber, null );
        close();
        return value;
    }

    public Cursor getPuzzleByPuzzleNumber(int puzzleNumber){
        openToRead();
        Cursor cursor = db.query( DBhelper.TablePuzzles,
                DBhelper.TableStudentsCols, "puzzleNumber" + "=" + puzzleNumber, null, null, null, null );
        return cursor;
    }

    public Cursor queryPuzzle() {
        openToRead();
        Cursor cursor = db.query( DBhelper.TablePuzzles,
                DBhelper.TableStudentsCols, null, null, null, null, null );
        return cursor;
    }

    public void clearData(){
        openToWrite();
        db.execSQL(DBhelper.sqlDropTableCurrentPuzzle);
        db.execSQL(DBhelper.sqlCreateTableCurrentPuzzle);
        close();
    }

    public Cursor queryStudent( int sid ) {
        openToRead();
        String[] cols = DBhelper.TableStudentsCols;
        Cursor cursor = db.query( DBhelper.TablePuzzles,
                cols, cols[1] + "=" + sid , null, null, null, null );
        return cursor;
    }
}