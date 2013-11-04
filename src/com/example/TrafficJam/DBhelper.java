package com.example.TrafficJam;

/**
 * Created with IntelliJ IDEA.
 * User: Lenovo
 * Date: 3.11.2013
 * Time: 14:39
 * To change this template use File | Settings | File Templates.
 */
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBhelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "PUZZLE_DB";
    public static final int DB_VERSION = 1;

    public static final String TablePuzzles = "puzzles";
    public static final String[] TableStudentsCols = { "_id", "puzzleNumber", "isFinished" };

    public static final String sqlCreateTableCurrentPuzzle =
            "CREATE TABLE puzzles (" +
                    "_id  INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "puzzleNumber  INTEGER ," +
                    "isFinished INTEGER" +
                    ");";


    public static final String sqlDropTableCurrentPuzzle =
            "DROP TABLE IF EXISTS puzzle;";

    public DBhelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL( sqlCreateTableCurrentPuzzle );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldV, int newV) {
        sqLiteDatabase.execSQL( sqlDropTableCurrentPuzzle );
        onCreate( sqLiteDatabase );
    }
}