package edu.gatech.cs.environmentalodors.database;

import edu.gatech.cs.environmentalodors.models.OdorEvent;
import edu.gatech.cs.environmentalodors.models.OdorReport;
import edu.gatech.cs.environmentalodors.models.User;
import java.util.Date;
import java.util.UUID;


public interface GenericDatabaseInterface {

//    public static final String DATABASE_NAME = "NosePlug.db";
//    public static final String UUID_TABLE = "uuids";
//    public static final String USERNAME_TABLE = "users";
//    public static final String USER_EMAILS_TABLE = "emails";
//    public static final String EVENTS_TABLE = "events";

//    private static final String SQL_CREATE_ENTRIES =
//            "CREATE TABLE " + DATABASE_NAME + " (" +
//                    UUID_TABLE + " UUID KEY," +
//                    USERNAME_TABLE + " USERNAME," +
//                    USER_EMAILS_TABLE + " EMAIL)";
//
//    private static final String SQL_DELETE_ENTRIES =
//            "DROP TABLE IF EXISTS " + DATABASE_NAME;

    // public void onCreate(SQLiteDatabase database);
    // public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion);

    public boolean registerUser(User user);
    public boolean addEvent(OdorEvent event);
    public boolean addReport(OdorReport report);

    public boolean updateUser(UUID id, Date birthday, /** String -> Sex */ String sex,
                              String address, String email, String phoneNumber, String username);
    public boolean deleteUser(UUID id);

    public OdorEvent getEvent(UUID id);
    public User getUser(UUID id);
    public OdorReport getReport(UUID id);
}