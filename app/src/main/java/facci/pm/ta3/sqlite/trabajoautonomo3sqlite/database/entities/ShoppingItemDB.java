package facci.pm.ta3.sqlite.trabajoautonomo3sqlite.database.entities;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import facci.pm.ta3.sqlite.trabajoautonomo3sqlite.database.helper.ShoppingElementHelper;
import facci.pm.ta3.sqlite.trabajoautonomo3sqlite.database.model.ShoppingItem;
import java.util.ArrayList;

public class ShoppingItemDB {

    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";

    private ShoppingElementHelper dbHelper;

    public ShoppingItemDB(Context context) {
        // Create new helper
        dbHelper = new ShoppingElementHelper(context);
    }

    /* Inner class that defines the table contents */
    public static abstract class ShoppingElementEntry implements BaseColumns {
        public static final String TABLE_NAME = "entry";
        public static final String COLUMN_NAME_TITLE = "title";

        public static final String CREATE_TABLE = "CREATE TABLE " +
                TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT" + COMMA_SEP +
                COLUMN_NAME_TITLE + TEXT_TYPE + " )";

        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public void insertElement(String productName) {
        //TODO: Todo el código necesario para INSERTAR un Item a la Base de datos
        SQLiteDatabase base = dbHelper.getWritableDatabase();
        // Creamos un objeto de tipo ContentValues para agregar los pares de claves de Columna y Valor
        ContentValues values = new ContentValues();
        values.put(ShoppingElementEntry.COLUMN_NAME_TITLE, productName);
        //  Insertamos  los Items en la tabla
        base.insert(ShoppingElementEntry.TABLE_NAME,ShoppingElementEntry.COLUMN_NAME_TITLE,values);
        // Cerramos la conexion  de la base de dato
        base.close();

    }


    public ArrayList<ShoppingItem> getAllItems() {

        ArrayList<ShoppingItem> shoppingItems = new ArrayList<>();

        String[] allColumns = { ShoppingElementEntry._ID,
                ShoppingElementEntry.COLUMN_NAME_TITLE};

        Cursor cursor = dbHelper.getReadableDatabase().query(
                ShoppingElementEntry.TABLE_NAME,    // The table to query
                allColumns,                         // The columns to return
                null,                               // The columns for the WHERE clause
                null,                               // The values for the WHERE clause
                null,                               // don't group the rows
                null,                               // don't filter by row groups
                null                                // The sort order
        );

        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            ShoppingItem shoppingItem = new ShoppingItem(getItemId(cursor), getItemName(cursor));
            shoppingItems.add(shoppingItem);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        dbHelper.getReadableDatabase().close();
        return shoppingItems;
    }
    private long getItemId(Cursor cursor) {
        return cursor.getLong(cursor.getColumnIndexOrThrow(ShoppingElementEntry._ID));
    }
    private String getItemName(Cursor cursor) {
        return cursor.getString(cursor.getColumnIndexOrThrow(ShoppingElementEntry.COLUMN_NAME_TITLE));
    }
    public void clearAllItems() {
        //TODO: Todo el código necesario para ELIMINAR todos los Items de la Base de datos
        SQLiteDatabase base = dbHelper.getWritableDatabase();
        // Borramos los items de la Base de datos
        base.delete(ShoppingElementEntry.TABLE_NAME,"",null);
        // Cerramos la conexion  de la base de datos
        base.close();

    }
    public void updateItem(ShoppingItem shoppingItem) {
        //TODO: Todo el código necesario para ACTUALIZAR un Item en la Base de datos
        // Obtenemos una referencia de la Base de Datos con permisos de escritura
        SQLiteDatabase base = dbHelper.getWritableDatabase();
        // Creamos el objeto ContentValues con las claves "columna"/valor que se desean actualizar
        ContentValues values = new ContentValues();
        values.put(ShoppingElementEntry.COLUMN_NAME_TITLE, shoppingItem.getName());
        // Actualizamos el Item con el método update el cual devuelve la cantidad de items actualizados
        base.update(ShoppingElementEntry.TABLE_NAME,values,ShoppingElementEntry._ID + " = "+ shoppingItem.getId(),null);
        // Cerramos la conexion de la base de datos
        base.close();

    }

    public void deleteItem(ShoppingItem shoppingItem) {
        //TODO: Todo el código necesario para ELIMINAR un Item de la Base de datos
        // Obtenemos una referencia de la Base de Datos con permisos de escritura
        SQLiteDatabase base = dbHelper.getWritableDatabase();
        // Se borra un item
        String strFilter =  ShoppingElementEntry._ID + " = "+ shoppingItem.getId();
        base.delete(ShoppingElementEntry.TABLE_NAME, strFilter,null);
        // Cerramos la conexion de la base de datos
        base.close();

    }
}

