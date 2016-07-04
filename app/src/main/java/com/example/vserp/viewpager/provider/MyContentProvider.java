package com.example.vserp.viewpager.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import com.example.vserp.viewpager.db.DBHelper;
import com.example.vserp.viewpager.utils.Prefs;

public class MyContentProvider extends ContentProvider {

    private SQLiteDatabase database;
    private DBHelper dbHelper;

    private static UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    private static final int URI_EXPENSES_CODE = 1;
    private static final int URI_EXPENSE_NAMES_CODE = 2;
    private static final int URI_RAW_QUERY_ALL_EXPENSES_CODE = 3;

    private static final int URI_INCOMES_CODE = 4;
    private static final int URI_INCOME_NAMES_CODE = 5;
    private static final int URI_RAW_QUERY_ALL_INCOMES_CODE = 6;

    private static final int URI_CASH_FLOW_MONTHLY_CODE = 7;
    private static final int URI_CASH_FLOW_MONTHLY_EXPENSES_PLAN_CODE = 8;

    static {
        uriMatcher.addURI(Prefs.URI_AUTHORITIES, Prefs.URI_EXPENSES_PATH, URI_EXPENSES_CODE);
        uriMatcher.addURI(Prefs.URI_AUTHORITIES, Prefs.URI_EXPENSE_NAMES_PATH, URI_EXPENSE_NAMES_CODE);
        uriMatcher.addURI(Prefs.URI_AUTHORITIES, Prefs.URI_ALL_EXPENSES_PATH, URI_RAW_QUERY_ALL_EXPENSES_CODE);

        uriMatcher.addURI(Prefs.URI_AUTHORITIES, Prefs.URI_INCOMES_PATH, URI_INCOMES_CODE);
        uriMatcher.addURI(Prefs.URI_AUTHORITIES, Prefs.URI_INCOME_NAMES_PATH, URI_INCOME_NAMES_CODE);
        uriMatcher.addURI(Prefs.URI_AUTHORITIES, Prefs.URI_ALL_INCOMES_PATH, URI_RAW_QUERY_ALL_INCOMES_CODE);

        uriMatcher.addURI(Prefs.URI_AUTHORITIES, Prefs.URI_CASH_FLOW_MONTHLY_PATH, URI_CASH_FLOW_MONTHLY_CODE);
    }

    public MyContentProvider() {
    }

    @Override
    public boolean onCreate() {
        dbHelper = new DBHelper(getContext(), Prefs.DB_CURRENT_VERSION);
        return true;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {

        database = dbHelper.getWritableDatabase();

        switch (uriMatcher.match(uri)) {
            case URI_EXPENSES_CODE:
                //TODO метод принимает uri, value и делать observer
                uri = insertUri(uri, Prefs.TABLE_NAME_EXPENSES, values);
                break;
            case URI_EXPENSE_NAMES_CODE:
                uri = insertUri(uri, Prefs.TABLE_NAME_EXPENSE_NAMES, values);
                break;
            case URI_INCOMES_CODE:
                //TODO метод принимает uri, value и делать observer
                uri = insertUri(uri, Prefs.TABLE_NAME_INCOMES, values);
                break;
            case URI_INCOME_NAMES_CODE:
                uri = insertUri(uri, Prefs.TABLE_NAME_INCOME_NAMES, values);
                break;
            case URI_CASH_FLOW_MONTHLY_CODE:
                uri = insertUri(uri, Prefs.TABLE_CASH_FLOW_MONTHLY_NAME, values);
                break;
        }
        getContext().getContentResolver().notifyChange(uri, null);

        database.close();
        return uri;
    }

    private Uri insertUri(Uri uri, String table, ContentValues values) {
        long id = database.insert(table, null, values);
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        database = dbHelper.getReadableDatabase();

        Cursor cursor = null;

        switch (uriMatcher.match(uri)) {
            case URI_EXPENSES_CODE:
                cursor = database.query(Prefs.TABLE_NAME_EXPENSES, projection,
                        selection, selectionArgs, null, null, sortOrder);
                break;
            case URI_EXPENSE_NAMES_CODE:
                cursor = database.query(Prefs.TABLE_NAME_EXPENSE_NAMES, projection,
                        selection, selectionArgs, null, null, sortOrder);
                break;
            case URI_RAW_QUERY_ALL_EXPENSES_CODE:
                cursor = queryCursorBuilder(Prefs.RAW_QUERY_ALL_EXPENSES, projection, selection,
                        selectionArgs, sortOrder);
                break;
            case URI_INCOMES_CODE:
                cursor = database.query(Prefs.TABLE_NAME_INCOMES, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case URI_INCOME_NAMES_CODE:
                cursor = database.query(Prefs.TABLE_NAME_INCOME_NAMES, projection, selection,
                        selectionArgs, null, null, sortOrder);
                break;
            case URI_RAW_QUERY_ALL_INCOMES_CODE:
                cursor = queryCursorBuilder(Prefs.RAW_QUERY_ALL_INCOMES, projection, selection,
                        selectionArgs, sortOrder);
                break;
            case URI_CASH_FLOW_MONTHLY_CODE:
                cursor = queryCursorBuilder(Prefs.TABLE_CASH_FLOW_MONTHLY_NAME, projection, selection,
                        selectionArgs, sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Non support URI");
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    private Cursor queryCursorBuilder(String table, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder sqLiteQueryBuilder = new SQLiteQueryBuilder();
        sqLiteQueryBuilder.setTables(table);
        return sqLiteQueryBuilder.query(database, projection, selection, selectionArgs, null, null, sortOrder);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        database = dbHelper.getWritableDatabase();
        int updateId;

        switch (uriMatcher.match(uri)) {
            case URI_EXPENSES_CODE:
                updateId = database.update(Prefs.TABLE_NAME_EXPENSES, values, selection, selectionArgs);
                break;
            case URI_EXPENSE_NAMES_CODE:
                updateId = database.update(Prefs.TABLE_NAME_EXPENSE_NAMES, values, selection, selectionArgs);
                break;
            case URI_INCOMES_CODE:
                updateId = database.update(Prefs.TABLE_NAME_INCOMES, values, selection, selectionArgs);
                break;
            case URI_INCOME_NAMES_CODE:
                updateId = database.update(Prefs.TABLE_NAME_INCOME_NAMES, values, selection, selectionArgs);
                break;
            case URI_CASH_FLOW_MONTHLY_CODE:
                updateId = database.update(Prefs.TABLE_CASH_FLOW_MONTHLY_NAME, values, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Not yet implemented");
        }
        database.close();
        getContext().getContentResolver().notifyChange(uri, null);

        return updateId;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        database = dbHelper.getWritableDatabase();
        int deleteId;
        switch (uriMatcher.match(uri)) {
            case URI_EXPENSES_CODE:
                deleteId = database.delete(Prefs.TABLE_NAME_EXPENSES, selection, selectionArgs);
                break;
            case URI_EXPENSE_NAMES_CODE:
                deleteId = database.delete(Prefs.TABLE_NAME_EXPENSE_NAMES, selection, selectionArgs);
                break;
            case URI_INCOMES_CODE:
                deleteId = database.delete(Prefs.TABLE_NAME_INCOMES, selection, selectionArgs);
                break;
            case URI_INCOME_NAMES_CODE:
                deleteId = database.delete(Prefs.TABLE_NAME_INCOME_NAMES, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Input correct URI");
        }
        database.close();
        getContext().getContentResolver().notifyChange(uri, null);
        return deleteId;


    }

    @Override
    public String getType(Uri uri) {

        return uri.getAuthority();
    }
}
