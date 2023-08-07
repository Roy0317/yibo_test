package com.yibo.yiboapp.data;

/**
 * 操作本地数据库接口
 * @author johnson
 */

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import com.yibo.yiboapp.BuildConfig;

import crazy_wrapper.Crazy.Utils.Utils;

public class YiboProvider extends ContentProvider {

    private static final String TAG = "YiboProvider";
    private DatabaseHelper mOpenHelper;
    private static final String DB_NAME = "yibo.db";
    private static UriMatcher mUriMatcher;
    private static final String GUARD_AUTHOR = BuildConfig.APPLICATION_ID+".provider";

    private static final String LOGIN_TABLE = "login";
    private static final String CART_TABLE = "cart";
    private static final String USUAL_GAME_TABLE = "usual_game";
    private static final String EVENT_TRACK_TABLE = "event_track";

    public static final int URI_LOGIN = 1;
    public static final int URI_CART = 2;
    public static final int URI_USUAL_GAME = 3;
    public static final int URI_EVENT_TRACK = 5;

    private static final String CART_URI_STRING = "content://" + GUARD_AUTHOR + "/" + CART_TABLE;
    private static final String LOGIN_URI_STRING = "content://" + GUARD_AUTHOR + "/" + LOGIN_TABLE;
    private static final String USUAL_GAME_URI_STRING = "content://" + GUARD_AUTHOR + "/" + USUAL_GAME_TABLE;
    private static final String EVENT_TRACK_URI_STRING = "content://" + GUARD_AUTHOR + "/" + EVENT_TRACK_TABLE;

    public static final Uri CART_URI = Uri.parse(CART_URI_STRING);
    public static final Uri LOGIN_URI = Uri.parse(LOGIN_URI_STRING);
    public static final Uri USUAL_GAME_URI = Uri.parse(USUAL_GAME_URI_STRING);
    public static final Uri EVENT_TRACK_URI = Uri.parse(EVENT_TRACK_URI_STRING);

    private static final String VND_ANDROID_DIR_LOAD = "vnd.android-dir/yibo";

    static {
        mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        mUriMatcher.addURI(GUARD_AUTHOR, CART_TABLE, URI_CART);
        mUriMatcher.addURI(GUARD_AUTHOR, LOGIN_TABLE, URI_LOGIN);
        mUriMatcher.addURI(GUARD_AUTHOR, USUAL_GAME_TABLE, URI_USUAL_GAME);
        mUriMatcher.addURI(GUARD_AUTHOR, EVENT_TRACK_TABLE, URI_EVENT_TRACK);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int affectedRows = 0;
        if (mUriMatcher.match(uri) == URI_CART) {
            affectedRows = db.delete(CART_TABLE, selection, selectionArgs);
        } else if (mUriMatcher.match(uri) == URI_LOGIN) {
            affectedRows = db.delete(LOGIN_TABLE, selection, selectionArgs);
        } else if (mUriMatcher.match(uri) == URI_USUAL_GAME) {
            affectedRows = db.delete(USUAL_GAME_TABLE, selection, selectionArgs);
        } else if (mUriMatcher.match(uri) == URI_EVENT_TRACK) {
            affectedRows = db.delete(EVENT_TRACK_TABLE, selection, selectionArgs);
        }

        if (affectedRows > 0)
            notifyChange(uri);
        return affectedRows;
    }

    @Override
    public String getType(Uri uri) {
        return VND_ANDROID_DIR_LOAD;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {

        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        if (mUriMatcher.match(uri) == URI_CART) {
            db.insert(CART_TABLE, null, values);
        } else if (mUriMatcher.match(uri) == URI_LOGIN) {
            db.insert(LOGIN_TABLE, null, values);
        } else if (mUriMatcher.match(uri) == URI_USUAL_GAME) {
            db.insert(USUAL_GAME_TABLE, null, values);
        } else if (mUriMatcher.match(uri) == URI_EVENT_TRACK) {
            db.insert(EVENT_TRACK_TABLE, null, values);
        }

        notifyChange(uri);
        return null;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new DatabaseHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {

        Cursor cursor = null;
        SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        switch (mUriMatcher.match(uri)) {
            case URI_CART:
                cursor = db.query(CART_TABLE, projection, selection, selectionArgs, null, null,
                        sortOrder);
                break;
            case URI_LOGIN:
                cursor = db.query(LOGIN_TABLE, projection, selection, selectionArgs, null, null,
                    sortOrder);
                break;
            case URI_USUAL_GAME:
                cursor = db.query(USUAL_GAME_TABLE, projection, selection, selectionArgs, null, null,
                        sortOrder);
                break;
            case URI_EVENT_TRACK:
                cursor = db.query(EVENT_TRACK_TABLE, projection, selection, selectionArgs, null, null,
                        sortOrder);
                break;
            default:
                throw new IllegalStateException("Unrecognized URI:" + uri);
        }
        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int affectedRows = 0;
        switch (mUriMatcher.match(uri)) {
            case URI_CART: {
                affectedRows = db.update(CART_TABLE, values, selection, null);
                break;
            }
            case URI_LOGIN: {
                affectedRows = db.update(LOGIN_TABLE, values, selection, null);
                break;
            }case URI_USUAL_GAME: {
                affectedRows = db.update(USUAL_GAME_TABLE, values, selection, null);
                break;
            }
            case URI_EVENT_TRACK:
                affectedRows = db.update(EVENT_TRACK_TABLE, values, selection, null);
                break;
            default:
        }
        if (affectedRows > 0) {
            notifyChange(uri);
        }
        return affectedRows;
    }

    public void notifyChange(Uri uri) {
        ContentResolver cr = getContext().getContentResolver();
        cr.notifyChange(uri, null);
    }


    class DatabaseHelper extends SQLiteOpenHelper {

        final static int DB_VERSION = 1710;
        public DatabaseHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            String login_sql = "CREATE TABLE IF NOT EXISTS " + LOGIN_TABLE +
                "(_id integer PRIMARY KEY AUTOINCREMENT, "
                + " username text,password text,is_remember integer default 0" +");";
            db.execSQL(login_sql);

            String cart_sql = "CREATE TABLE IF NOT EXISTS " + CART_TABLE +
                "(_id integer PRIMARY KEY AUTOINCREMENT, cp_code text," +
                " play_name text,play_code text, sub_play_name text,sub_play_code text," +
                    "beishu integer default 0,numbers text,mode integer default 0," +
                    "zhushu integer default 0,money float default 0,save_time long default 0," +
                    "orderno text,lotcode text,lottype text,rate float default 0,"+ "user text);";
            db.execSQL(cart_sql);

            String usual_game_sql = "CREATE TABLE IF NOT EXISTS " + USUAL_GAME_TABLE +
                    "(_id integer PRIMARY KEY AUTOINCREMENT, game_module interger default 0," +
                    " lot_name text,lot_code text, lot_type text,play_name text," +
                    "play_code text,sub_play_name text,sub_play_code text,cp_version text," +
                    "ball_type text,game_type text,ft_play_code text,bk_play_code text," +
                    "zr_img_url text,zr_play_code text,dz_img_url text,dz_play_code text,user text,duration long default 0," +
                    "add_time long default 0"+ ");";
            db.execSQL(usual_game_sql);
            createEventTrackTable(db);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Utils.writeLogToFile(TAG,
                    "oncreate db database onUpgrage from " + oldVersion + " to " + newVersion);
            //we can upgrade the database only delete ,update,add a column or a table
            //in the future when we publish a new version.
            if (oldVersion < 1200) {
                upgradeToVersion1200(db);
            }
            if (oldVersion < 1300) {
                upgradeToVersion1300(db);
            }
            if (oldVersion < 1710) {
                createEventTrackTable(db);
            }
        }


        private void createEventTrackTable(SQLiteDatabase db) {
            String eventTrack = "CREATE TABLE IF NOT EXISTS " + EVENT_TRACK_TABLE +
                    "(_id integer PRIMARY KEY AUTOINCREMENT, "
                    + "uid text,event text,timestamp text,url text,headers text,status_code text,response text" + ");";
            db.execSQL(eventTrack);
        }

        public void upgradeToVersion1300(SQLiteDatabase db) {
            db.beginTransaction();
            try {
                db.execSQL("ALTER TABLE " + USUAL_GAME_TABLE + " ADD COLUMN duration long default 0");
                db.setTransactionSuccessful();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                db.endTransaction();
            }
        }

        public void upgradeToVersion1200(SQLiteDatabase db) {
            db.beginTransaction();
            try {
                String usual_game_sql = "CREATE TABLE IF NOT EXISTS " + USUAL_GAME_TABLE +
                        "(_id integer PRIMARY KEY AUTOINCREMENT, game_module interger default 0," +
                        " lot_name text,lot_code text, lot_type text,play_name text," +
                        "play_code text,sub_play_name text,sub_play_code text,cp_version text," +
                        "ball_type text,game_type text,ft_play_code text,bk_play_code text," +
                        "zr_img_url text,zr_play_code text,dz_img_url text,dz_play_code text," +
                        "user text,add_time long default 0"+ ");";
                db.execSQL(usual_game_sql);
                db.setTransactionSuccessful();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                db.endTransaction();
            }
        }


    }
}
