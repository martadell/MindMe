package edu.upc.eseiaat.pma.mindme.provadraglist;

/**
 * Created by Marta on 14/01/2018.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.gson.Gson;

import java.util.ArrayList;

public class DragListDataBase extends SQLiteOpenHelper {
    private static final int VERSIO_DB = 1;
    public static final String ELEMENTS_TAULA = "ELEMENTS";
    public static final String NOM_DB = "DB";

    private static DragListDataBase instancia;

    private DragListDataBase(Context context) {
        super(context, NOM_DB, null, VERSIO_DB);
    }

    public static DragListDataBase getInstancia(Context context) {
        if (instancia == null) instancia = new DragListDataBase(context);
        return instancia;
    }

    public void createTable() {
        SQLiteDatabase db = getWritableDatabase();
        String SQLCreateTaula = "CREATE TABLE IF NOT EXISTS " + ELEMENTS_TAULA + "(ELEMENT TEXT,NOM TEXT)";
        db.execSQL(SQLCreateTaula);
    }

    public ArrayList<DragListElement> getElements() {
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<DragListElement> lArray = new ArrayList<>();
        String query = "SELECT * FROM " + DragListDataBase.ELEMENTS_TAULA;
        Cursor c = db.rawQuery(query, null);
        if (c.moveToFirst()) {
            do {
                DragListElement mElement = new Gson().fromJson(c.getString(0), DragListElement.class);
                lArray.add(mElement);
            } while (c.moveToNext());
        }
        c.close();
        return lArray;
    }

    public void addElement(DragListElement mElement) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("ELEMENT", new Gson().toJson(mElement));
        cv.put("NOM", mElement.getNom());

        long id = db.insert(ELEMENTS_TAULA, null, cv);
        if (id == -1)
            throw new SQLException("Error d'inserció " + mElement.getNom() + ", no conté dades");
    }

    public void updateElement(DragListElement mElement) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("ELEMENT", new Gson().toJson(mElement));
        cv.put("NOM", mElement.getNom());

        long id = db.update(ELEMENTS_TAULA, cv, "NOM = '" + mElement.getNom() + "'", null);
        if (id <= 0)
            throw new SQLException("Error d'actualització " + mElement.getNom() + ", no conté dades");
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}

