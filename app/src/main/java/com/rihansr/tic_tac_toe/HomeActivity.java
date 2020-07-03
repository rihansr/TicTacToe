package com.rihansr.tic_tac_toe;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Vibrator;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;

@SuppressLint("SetTextI18n")
public class HomeActivity extends AppCompatActivity {

    private AutoWritingText appName;
    private LinearLayout Center_Layout;
    private Button one_player_btn, two_players_btn, howToPlay, quit_game;
    private ImageButton sound_icon, vibration_icon, share_icon;
    private TextView sound_tv, vibration_tv, share_tv;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Set_Id();

        Common.Set_Font(getApplicationContext(), new View[]{appName, one_player_btn, two_players_btn, howToPlay, quit_game, sound_tv, vibration_tv, share_tv});

        Set_Layout_Size();

        appName.animateText(getResources().getString(R.string.app_name));
        appName.setCharacterDelay(180);
        appName.setShadowLayer(20f, 5.0f, 5.0f, Color.parseColor("#2F0400"));

        if(Common.Get_Sound_Mode(getApplicationContext())){
            sound_icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_sound_on));
            sound_tv.setText("Sound On");
        }
        else {
            sound_icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_sound_off));
            sound_tv.setText("Sound Off");
        }

        if(Common.Get_Vibration_Mode(getApplicationContext())){
            vibration_icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_vibration_on));
            vibration_tv.setText("Vibration On");
        }
        else {
            vibration_icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_vibration_off));
            vibration_tv.setText("Vibration Off");
        }
    }

    private void Set_Id() {
        appName = findViewById(R.id.appName_id);
        Center_Layout = findViewById(R.id.center_layout);
        one_player_btn = findViewById(R.id.one_player_btn);
        two_players_btn = findViewById(R.id.two_players_btn);
        howToPlay = findViewById(R.id.howToPlay_btn);
        quit_game = findViewById(R.id.quit_game);
        sound_icon = findViewById(R.id.sound_icon);
        sound_tv = findViewById(R.id.sound_tv);
        vibration_icon = findViewById(R.id.vibrate_icon);
        vibration_tv = findViewById(R.id.vibrate_tv);
        share_icon = findViewById(R.id.share_icon);
        share_tv = findViewById(R.id.share_tv);
    }

    private void Set_Layout_Size() {
        try {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) Center_Layout.getLayoutParams();
            int size = displayMetrics.widthPixels;

            if(size <= 500){
                params.height = size - (size/12);
                params.width = size - (size/12);
            }
            else{
                params.height = size - (size/10);
                params.width = size - (size/10);
            }

            Center_Layout.setLayoutParams(params);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void Select_Option(View view) {

        if(view.getId() == R.id.one_player_btn){
            Common.Bounce_Animation(getApplicationContext(), view, 0.1, 10);
            Common.Sound(getApplicationContext(), "Click");
            Common.Vibration(getApplicationContext());

            if(Common.Get_Is_Game_Resumed(getApplicationContext()) && !Common.Get_Resumed_Data(getApplicationContext()).get("SingleOrDouble").isEmpty()){

                Resume_Dialog("toSinglePlayer");
            }
            else {
                Intent intent = new Intent(getApplicationContext(),SettingsActivity.class);
                intent.putExtra("toSinglePlayer",true);
                startActivity(intent);
            }
        }

        if(view.getId() == R.id.two_players_btn){
            Common.Bounce_Animation(getApplicationContext(), view, 0.1, 10);
            Common.Sound(getApplicationContext(), "Click");
            Common.Vibration(getApplicationContext());

            if(Common.Get_Is_Game_Resumed(getApplicationContext()) && !Common.Get_Resumed_Data(getApplicationContext()).get("SingleOrDouble").isEmpty()){
                Resume_Dialog("toDoublePlayer");
            }
            else {
                Intent intent = new Intent(getApplicationContext(),SettingsActivity.class);
                intent.putExtra("toDoublePlayer",true);
                startActivity(intent);
            }
        }

        if(view.getId() == R.id.howToPlay_btn){
            Common.Bounce_Animation(getApplicationContext(), view, 0.1, 10);
            Common.Sound(getApplicationContext(), "Click");
            Common.Vibration(getApplicationContext());
            How_To_Play();
        }

        if(view.getId() == R.id.quit_game){
            Common.Bounce_Animation(getApplicationContext(), view, 0.1, 10);
            Common.Sound(getApplicationContext(), "Click");
            Common.Vibration(getApplicationContext());

            android.os.Process.killProcess(android.os.Process.myPid());
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            startActivity(intent);
        }

        if(view.getId() == R.id.sound_btn){
            Common.Bounce_Animation(getApplicationContext(), sound_icon, 0.15, 5);
            Common.Sound(getApplicationContext(), "Move");
            Common.Vibration(getApplicationContext());

            if(sound_icon.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.ic_sound_on).getConstantState()){
                sound_icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_sound_off));
                sound_tv.setText("Sound Off");
                Common.Set_Sound_Mode(getApplicationContext(),false);
            }
            else {
                sound_icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_sound_on));
                sound_tv.setText("Sound On");
                Common.Set_Sound_Mode(getApplicationContext(),true);
            }
        }

        if(view.getId() == R.id.vibrate_btn){
            Common.Bounce_Animation(getApplicationContext(), vibration_icon, 0.15, 5);
            Common.Sound(getApplicationContext(), "Move");
            Common.Vibration(getApplicationContext());

            if(vibration_icon.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.ic_vibration_on).getConstantState()){
                vibration_icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_vibration_off));
                vibration_tv.setText("Vibration Off");
                Common.Set_Vibration_Mode(getApplicationContext(),false);
                Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                if (vibe != null) vibe.cancel();
            }
            else {
                vibration_icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_vibration_on));
                vibration_tv.setText("Vibration On");
                Common.Set_Vibration_Mode(getApplicationContext(),true);
                Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                if (vibe != null) vibe.vibrate(35);
            }
        }

        if(view.getId() == R.id.share_btn){
            Common.Bounce_Animation(getApplicationContext(), share_icon, 0.15, 5);
            Common.Sound(getApplicationContext(), "Click");
            Common.Vibration(getApplicationContext());

            if(isPermissionGranted(111)){
                Share_Apk();
            }
        }
    }

    private void Resume_Dialog(final String key){

        View view = View.inflate(getApplicationContext(),R.layout.resume_layout, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(view);
        alertDialogBuilder.setCancelable(true);

        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(alertDialog.getWindow()).getDecorView().setBackgroundColor(Color.TRANSPARENT);
        alertDialog.show();

        Common.Bounce_Animation(getApplicationContext(), view, 0.15, 5);

        LinearLayout dialog_Layout = view.findViewById(R.id.dialog_layout);
        TextView resume_Message = view.findViewById(R.id.resume_msg);
        Button resume_Game = view.findViewById(R.id.resume_game);
        Button new_Game = view.findViewById(R.id.new_game);

        /* Set Font */
        Common.Set_Font(getApplicationContext(), new View[]{resume_Message, resume_Game, new_Game});

        /* Set Display Size */
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) dialog_Layout.getLayoutParams();
        int size = displayMetrics.widthPixels;

        if(size <= 500){
            params.height = size - (size/7);
            params.width = size - (size/7);
        }
        else{
            params.height = size - (size/5);
            params.width = size - (size/5);
        }

        dialog_Layout.setLayoutParams(params);

        resume_Game.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Common.Bounce_Animation(getApplicationContext(), v, 0.1, 10);
                Common.Sound(getApplicationContext(), "Click");
                Common.Vibration(getApplicationContext());

                switch (Common.Get_Resumed_Data(getApplicationContext()).get("SingleOrDouble")){
                    case "Single":
                        startActivity(new Intent(getApplicationContext(),SinglePlayerMode.class));
                        break;

                    case "Double":
                        startActivity(new Intent(getApplicationContext(),DoublePlayerMode.class));
                        break;
                }
            }
        });

        new_Game.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Common.Bounce_Animation(getApplicationContext(), v, 0.1, 10);
                Common.Sound(getApplicationContext(), "Click");
                Common.Vibration(getApplicationContext());

                Common.Set_Is_Game_Resumed(getApplicationContext(),false);
                Common.Set_Resume_Data(getApplicationContext(),"SingleOrDouble", "");
                Common.Set_Resume_Data(getApplicationContext(),"FirstMove", "X");
                Common.Set_Resume_Data(getApplicationContext(),"CrossWinCount", "0");
                Common.Set_Resume_Data(getApplicationContext(),"ZeroWinCount", "0");

                for (int i=1; i<=9; i++){
                    Common.Set_Resume_Data(getApplicationContext(),String.valueOf(i), "null");
                }

                Intent intent = new Intent(getApplicationContext(),SettingsActivity.class);
                intent.putExtra(key,true);
                startActivity(intent);
            }
        });
    }

    private void How_To_Play(){
        View view = View.inflate(getApplicationContext(),R.layout.howtoplay_layout, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(view);
        alertDialogBuilder.setCancelable(true);

        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(alertDialog.getWindow()).getDecorView().setBackgroundColor(Color.TRANSPARENT);
        alertDialog.show();

        Common.Bounce_Animation(getApplicationContext(), view, 0.15, 5);

        LinearLayout dialog_Layout = view.findViewById(R.id.dialog_layout);
        TextView howToPlay_Title = view.findViewById(R.id.howToPlay_Title);
        TextView howToPlay_Tv = view.findViewById(R.id.howToPlay_Tv);

        /* Font */
        Common.Set_Font(getApplicationContext(), new View[]{howToPlay_Title, howToPlay_Tv});

        /* Set Display Size */
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) dialog_Layout.getLayoutParams();
        int size = displayMetrics.widthPixels;

        if(size <= 500){
            params.height = size - (size/7);
            params.width = size - (size/7);
        }
        else{
            params.height = size - (size/5);
            params.width = size - (size/5);
        }

        dialog_Layout.setLayoutParams(params);
    }

    @Override public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case 111:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Share_Apk();
                }
                break;
        }

    }

    public  boolean isPermissionGranted(int requestCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                return true;
            }
            else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, requestCode);
                return false;
            }
        }
        else {
            return true;
        }
    }

    private void Share_Apk() {
        try {
            File initialApkFile = new File(getPackageManager().getApplicationInfo(getPackageName(), 0).sourceDir);

            File tempFile = new File(getExternalCacheDir() + "/ExtractedApk");

            if (!tempFile.isDirectory())
                if (!tempFile.mkdirs())
                    return;

            tempFile = new File(tempFile.getPath() + "/" + getResources().getString(R.string.app_name) + ".apk");

            if (!tempFile.exists()) {
                if (!tempFile.createNewFile()) {
                    return;
                }
            }

            InputStream in = new FileInputStream(initialApkFile);
            OutputStream out = new FileOutputStream(tempFile);

            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();

            Intent share = new Intent();
            share.setAction(Intent.ACTION_SEND);
            share.setType("*/*");
            share.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(tempFile));
            startActivity(Intent.createChooser(share, "Share " + getResources().getString(R.string.app_name) + " Via"));
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
