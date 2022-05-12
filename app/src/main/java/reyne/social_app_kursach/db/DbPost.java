package reyne.social_app_kursach.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import reyne.social_app_kursach.model.Wall_post;

public class DbPost {


    public static long add(SQLiteDatabase db, Wall_post wallPost) {

        ContentValues values = new ContentValues();
        values.put("id", wallPost.getId());
        values.put("user_id", wallPost.getUser_id());
        values.put("text", wallPost.getText());
        //values.put("image", wallPost.getImage());
        values.put("created_at", wallPost.getCreated_at());
        values.put("updated_at", wallPost.getUpdated_at());

        return db.insert(DbHelper.POSTS_TABLE, null, values);
    }

    public static Cursor getDeletedSync(SQLiteDatabase db) {

        return db.rawQuery("select * from " + DbHelper.POSTS_TABLE + " where deleted = 'true' ;",null);
    }
    public static Cursor getActual(SQLiteDatabase db) {
        return db.rawQuery("select * from " + DbHelper.POSTS_TABLE + " where deleted = 'false' ;", null);
    }

   // public static Cursor getAllByPriority(SQLiteDatabase db) {
   //     return db.rawQuery("select * from " + DbHelper.POSTS_TABLE + " order by priority desc", null);
   // }

    public static void deleteById(SQLiteDatabase db, int id) {
        //db.rawQuery(" delete from " + DbHelper.Events_table + " where id_event = ? ", new String[]{String.valueOf(id)});
        db.delete(DbHelper.POSTS_TABLE, "id = ?" , new String[]{String.valueOf(id)});
        return;
    }
    public static void deleteByIdOffline(SQLiteDatabase db, int id) {
        //db.rawQuery(" delete from " + DbHelper.Events_table + " where id_event = ? ", new String[]{String.valueOf(id)});
        ContentValues cv = new ContentValues();

        cv.put("deleted", "true");
        db.update(DbHelper.POSTS_TABLE,cv,"id = ?",new String[]{String.valueOf(id)});
        return;
    }
    public static void editById(SQLiteDatabase db, int id, String new_text) {

        // db.rawQuery(" update " + DbHelper.Events_table + " set note = ?, priority = ? where id_event = ? ", new String[]{ new_note ,new_priority, String.valueOf(id) });
        ContentValues cv = new ContentValues();
        cv.put("text",new_text);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.S'Z'");
        String date = sdf.format(new Date());
        cv.put("updated_at", String.valueOf(date));
        db.update(DbHelper.POSTS_TABLE,cv,"id = ?",new String[]{String.valueOf(id)});

        return ;
    }
    public static Cursor findbyid(SQLiteDatabase db, int id) {
        return db.rawQuery("select * from " + DbHelper.POSTS_TABLE + " where id_event = ?", new String[]{String.valueOf(id)});
    }
}