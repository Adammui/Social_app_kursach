package reyne.social_app_kursach.db;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import reyne.social_app_kursach.model.Image;

public class DbHelper extends SQLiteOpenHelper {

    private static final int SCHEMA = 1;
    private static final String Database_name = "BlogDb";
    public static final String POSTS_TABLE = "Posts";

    private static DbHelper instance = null;

    public DbHelper(Context context) {
        super(context, Database_name, null, SCHEMA);
    }


    public static DbHelper getInstance(Context context) {
        if(instance == null) instance = new DbHelper(context);
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + POSTS_TABLE + " (                    "
                + "id integer primary key autoincrement not null,"
                + "user_id integer not null , "
                + "text text , "
                + "img text , "
                + "deleted text default 'false' ,"
                + "created_at text not null, "
                + "updated_at text not null);"
        );
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("drop table if exists " + POSTS_TABLE);
        onCreate(db);
    }
}

