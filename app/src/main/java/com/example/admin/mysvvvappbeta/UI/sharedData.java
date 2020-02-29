package com.example.admin.mysvvvappbeta.UI;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.admin.mysvvvappbeta.L.LGC;
import com.example.admin.mysvvvappbeta.models.user;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by asad mirza on 14-01-2018.
 */

public class sharedData {

    SharedPreferences pref;
    static private final String userFLAG = "initialized";

    static private final String sharedPreName = "USER_PROFILE";

  public void saveUserData(user u, Context mContext) {


        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser == null) {

            mContext.startActivity(new Intent(mContext, LGC.class));

        } else {
            pref= mContext.getSharedPreferences(sharedPreName, Context.MODE_MULTI_PROCESS);
            SharedPreferences.Editor edt = pref.edit();

            edt.putString("name", firebaseUser.getDisplayName());
            edt.putString("cid",u.getCid());
            edt.putString("college", u.getCollege());
            edt.putString("course", u.getCourse());
            edt.putString("branch", u.getBranch());
            edt.putString("year", u.getYear());
            edt.putString("section",u.getSection());
            edt.putString("website",u.getToken());
            edt.putString("photo",u.getPhotoId());
            edt.putString("type",u.getUserType());

            boolean commit = edt.commit();
            if (!commit)
                mContext.startActivity(new Intent(mContext,LGC.class));

        }



    }

                 public user getUserData(final Context mContext){

                pref = mContext.getSharedPreferences(sharedPreName, Context.MODE_MULTI_PROCESS);


                final user u = new user();
                String name = pref.getString("name", null);
                String college = pref.getString("college", null);
                String course = pref.getString("course", null);
                String branch = pref.getString("branch", null);
                String year = pref.getString("year", null);
                String section = pref.getString("section",null);
                    String website = pref.getString("website",null);
                    String photo = pref.getString("photo",null);
                    String type =   pref.getString("type",null);
                    u.setPhotoId(photo);
                    u.setCollege(college);
                    u.setUserType(type);
                u.setName(name);
                u.setCourse(course);
                u.setBranch(branch);
                u.setYear(year);
                u.setSection(section);
                u.setToken(website);
                return u;


            }
             public void setLastSeenNotif(Long time, Context mContext){
                pref= mContext.getSharedPreferences(sharedPreName, Context.MODE_MULTI_PROCESS);
                SharedPreferences.Editor edt = pref.edit();

                edt.putLong("lastseen_notif",time);
                edt.commit();



            }

             public  long getLastSeenNotif(Context mContext){

                pref = mContext.getSharedPreferences(sharedPreName, Context.MODE_MULTI_PROCESS);


                final user u = new user();
                long lastSeen = pref.getLong("lastseen_notif",0);
                return lastSeen;



            }
   public void setLastSeenNotice(Long time, Context mContext){
        pref= mContext.getSharedPreferences(sharedPreName, Context.MODE_MULTI_PROCESS);
        SharedPreferences.Editor edt = pref.edit();

        edt.putLong("lastseen_notice",time);
        edt.commit();



    }

    public long getLastSeenNotice(Context mContext){

        pref = mContext.getSharedPreferences(sharedPreName, Context.MODE_MULTI_PROCESS);


        final user u = new user();
        long lastSeen = pref.getLong("lastseen_notice",0);
        return lastSeen;



    }








}



