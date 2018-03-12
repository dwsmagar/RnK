package com.susankya.schoolvalley;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.support.v7.app.AlertDialog;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

public class UtilitiesAdi implements FragmentCodes {


    public static int catColor(String category, Resources resources) {
        switch (category.toLowerCase()) {
            case "general":
                return resources.getColor(R.color.general_color);
            case "holiday":
                return resources.getColor(R.color.holiday_color);
            case "exams":
                return resources.getColor(R.color.exam_color);
            case "result":
                return resources.getColor(R.color.result_color1);
            case "others":
                return resources.getColor(R.color.other_color);
            default:
                return resources.getColor(R.color.button_color);
        }
    }

    public static String giveMeSN(Context c, String dbName) {
       /* try {
                JSONArray jsonArray = (JSONArray) new JSONTokener(loadJSON(c,"collegelist")).nextValue();

                HashMap<String,String> collegeNames=new HashMap<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String databaseName = jsonObject.getString("database_name");
                    String sn=jsonObject.getString("sn");
                    collegeNames.put(databaseName,sn);

                }

                return collegeNames.get(dbName);


            } catch (Exception e) {

            Toast.makeText(c, e+"", Toast.LENGTH_LONG).show();
        }
        return "0";*/
        return c.getSharedPreferences("userinfo", Context.MODE_PRIVATE).getString("sn", "0");
    }

    public static boolean isProfileOfAdmin(Context context, String sn) {
        //Log.d("TAG", "sn from function "+sn+ "  saved sn "+UtilitiesAdi.giveMeSN(context,Utilities.getDatabaseName(context)) );
        if (Utilities.isAdmin(context)) {
            if (UtilitiesAdi.giveMeSN(context, Utilities.getDatabaseName(context)).equals(sn)) {
                return true;
            } else
                return false;
        } else return false;
    }


    public static void setProfileLoaded(Context c, boolean b) {
        SharedPreferences sharedPreferences = c.getSharedPreferences("profile", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("profileLoaded", b).commit();
    }

    public static boolean getProfileLoaded(Context c) {
        SharedPreferences sharedPreferences = c.getSharedPreferences("profile", Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("profileLoaded", false);
    }

    public static void saveProfileSharedPref(Context c, UserInfo u) {
        String aboutTitles[] = new String[]{"Username", "First Name", "Last Name", "Level", "Institution", "Gender", "Mobile Number",
                "Location", "Interests", "Hobbies"};
        SharedPreferences sharedPreferences = c.getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("level", u.getLevel())
                .putString("institution", u.getInstitution())
                .putString("gender", u.getGender())
                .putString("mobileNumber", u.getPhoneNumber())
                .putString("location", u.getLocation())
                .putString("interest", u.getInterest())
                .putString("hobby", u.getHobby())
                .putString("firstName", u.getFirstName())
                .putString("lastName", u.getLastName())
                .putString("fullName", u.getFullName())
                .putString("password", u.getPassword())
                .putString("thumbnail", u.getProfilePicThumbnailLocation())
                .putString("profilepic", u.getProfilepiclocation())
                .commit()
        ;
    }

    public static void showPopUp(Context context
            , String title
            , String msg
            , String positiveBtnTxt
            , String negativeBtnTxt
            , DialogInterface.OnClickListener positiveBtnListener
            , DialogInterface.OnClickListener negativeBtnListener) {

        final AlertDialog errorDialog;
        AlertDialog.Builder errorDialogBuilder = new AlertDialog.Builder(context, android.R.style.Theme_Dialog);
        errorDialogBuilder.setTitle(title);
        errorDialogBuilder.setMessage(msg);
        errorDialogBuilder.setPositiveButton(positiveBtnTxt, positiveBtnListener);
        errorDialogBuilder.setNegativeButton(negativeBtnTxt, negativeBtnListener);
        errorDialog = errorDialogBuilder.create();
        errorDialog.show();

    }

    public static String getFileName(String url) {
        String[] imageExtensions = new String[]{"jpg", "jpeg", "png"};
        String[] splitUrl = url.split("/");
        String filename = splitUrl[splitUrl.length - 1];
        return filename;
    }

    public static boolean isImage(String url) {
        try {
            String[] imageExtensions = new String[]{"jpg", "jpeg", "png"};
            String[] splitUrl = url.split("/");
            String filename = splitUrl[splitUrl.length - 1];

            String[] splitName = filename.split("\\.");

            String extension = splitName[splitName.length - 1];

            for (int i = 0; i < imageExtensions.length; i++) {
                if (extension.toLowerCase().equals(imageExtensions[i]))
                    return true;
            }
        } catch (Exception e) {
            //Log.d("esto vayo",e+"");
            return false;
        }

        return false;

    }

    public static void saveProfileJSON(Context c, String s) {

        Writer writer = null;
        try {

            OutputStream out = c.getApplicationContext()
                    .openFileOutput("profile", Context.MODE_PRIVATE);
            writer = new OutputStreamWriter(out);
            writer.write(s);

        } catch (Exception e) {
            //Toast.makeText(c, e.toString(), Toast.LENGTH_LONG).show();
        } finally {
            if (writer != null)
                try {
                    writer.close();
                } catch (Exception e) {
                    //Toast.makeText(c,"Error closing file",Toast.LENGTH_SHORT).show();
                }
        }
    }

    public static boolean deleteFile(File file) {
        boolean deletedAll = true;
        if (file != null) {
            if (file.isDirectory()) {
                String[] children = file.list();
                for (int i = 0; i < children.length; i++) {
                    deletedAll = deleteFile(new File(file, children[i])) && deletedAll;
                }
            } else {
                deletedAll = file.delete();
            }
        }

        return deletedAll;
    }

    public static void setBoolean(Context c, String attribute, boolean val) {
        SharedPreferences sharedPreferences = c.getSharedPreferences("userinfo", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(attribute, val);
        editor.commit();
    }

    public static boolean getBoolean(Context context, String attribute) {

        SharedPreferences sharedPreferences = context.getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(attribute, false);
    }

    public static String getString(Context context, String attribute) {

        SharedPreferences sharedPreferences = context.getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        return sharedPreferences.getString(attribute, "");
    }

    public static void setString(Context context, String attribute, String value) {
        context.getSharedPreferences("userinfo", Context.MODE_PRIVATE).edit().putString(attribute, value).commit();

    }

    public static boolean getBoolean(Context context, String spName, String attribute) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(spName, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(attribute, false);
    }

    public static SharedPreferences getSP(Context context, String spName) {
        return context.getSharedPreferences(spName, Context.MODE_PRIVATE);

    }

    public static void setBoolean(Context c, String spName, String attribute, boolean val) {

        SharedPreferences sharedPreferences = c.getSharedPreferences(spName, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(attribute, val);
        editor.commit();
    }


    public static void saveJSON(Context c, String s, String filename) {

        Writer writer = null;
        try {

            OutputStream out = c.getApplicationContext()
                    .openFileOutput(filename, Context.MODE_PRIVATE);
            writer = new OutputStreamWriter(out);
            writer.write(s);

        } catch (Exception e) {
            //Toast.makeText(c, e.toString(), Toast.LENGTH_LONG).show();
        } finally {
            if (writer != null)
                try {
                    writer.close();
                } catch (Exception e) {
                    //Toast.makeText(c,"Error closing file",Toast.LENGTH_SHORT).show();
                }
        }
    }

    public static String giveMeCover(Context c) {
        SharedPreferences s = c.getSharedPreferences("type", Context.MODE_PRIVATE);
        return s.getString("cover_pic", "default.jpg");
    }

    public static String loadProfileJSON(Context c) {
        String line = null;
        String json = "";
        BufferedReader reader = null;
        try {
            // Open and read the file into a StringBuilder
            InputStream in = c.getApplicationContext().openFileInput("profile");
            reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder jsonString = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                jsonString.append(line);
            }
            json = jsonString.toString();
        } catch (Exception e) {
            //Toast.makeText(c, "could Not Find File", Toast.LENGTH_SHORT).show();
        } finally {
            if (reader != null)
                try {
                    reader.close();
                } catch (Exception e) {
                    //Toast.makeText(c, "Error closing file", Toast.LENGTH_SHORT).show();
                }
        }
        return json;
    }

    public static String returnDayNum(String day) {
        switch (day) {
            case "Sunday":
                return "1";
            case "Monday":
                return "2";
            case "Tuesday":
                return "3";
            case "Wednesday":
                return "4";
            case "Thursday":
                return "5";
            case "Friday":
                return "6";
            case "Saturday":
                return "7";
            default:
                return "0";
        }
    }

    public static String loadJSON(Context c, String filename) {


        String line = null;
        String json = "";
        BufferedReader reader = null;
        try {
            // Open and read the file into a StringBuilder
            InputStream in = c.getApplicationContext().openFileInput(filename);
            reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder jsonString = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                jsonString.append(line);
            }
            json = jsonString.toString();

        } catch (Exception e) {
            //Toast.makeText(c, "could Not Find File", Toast.LENGTH_SHORT).show();
        } finally {
            if (reader != null)
                try {
                    reader.close();
                } catch (Exception e) {
                    //Toast.makeText(c, "Error closing file", Toast.LENGTH_SHORT).show();
                }
        }
        return json;
    }
}
