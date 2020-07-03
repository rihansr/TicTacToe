package com.rihansr.tic_tac_toe;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Vibrator;
import com.google.android.material.snackbar.Snackbar;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Common {

    private static boolean isSoundPlaying = false;
    private static MediaPlayer mediaPlayer;

    public static String PlayerOneName_Key = "PlayerOneName_Key";
    public static String PlayerOneAvatar_Key = "PlayerOneAvatar_Key";
    public static String PlayerTwoName_Key = "PlayerTwoName_Key";
    public static String PlayerTwoAvatar_Key = "PlayerTwoAvatar_Key";
    public static String FirstMove_Key = "FirstMove_Key";
    public static String GameMode_Key = "GameMode_Key";
    public static String PlayMode_Key = "PlayMode_Key";


    /* Vibration*/
    public static void Set_Vibration_Mode(Context context, boolean state){
        context.getSharedPreferences("VibrationMode", Context.MODE_PRIVATE).edit().putBoolean("Vibration_Key",state).apply();
    }

    public static Boolean Get_Vibration_Mode(Context context){
        return context.getSharedPreferences("VibrationMode", Context.MODE_PRIVATE).getBoolean("Vibration_Key",true);
    }

    public static void Vibration(Context context){

        Vibrator vibe = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);

        if (vibe != null) {
            if(Get_Vibration_Mode(context)){
                vibe.vibrate(25);
            }
            else {
                vibe.cancel();
            }
        }
    }


    /* Sound */
    public static void Set_Sound_Mode(Context context, boolean state){
        context.getSharedPreferences("SoundMode", Context.MODE_PRIVATE).edit().putBoolean("Sound_Key",state).apply();
    }

    public static Boolean Get_Sound_Mode(Context context){
        return context.getSharedPreferences("SoundMode", Context.MODE_PRIVATE).getBoolean("Sound_Key",true);
    }

    public static void Sound(Context context, String sound){

        if(Get_Sound_Mode(context)){

            if(isSoundPlaying){
                mediaPlayer.release();
                isSoundPlaying = false;
            }
            switch (sound){
                case "Move":
                    mediaPlayer = MediaPlayer.create(context, R.raw.move_sound);
                    break;

                case "Click":
                    mediaPlayer = MediaPlayer.create(context, R.raw.click_sound);
                    break;

                case "Win":
                    mediaPlayer = MediaPlayer.create(context, R.raw.winning_sound);
                    break;

                case "Draw":
                    mediaPlayer = MediaPlayer.create(context, R.raw.lose_sound);
                    break;

                case "Lose":
                    mediaPlayer = MediaPlayer.create(context, R.raw.lose_sound);
                    break;

                case "Capture":
                    mediaPlayer = MediaPlayer.create(context, R.raw.capture_sound);
                    break;

                default:
                    mediaPlayer = MediaPlayer.create(context, R.raw.move_sound);
            }
            isSoundPlaying = true;
            mediaPlayer.start();
        }
    }


    /* Animation */
    public static void Bounce_Animation(Context context, View view, double amplitude, double frequency){
        Animation Bounce_Animation = AnimationUtils.loadAnimation(context, R.anim.bounce);
        MyBounceInterpolator interpolator = new MyBounceInterpolator(amplitude,frequency);
        Bounce_Animation.setInterpolator(interpolator);
        view.startAnimation(Bounce_Animation);
    }


    /* Font*/
    public static void Set_Font(Context context, View[] view){
        for(View font_View: view){
            if(font_View instanceof TextView){
                ((TextView) font_View).setTypeface(Typeface.createFromAsset(context.getAssets(), "vinque.ttf"));
            }
            if(font_View instanceof Button){
                ((Button) font_View).setTypeface(Typeface.createFromAsset(context.getAssets(), "vinque.ttf"));
            }
        }
    }


    /* Settings */
    public static void Set_Settings_Data(Context context, String key, String value){
        context.getSharedPreferences("Settings_Data", Context.MODE_PRIVATE).edit().putString(key,value).apply();
    }

    public static Map<String, String> Get_Settings_Data(Context context){
        SharedPreferences sp = context.getSharedPreferences("Settings_Data", Context.MODE_PRIVATE);
        Map<String, String> playerDetails = new HashMap<>();
        playerDetails.put(PlayerOneName_Key,  sp.getString(PlayerOneName_Key, "Player One"));
        playerDetails.put(PlayerOneAvatar_Key, sp.getString(PlayerOneAvatar_Key, "AvatarOne"));
        playerDetails.put(PlayerTwoName_Key, sp.getString(PlayerTwoName_Key, "Player Two"));
        playerDetails.put(PlayerTwoAvatar_Key, sp.getString(PlayerTwoAvatar_Key, "AvatarTwo"));
        playerDetails.put(FirstMove_Key, sp.getString(FirstMove_Key, "XO"));
        playerDetails.put(GameMode_Key, sp.getString(GameMode_Key, "Timer"));
        playerDetails.put(PlayMode_Key, sp.getString(PlayMode_Key, "Easy"));
        return playerDetails;
    }


    /* Game Resume */
    public static void Set_Is_Game_Resumed(Context context, boolean state){
        context.getSharedPreferences("ResumeGame", Context.MODE_PRIVATE).edit().putBoolean("ResumeGame_Key",state).apply();
    }

    public static Boolean Get_Is_Game_Resumed(Context context){
        return context.getSharedPreferences("ResumeGame", Context.MODE_PRIVATE).getBoolean("ResumeGame_Key",false);
    }

    public static void Set_Resume_Data(Context context, String key, String value){
        if(value.isEmpty()){
            context.getSharedPreferences("ResumeData", Context.MODE_PRIVATE).edit().putString(key,value+"null").apply();
        }
        else {
            context.getSharedPreferences("ResumeData", Context.MODE_PRIVATE).edit().putString(key,value).apply();
        }
    }

    public static Map<String, String> Get_Resumed_Data(Context context){
        SharedPreferences sp = context.getSharedPreferences("ResumeData", Context.MODE_PRIVATE);
        Map<String, String> position = new HashMap<>();
        position.put("SingleOrDouble",  sp.getString("SingleOrDouble", ""));
        position.put("FirstMove",  sp.getString("FirstMove", "X"));
        position.put("CrossWinCount",  sp.getString("CrossWinCount", "0"));
        position.put("ZeroWinCount",  sp.getString("ZeroWinCount", "0"));

        for(int i=1; i<=9; i++){
            position.put(String.valueOf(i), sp.getString(String.valueOf(i), "null"));
        }

        return position;
    }


    /* Toast */
    public static void Custom_Toast(Context context, String text){
        Typeface toastTf = Typeface.createFromAsset(context.getAssets(), "vinque.ttf");
        Toast toast = Toast.makeText(context,text, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        View toastView = toast.getView();
        TextView textView = toastView.findViewById(android.R.id.message);
        textView.setTextColor(Color.WHITE);
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(16);
        textView.setTypeface(toastTf);
        toastView.setPadding(20,10,20,15);
        toastView.setBackgroundResource(R.drawable.shape_toast);
        toast.show();
    }


    /* Custom SnackBar: */
    public static void Custom_SnackBar(final Context context, final View layout, final String message){

        Typeface snackBarTf = Typeface.createFromAsset(context.getAssets(), "vinque.ttf");

        Snackbar snackbar = Snackbar.make(layout, message, Snackbar.LENGTH_SHORT);
        Snackbar.SnackbarLayout s_layout = (Snackbar.SnackbarLayout) snackbar.getView();
        TextView textView = s_layout.findViewById(R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        textView.setTextSize(16);
        textView.setTypeface(snackBarTf);
        textView.setMaxLines(3);
        s_layout.setBackground(context.getResources().getDrawable(R.drawable.shape_snackbar));
        snackbar.show();
    }


    /* Check Internet Connection: */
    public static boolean Is_Internet_Connected(Context context) {

        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivity != null) {

            NetworkInfo[] info = connectivity.getAllNetworkInfo();

            if (info != null){

                for (NetworkInfo anInfo : info){

                    if (anInfo.getState() == NetworkInfo.State.CONNECTED) {

                        Runtime runtime = Runtime.getRuntime();
                        try {
                            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
                            int     exitValue = ipProcess.waitFor();
                            return (exitValue == 0);
                        }
                        catch (IOException e) {
                            e.printStackTrace();
                        }
                        catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        return false;
                    }
                }
            }
        }
        return false;
    }
}
