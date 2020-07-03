package com.rihansr.tic_tac_toe;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Environment;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;
import java.util.Random;

@SuppressLint("SetTextI18n")
public class DoublePlayerMode extends AppCompatActivity {

    private RelativeLayout Center_Layout, Content_Layout;
    private TextView cell_1, cell_2, cell_3, cell_4, cell_5, cell_6, cell_7, cell_8, cell_9;
    private TextView player_One_Name, player_Two_Name;
    private ImageView player_One_Avatar, player_Two_Avatar;
    private boolean screenOff = false;

    private boolean isCrossTurn;
    private String firstMove = "X";

    private boolean isOnClassicMode;
    private CountDownTimer Move_Timer;
    private boolean isTimerOn = false;
    private ProgressBar Cross_Timer_Progress, Zero_Timer_Progress;

    private boolean isGameWin = false;
    private TextView versus_Tv, cross_Win_Tv, zero_Win_Tv;
    private View view_Line_Left, view_Line_Top, view_Line_Right, view_Line_Bottom, view_Line_Horizontal, view_Line_Vertical, view_Line_Front, view_Line_Back;
    private boolean isGameDraw = false;
    private AlertDialog alertDialog;

    private TextView home_Tv, settings_Tv, new_Game_Tv;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_double_player_mode);

        Set_Id();

        View[] views = {player_One_Name, player_Two_Name, versus_Tv, cross_Win_Tv, zero_Win_Tv, home_Tv, settings_Tv, new_Game_Tv};
        Common.Set_Font(getApplicationContext(), views);

        Set_Layout_Size();

        Move_Timer = new CountDownTimer(11000, 1000) {

            @SuppressLint("SetTextI18n")
            public void onTick(long millisUntilFinished) {
                if(isCrossTurn){
                    Cross_Timer_Progress.setVisibility(View.VISIBLE);
                    Cross_Timer_Progress.setProgress((int) millisUntilFinished/1000);
                }
                else {
                    Zero_Timer_Progress.setVisibility(View.VISIBLE);
                    Zero_Timer_Progress.setProgress((int) millisUntilFinished/1000);
                }

                isTimerOn = true;
            }

            public void onFinish() {
                Stop_Timer();
                AutoSet_Cross_Zero();
            }
        };

        Load_Name();

        Load_Avatar();

        Load_Game_Mode();

        if(Common.Get_Is_Game_Resumed(getApplicationContext()) && !Common.Get_Resumed_Data(getApplicationContext()).get("SingleOrDouble").isEmpty()){
            Reload_Game();
        }
        else {
            Load_First_Move();
        }
    }

    private void Set_Id() {
        Center_Layout = findViewById(R.id.center_layout);
        Content_Layout = findViewById(R.id.content_layout);

        cell_1 = findViewById(R.id.box_one);
        cell_2 = findViewById(R.id.box_two);
        cell_3 = findViewById(R.id.box_three);
        cell_4 = findViewById(R.id.box_four);
        cell_5 = findViewById(R.id.box_five);
        cell_6 = findViewById(R.id.box_six);
        cell_7 = findViewById(R.id.box_seven);
        cell_8 = findViewById(R.id.box_eight);
        cell_9 = findViewById(R.id.box_nine);

        player_One_Name = findViewById(R.id.player_one_name);
        player_One_Avatar = findViewById(R.id.player_one_avatar);

        player_Two_Name = findViewById(R.id.player_two_name);
        player_Two_Avatar = findViewById(R.id.player_two_avatar);

        Cross_Timer_Progress = findViewById(R.id.x_progress);
        Zero_Timer_Progress = findViewById(R.id.o_progress);

        versus_Tv = findViewById(R.id.versus_tv);
        cross_Win_Tv = findViewById(R.id.x_won_count);
        zero_Win_Tv = findViewById(R.id.o_won_count);

        view_Line_Left = findViewById(R.id.draw_line_left);
        view_Line_Top = findViewById(R.id.draw_line_top);
        view_Line_Right = findViewById(R.id.draw_line_right);
        view_Line_Bottom = findViewById(R.id.draw_line_bottom);
        view_Line_Horizontal = findViewById(R.id.draw_line_horizontal);
        view_Line_Vertical = findViewById(R.id.draw_line_vertical);
        view_Line_Front = findViewById(R.id.draw_line_front);
        view_Line_Back = findViewById(R.id.draw_line_back);

        home_Tv = findViewById(R.id.home_tv);
        settings_Tv = findViewById(R.id.settings_tv);
        new_Game_Tv = findViewById(R.id.newGame_tv);
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

    private void Load_Name() {

        String NameOne = Common.Get_Settings_Data(getApplicationContext()).get(Common.PlayerOneName_Key);
        String NameTwo = Common.Get_Settings_Data(getApplicationContext()).get(Common.PlayerTwoName_Key);

        if(!NameOne.isEmpty()){
            player_One_Name.setText(NameOne);
        }
        else {
            player_One_Name.setText("Player One");
        }

        if(!NameTwo.isEmpty()){
            player_Two_Name.setText(NameTwo);
        }
        else {
            player_Two_Name.setText("Player Two");
        }
    }

    private void Load_Avatar() {

        switch (Common.Get_Settings_Data(getApplicationContext()).get(Common.PlayerOneAvatar_Key)){

            case "AvatarOne":
                player_One_Avatar.setImageDrawable(getResources().getDrawable(R.drawable.avatar_one));
                break;

            case "AvatarTwo":
                player_One_Avatar.setImageDrawable(getResources().getDrawable(R.drawable.avatar_two));
                break;

            case "AvatarThree":
                player_One_Avatar.setImageDrawable(getResources().getDrawable(R.drawable.avatar_three));
                break;

            case "AvatarFour":
                player_One_Avatar.setImageDrawable(getResources().getDrawable(R.drawable.avatar_four));
                break;

            case "AvatarFive":
                player_One_Avatar.setImageDrawable(getResources().getDrawable(R.drawable.avatar_five));
                break;

            case "AvatarSix":
                player_One_Avatar.setImageDrawable(getResources().getDrawable(R.drawable.avatar_six));
                break;

            case "AvatarSeven":
                player_One_Avatar.setImageDrawable(getResources().getDrawable(R.drawable.avatar_seven));
                break;

            case "AvatarEight":
                player_One_Avatar.setImageDrawable(getResources().getDrawable(R.drawable.avatar_eight));
                break;

            default:
                player_One_Avatar.setImageDrawable(getResources().getDrawable(R.drawable.avatar_one));

        }

        switch (Common.Get_Settings_Data(getApplicationContext()).get(Common.PlayerTwoAvatar_Key)){

            case "AvatarOne":
                player_Two_Avatar.setImageDrawable(getResources().getDrawable(R.drawable.avatar_one));
                break;

            case "AvatarTwo":
                player_Two_Avatar.setImageDrawable(getResources().getDrawable(R.drawable.avatar_two));
                break;

            case "AvatarThree":
                player_Two_Avatar.setImageDrawable(getResources().getDrawable(R.drawable.avatar_three));
                break;

            case "AvatarFour":
                player_Two_Avatar.setImageDrawable(getResources().getDrawable(R.drawable.avatar_four));
                break;

            case "AvatarFive":
                player_Two_Avatar.setImageDrawable(getResources().getDrawable(R.drawable.avatar_five));
                break;

            case "AvatarSix":
                player_Two_Avatar.setImageDrawable(getResources().getDrawable(R.drawable.avatar_six));
                break;

            case "AvatarSeven":
                player_Two_Avatar.setImageDrawable(getResources().getDrawable(R.drawable.avatar_seven));
                break;

            case "AvatarEight":
                player_Two_Avatar.setImageDrawable(getResources().getDrawable(R.drawable.avatar_eight));
                break;

            default:
                player_Two_Avatar.setImageDrawable(getResources().getDrawable(R.drawable.avatar_one));

        }
    }

    private void Load_First_Move() {
        switch (Common.Get_Settings_Data(getApplicationContext()).get(Common.FirstMove_Key)){

            case "X":
                isCrossTurn = true;
                Common.Custom_Toast(getApplicationContext(), player_One_Name.getText().toString()+", it's your turn.");
                Start_Timer();
                break;

            case "O":
                isCrossTurn = false;
                Common.Custom_Toast(getApplicationContext(), player_Two_Name.getText().toString()+", it's your turn.");
                Start_Timer();
                break;

            case "XO":
                if(firstMove.equals("X")){
                    isCrossTurn = true;
                    Common.Custom_Toast(getApplicationContext(), player_One_Name.getText().toString()+", it's your turn.");
                    Start_Timer();
                    firstMove = "O";
                }
                else {
                    isCrossTurn = false;
                    Common.Custom_Toast(getApplicationContext(), player_Two_Name.getText().toString()+", it's your turn.");
                    Start_Timer();
                    firstMove = "X";
                }
                break;

            default:
                isCrossTurn = true;
                Common.Custom_Toast(getApplicationContext(), player_One_Name.getText().toString()+", it's your turn.");
                Start_Timer();
        }
    }

    private void Load_Game_Mode(){

        switch (Common.Get_Settings_Data(getApplicationContext()).get(Common.GameMode_Key)){

            case "Classic":
                isOnClassicMode = true;
                break;

            case "Timer":
                isOnClassicMode = false;
                break;

            default:
                isOnClassicMode = false;
        }
    }

    public void Set_Cross_Zero(View view) {

        if(view instanceof TextView){
            TextView button = (TextView) view;
            if(button.getText().toString().isEmpty()){
                if(isTimerOn){
                    Stop_Timer();
                }
                Start_Timer();
                Check_Cross_Zero(button);
            }
        }
    }

    private void Check_Cross_Zero(TextView button){

        Common.Set_Is_Game_Resumed(getApplicationContext(),true);
        Resume_Game();

        if(isCrossTurn){
            Common.Bounce_Animation(getApplicationContext(), button, 0.025, 10);
            Common.Vibration(getApplicationContext());
            Common.Sound(getApplicationContext(), "Move");
            button.setText("X");
            Set_Text_Style(button,button.getText().toString());
            isCrossTurn = false;
            Match_Cross();
        }
        else {
            Common.Bounce_Animation(getApplicationContext(), button, 0.025, 10);
            Common.Vibration(getApplicationContext());
            Common.Sound(getApplicationContext(), "Move");
            button.setText("O");
            Set_Text_Style(button,button.getText().toString());
            isCrossTurn = true;
            Match_Zero();
        }
    }

    private void Set_Text_Style(TextView button, String text){

        if(!text.isEmpty()){
            if(text.equals("X")){
                button.setText(text);
                button.setShadowLayer(20f, 5.0f, 5.0f, Color.parseColor("#300A02"));
                button.setTextColor(getResources().getColor(R.color.colorCross));
                button.setTypeface(Typeface.createFromAsset(getAssets(), "tintilla.ttf"));
            }
            else {
                button.setText(text);
                button.setShadowLayer(20f, 5.0f, 5.0f, Color.parseColor("#022901"));
                button.setTextColor(getResources().getColor(R.color.colorZero));
                button.setTypeface(Typeface.createFromAsset(getAssets(), "tintilla.ttf"));
            }
        }
    }

    private int Generate_Move_Rules(){
        String _1 = cell_1.getText().toString();
        String _2 = cell_2.getText().toString();
        String _3 = cell_3.getText().toString();
        String _4 = cell_4.getText().toString();
        String _5 = cell_5.getText().toString();
        String _6 = cell_6.getText().toString();
        String _7 = cell_7.getText().toString();
        String _8 = cell_8.getText().toString();
        String _9 = cell_9.getText().toString();

        if((_1.equals("X") && _2.equals("X") && _3.isEmpty()) || (_1.equals("O") && _2.equals("O") && _3.isEmpty())){
            return 3;
        }
        else if((_1.equals("X") && _2.isEmpty() && _3.equals("X")) || (_1.equals("O") && _2.isEmpty() && _3.equals("O"))){
            return 2;
        }
        else if((_1.isEmpty() && _2.equals("X") && _3.equals("X")) || (_1.isEmpty() && _2.equals("O") && _3.equals("O"))){
            return 1;
        }

        else if((_1.equals("X") && _4.equals("X") && _7.isEmpty()) || (_1.equals("O") && _4.equals("O") && _7.isEmpty())){
            return 7;
        }
        else if((_1.equals("X") && _4.isEmpty() && _7.equals("X")) || (_1.equals("O") && _4.isEmpty() && _7.equals("O"))){
            return 4;
        }
        else if((_1.isEmpty() && _4.equals("X") && _7.equals("X")) || (_1.isEmpty() && _4.equals("O") && _7.equals("O"))){
            return 1;
        }

        else if((_1.equals("X") && _5.equals("X") && _9.isEmpty()) || (_1.equals("O") && _5.equals("O") && _9.isEmpty())){
            return 9;
        }
        else if((_1.equals("X") && _5.isEmpty() && _9.equals("X")) || (_1.equals("O") && _5.isEmpty() && _9.equals("O"))){
            return 5;
        }
        else if((_1.isEmpty() && _5.equals("X") && _9.equals("X")) || (_1.isEmpty() && _5.equals("O") && _9.equals("O"))){
            return 1;
        }

        else if((_2.equals("X") && _5.equals("X") && _8.isEmpty()) || (_2.equals("O") && _5.equals("O") && _8.isEmpty())){
            return 8;
        }
        else if((_2.equals("X") && _5.isEmpty() && _8.equals("X")) || (_2.equals("O") && _5.isEmpty() && _8.equals("O"))){
            return 5;
        }
        else if((_2.isEmpty() && _5.equals("X") && _8.equals("X")) || (_2.isEmpty() && _5.equals("O") && _8.equals("O"))){
            return 2;
        }

        else if((_3.equals("X") && _6.equals("X") && _9.isEmpty()) || (_3.equals("O") && _6.equals("O") && _9.isEmpty())){
            return 9;
        }
        else if((_3.equals("X") && _6.isEmpty() && _9.equals("X")) || (_3.equals("O") && _6.isEmpty() && _9.equals("O"))){
            return 6;
        }
        else if((_3.isEmpty() && _6.equals("X") && _9.equals("X")) || (_3.isEmpty() && _6.equals("O") && _9.equals("O"))){
            return 3;
        }

        else if((_3.equals("X") && _5.equals("X") && _7.isEmpty()) || (_3.equals("O") && _5.equals("O") && _7.isEmpty())){
            return 7;
        }
        else if((_3.equals("X") && _5.isEmpty() && _7.equals("X")) || (_3.equals("O") && _5.isEmpty() && _7.equals("O"))){
            return 5;
        }
        else if((_3.isEmpty() && _5.equals("X") && _7.equals("X")) || (_3.isEmpty() && _5.equals("O") && _7.equals("O"))){
            return 3;
        }

        else if((_4.equals("X") && _5.equals("X") && _6.isEmpty()) || (_4.equals("O") && _5.equals("O") && _6.isEmpty())){
            return 6;
        }
        else if((_4.equals("X") && _5.isEmpty() && _6.equals("X")) || (_4.equals("O") && _5.isEmpty() && _6.equals("O"))){
            return 5;
        }
        else if((_4.isEmpty() && _5.equals("X") && _6.equals("X")) || (_4.isEmpty() && _5.equals("O") && _6.equals("O"))){
            return 4;
        }

        else if((_7.equals("X") && _8.equals("X") && _9.isEmpty()) || (_7.equals("O") && _8.equals("O") && _9.isEmpty())){
            return 9;
        }
        else if((_7.equals("X") && _8.isEmpty() && _9.equals("X")) || (_7.equals("O") && _8.isEmpty() && _9.equals("O"))){
            return 8;
        }
        else if((_7.isEmpty() && _8.equals("X") && _9.equals("X")) || (_7.isEmpty() && _8.equals("O") && _9.equals("O"))){
            return 7;
        }

        else if((_1.equals("X") && _3.equals("X") && _7.isEmpty()) || (_1.equals("O") && _3.equals("O") && _7.isEmpty())){
            return 7;
        }
        else if((_1.equals("X") && _3.isEmpty() && _7.equals("X")) || (_1.equals("O") && _3.isEmpty() && _7.equals("O"))){
            return 3;
        }
        else if((_1.isEmpty() && _3.equals("X") && _7.equals("X")) || (_1.isEmpty() && _3.equals("O") && _7.equals("O"))){
            return 1;
        }

        else if((_1.equals("X") && _3.equals("X") && _9.isEmpty()) || (_1.equals("O") && _3.equals("O") && _9.isEmpty())){
            return 9;
        }
        else if((_1.equals("X") && _3.isEmpty() && _9.equals("X")) || (_1.equals("O") && _3.isEmpty() && _9.equals("O"))){
            return 3;
        }
        else if((_1.isEmpty() && _3.equals("X") && _9.equals("X")) || (_1.isEmpty() && _3.equals("O") && _9.equals("O"))){
            return 1;
        }

        else if((_1.equals("X") && _9.equals("X") && _7.isEmpty()) || (_1.equals("O") && _9.equals("O") && _7.isEmpty())){
            return 7;
        }
        else if((_1.equals("X") && _9.isEmpty() && _7.equals("X")) || (_1.equals("O") && _9.isEmpty() && _7.equals("O"))){
            return 9;
        }
        else if((_1.isEmpty() && _9.equals("X") && _7.equals("X")) || (_1.isEmpty() && _9.equals("O") && _7.equals("O"))){
            return 1;
        }

        else if((_3.equals("X") && _9.equals("X") && _7.isEmpty()) || (_3.equals("O") && _9.equals("O") && _7.isEmpty())){
            return 7;
        }
        else if((_3.equals("X") && _9.isEmpty() && _7.equals("X")) || (_3.equals("O") && _9.isEmpty() && _7.equals("O"))){
            return 9;
        }
        else if((_3.isEmpty() && _9.equals("X") && _7.equals("X")) || (_3.isEmpty() && _9.equals("O") && _7.equals("O"))){
            return 3;
        }
        else {
            return new Random().nextInt(9)+1;
        }
    }

    private void Match_Cross() {
        String _1 = cell_1.getText().toString();
        String _2 = cell_2.getText().toString();
        String _3 = cell_3.getText().toString();
        String _4 = cell_4.getText().toString();
        String _5 = cell_5.getText().toString();
        String _6 = cell_6.getText().toString();
        String _7 = cell_7.getText().toString();
        String _8 = cell_8.getText().toString();
        String _9 = cell_9.getText().toString();

        if(_1.equals("X") && _2.equals("X") && _3.equals("X")){
            Stop_Timer();
            Disable_Game_Move();
            isGameWin = true;
            Show_Win_Line(2);
            Win_Dialog(true, player_One_Name.getText().toString()+" Wins!");
            Cross_Win_Count();
        }

        else if(_1.equals("X") && _4.equals("X") && _7.equals("X")){
            Stop_Timer();
            Disable_Game_Move();
            isGameWin = true;
            Show_Win_Line(1);
            Win_Dialog(true, player_One_Name.getText().toString()+" Wins!");
            Cross_Win_Count();
        }

        else if(_1.equals("X") && _3.equals("X") && _5.equals("X") && _7.equals("X") && _9.equals("X") ){
            Stop_Timer();
            Disable_Game_Move();
            isGameWin = true;
            switch (new Random().nextInt(2)+1){
                case 1:
                    Show_Win_Line(8);
                    break;

                case 2:
                    Show_Win_Line(7);
                    break;
            }
            Win_Dialog(true, player_One_Name.getText().toString()+" Wins!");
            Cross_Win_Count();
        }

        else if(_1.equals("X") && _5.equals("X") && _9.equals("X")){
            Stop_Timer();
            Disable_Game_Move();
            isGameWin = true;
            Show_Win_Line(8);
            Win_Dialog(true, player_One_Name.getText().toString()+" Wins!");
            Cross_Win_Count();
        }

        else if(_2.equals("X") && _4.equals("X") && _5.equals("X") && _6.equals("X") && _8.equals("X") ){

            Stop_Timer();
            Disable_Game_Move();
            isGameWin = true;
            switch (new Random().nextInt(2)+1){
                case 1:
                    Show_Win_Line(5);
                    break;

                case 2:
                    Show_Win_Line(6);
                    break;
            }            Win_Dialog(true, player_One_Name.getText().toString()+" Wins!");
            Cross_Win_Count();
        }

        else if(_2.equals("X") && _5.equals("X") && _8.equals("X")){
            Stop_Timer();
            Disable_Game_Move();
            isGameWin = true;
            Show_Win_Line(5);
            Win_Dialog(true, player_One_Name.getText().toString()+" Wins!");
            Cross_Win_Count();
        }

        else if(_3.equals("X") && _6.equals("X") && _9.equals("X")){
            Stop_Timer();
            Disable_Game_Move();
            isGameWin = true;
            Show_Win_Line(3);
            Win_Dialog(true, player_One_Name.getText().toString()+" Wins!");
            Cross_Win_Count();
        }

        else if(_3.equals("X") && _5.equals("X") && _7.equals("X")){
            Stop_Timer();
            Disable_Game_Move();
            isGameWin = true;
            Show_Win_Line(7);
            Win_Dialog(true, player_One_Name.getText().toString()+" Wins!");
            Cross_Win_Count();
        }

        else if(_4.equals("X") && _5.equals("X") && _6.equals("X")){
            Stop_Timer();
            Disable_Game_Move();
            isGameWin = true;
            Show_Win_Line(6);
            Win_Dialog(true, player_One_Name.getText().toString()+" Wins!");
            Cross_Win_Count();
        }

        else if(_7.equals("X") && _8.equals("X") && _9.equals("X")){
            Stop_Timer();
            Disable_Game_Move();
            isGameWin = true;
            Show_Win_Line(4);
            Win_Dialog(true, player_One_Name.getText().toString()+" Wins!");
            Cross_Win_Count();
        }

        else if(_1.equals("X") && _3.equals("X") && _5.equals("X") && _7.equals("X") && _9.equals("X") ){
            Stop_Timer();
            Disable_Game_Move();
            isGameWin = true;
            Show_Win_Line(7);
            Win_Dialog(true, player_One_Name.getText().toString()+" Wins!");
            Cross_Win_Count();
        }

        else if(!_1.isEmpty() && !_2.isEmpty() && !_3.isEmpty() && !_4.isEmpty() && !_5.isEmpty() && !_6.isEmpty() && !_7.isEmpty() && !_8.isEmpty() && !_9.isEmpty() && !isGameWin){
            Stop_Timer();
            Disable_Game_Move();
            isGameWin = false;
            isGameDraw = true;
            Win_Dialog(false, getResources().getString(R.string.draw_msg));
        }
    }

    private void Match_Zero() {
        String _1 = cell_1.getText().toString();
        String _2 = cell_2.getText().toString();
        String _3 = cell_3.getText().toString();
        String _4 = cell_4.getText().toString();
        String _5 = cell_5.getText().toString();
        String _6 = cell_6.getText().toString();
        String _7 = cell_7.getText().toString();
        String _8 = cell_8.getText().toString();
        String _9 = cell_9.getText().toString();

        if(_1.equals("O") && _2.equals("O") && _3.equals("O")){
            Stop_Timer();
            Disable_Game_Move();
            isGameWin = true;
            Show_Win_Line(2);
            Win_Dialog(true, player_Two_Name.getText().toString()+" Wins!");
            Zero_Win_Count();
        }

        else if(_1.equals("O") && _4.equals("O") && _7.equals("O")){
            Stop_Timer();
            Disable_Game_Move();
            isGameWin = true;
            Show_Win_Line(1);
            Win_Dialog(true, player_Two_Name.getText().toString()+" Wins!");
            Zero_Win_Count();
        }

        else if(_1.equals("O") && _3.equals("O") && _5.equals("O") && _7.equals("O") && _9.equals("O")){

            Stop_Timer();
            Disable_Game_Move();
            isGameWin = true;
            switch (new Random().nextInt(2)+1){
                case 1:
                    Show_Win_Line(8);
                    break;

                case 2:
                    Show_Win_Line(7);
                    break;
            }
            Win_Dialog(true, player_Two_Name.getText().toString()+" Wins!");
            Zero_Win_Count();
        }

        else if(_1.equals("O") && _5.equals("O") && _9.equals("O")){
            Stop_Timer();
            Disable_Game_Move();
            isGameWin = true;
            Show_Win_Line(8);
            Win_Dialog(true, player_Two_Name.getText().toString()+" Wins!");
            Zero_Win_Count();
        }

        else if(_2.equals("O") && _4.equals("O") && _5.equals("O") && _6.equals("O") && _8.equals("O") ){
            Win_Dialog(true, player_Two_Name.getText().toString()+" Wins!");
            isGameWin = true;
            switch (new Random().nextInt(2)+1){
                case 1:
                    Show_Win_Line(5);
                    break;

                case 2:
                    Show_Win_Line(6);
                    break;
            }
            Cross_Win_Count();
            Disable_Game_Move();
            Stop_Timer();
        }

        else if(_2.equals("O") && _5.equals("O") && _8.equals("O")){
            Stop_Timer();
            Disable_Game_Move();
            isGameWin = true;
            Show_Win_Line(5);
            Win_Dialog(true, player_Two_Name.getText().toString()+" Wins!");
            Zero_Win_Count();
        }

        else if(_3.equals("O") && _6.equals("O") && _9.equals("O")){
            Stop_Timer();
            Disable_Game_Move();
            isGameWin = true;
            Show_Win_Line(3);
            Win_Dialog(true, player_Two_Name.getText().toString()+" Wins!");
            Zero_Win_Count();
        }

        else if(_3.equals("O") && _5.equals("O") && _7.equals("O")){
            Stop_Timer();
            Disable_Game_Move();
            isGameWin = true;
            Show_Win_Line(7);
            Win_Dialog(true, player_Two_Name.getText().toString()+" Wins!");
            Zero_Win_Count();
        }

        else if(_4.equals("O") && _5.equals("O") && _6.equals("O")){
            Stop_Timer();
            Disable_Game_Move();
            isGameWin = true;
            Show_Win_Line(6);
            Win_Dialog(true, player_Two_Name.getText().toString()+" Wins!");
            Zero_Win_Count();
        }

        else if(_7.equals("O") && _8.equals("O") && _9.equals("O")){
            Stop_Timer();
            Disable_Game_Move();
            isGameWin = true;
            Show_Win_Line(4);
            Win_Dialog(true, player_Two_Name.getText().toString()+" Wins!");
            Zero_Win_Count();
        }

        else if(!_1.isEmpty() && !_2.isEmpty() && !_3.isEmpty() && !_4.isEmpty() && !_5.isEmpty() && !_6.isEmpty() && !_7.isEmpty() && !_8.isEmpty() && !_9.isEmpty() && !isGameWin){
            Stop_Timer();
            Disable_Game_Move();
            isGameWin = false;
            isGameDraw = true;
            Win_Dialog(false, getResources().getString(R.string.draw_msg));
        }
    }

    private void AutoSet_Cross_Zero(){
        if(!isGameWin && !isGameDraw){
            Start_Timer();
            switch (Generate_Move_Rules()){
                case 1:
                    if(cell_1.getText().toString().isEmpty()){
                        Check_Cross_Zero(cell_1);
                    }
                    else {
                        AutoSet_Cross_Zero();
                    }
                    break;

                case 2:
                    if(cell_2.getText().toString().isEmpty()){
                        Check_Cross_Zero(cell_2);
                    }
                    else {
                        AutoSet_Cross_Zero();
                    }
                    break;

                case 3:
                    if(cell_3.getText().toString().isEmpty()){
                        Check_Cross_Zero(cell_3);
                    }
                    else {
                        AutoSet_Cross_Zero();
                    }
                    break;

                case 4:
                    if(cell_4.getText().toString().isEmpty()){
                        Check_Cross_Zero(cell_4);
                    }
                    else {
                        AutoSet_Cross_Zero();
                    }
                    break;

                case 5:
                    if(cell_5.getText().toString().isEmpty()){
                        Check_Cross_Zero(cell_5);
                    }
                    else {
                        AutoSet_Cross_Zero();
                    }
                    break;

                case 6:
                    if(cell_6.getText().toString().isEmpty()){
                        Check_Cross_Zero(cell_6);
                    }
                    else {
                        AutoSet_Cross_Zero();
                    }
                    break;

                case 7:
                    if(cell_7.getText().toString().isEmpty()){
                        Check_Cross_Zero(cell_7);
                    }
                    else {
                        AutoSet_Cross_Zero();
                    }
                    break;

                case 8:
                    if(cell_8.getText().toString().isEmpty()){
                        Check_Cross_Zero(cell_8);
                    }
                    else {
                        AutoSet_Cross_Zero();
                    }
                    break;

                case 9:
                    if(cell_9.getText().toString().isEmpty()){
                        Check_Cross_Zero(cell_9);
                    }
                    else {
                        AutoSet_Cross_Zero();
                    }
                    break;

                default:
                    Restart_Game();
            }
        }
    }

    private void Start_Timer() {
        if(!isOnClassicMode){
           Move_Timer.start();
        }
    }

    private void Stop_Timer() {
        if(!isOnClassicMode){
            isTimerOn = false;
            Cross_Timer_Progress.setProgress(15);
            Zero_Timer_Progress.setProgress(15);
            Cross_Timer_Progress.setVisibility(View.INVISIBLE);
            Zero_Timer_Progress.setVisibility(View.INVISIBLE);
            Move_Timer.cancel();
        }
    }

    private void Cross_Win_Count() {
        int count = Integer.parseInt(cross_Win_Tv.getText().toString());
        cross_Win_Tv.setText((count+1)+"");
    }

    private void Zero_Win_Count() {
        int count = Integer.parseInt(zero_Win_Tv.getText().toString());
        zero_Win_Tv.setText((count+1)+"");
    }

    private void Show_Win_Line(int position){

        switch (position){
            case 1:
                view_Line_Left.setVisibility(View.VISIBLE);
                Common.Bounce_Animation(getApplicationContext(), view_Line_Left, 0.1, 10);
                Measure_Line_Size(view_Line_Left, "Vertical", 4);
                Measure_Line_Position(view_Line_Left, "Left");
                break;

            case 2:
                view_Line_Top.setVisibility(View.VISIBLE);
                Common.Bounce_Animation(getApplicationContext(), view_Line_Top, 0.1, 10);
                Measure_Line_Size(view_Line_Top, "Horizontal", 4);
                Measure_Line_Position(view_Line_Top, "Top");
                break;

            case 3:
                view_Line_Right.setVisibility(View.VISIBLE);
                Common.Bounce_Animation(getApplicationContext(), view_Line_Right, 0.1, 10);
                Measure_Line_Size(view_Line_Right, "Vertical", 4);
                Measure_Line_Position(view_Line_Right, "Right");
                break;

            case 4:
                view_Line_Bottom.setVisibility(View.VISIBLE);
                Common.Bounce_Animation(getApplicationContext(), view_Line_Bottom, 0.1, 10);
                Measure_Line_Size(view_Line_Bottom, "Horizontal", 4);
                Measure_Line_Position(view_Line_Bottom, "Bottom");
                break;

            case 5:
                view_Line_Vertical.setVisibility(View.VISIBLE);
                Common.Bounce_Animation(getApplicationContext(), view_Line_Vertical, 0.1, 10);
                Measure_Line_Size(view_Line_Vertical, "Vertical", 4);
                break;

            case 6:
                view_Line_Horizontal.setVisibility(View.VISIBLE);
                Common.Bounce_Animation(getApplicationContext(), view_Line_Horizontal, 0.1, 10);
                Measure_Line_Size(view_Line_Horizontal, "Horizontal", 4);
                break;

            case 7:
                view_Line_Front.setVisibility(View.VISIBLE);
                Common.Bounce_Animation(getApplicationContext(), view_Line_Front, 0.1, 10);
                Measure_Line_Size(view_Line_Front, "Vertical", 8);
                break;

            case 8:
                view_Line_Back.setVisibility(View.VISIBLE);
                Common.Bounce_Animation(getApplicationContext(), view_Line_Back, 0.1, 10);
                Measure_Line_Size(view_Line_Back, "Vertical", 8);
                break;
        }

    }

    private void Measure_Line_Size(View view, String orientation, int value){
        try {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view.getLayoutParams();
            int size = displayMetrics.widthPixels;

            switch (orientation){
                case "Vertical":
                    params.height = size - (size/value);
                    break;

                case "Horizontal":
                    params.width = size - (size/value);
                    break;
            }

            view.setLayoutParams(params);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void Measure_Line_Position(final View view, final String position){

        cell_1.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener(){
            @Override public void onGlobalLayout(){

                cell_1.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                double margin = (cell_1.getMeasuredWidth()/1.70);
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view.getLayoutParams();

                switch (position){
                    case "Left":
                        params.setMargins((int)margin,0,0,0);
                        break;

                    case "Top":
                        params.setMargins(0, (int)margin,0,0);
                        break;

                    case "Right":
                        params.setMargins(0,0,(int)margin,0);
                        break;

                    case "Bottom":
                        params.setMargins(0,0,0,(int)margin);
                        break;
                }

                view.setLayoutParams(params);
            }
        });
    }

    private void Win_Dialog(boolean gameWin, String message){

        final View view = View.inflate(getApplicationContext(),R.layout.win_layout, null);

        if( alertDialog != null && alertDialog.isShowing() ) return;

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(view);
        alertDialogBuilder.setCancelable(true);

        alertDialog = alertDialogBuilder.create();
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(alertDialog.getWindow()).getDecorView().setBackgroundColor(Color.TRANSPARENT);
        alertDialog.show();

        Common.Bounce_Animation(getApplicationContext(), view, 0.15, 5);

        LinearLayout dialog_Layout = view.findViewById(R.id.dialog_layout);
        TextView draw_Icon = view.findViewById(R.id.draw_icon);
        RelativeLayout star_Layout = view.findViewById(R.id.star_layout);
        ImageButton star_One = view.findViewById(R.id.star_one);
        ImageButton star_Two = view.findViewById(R.id.star_two);
        ImageButton star_Three = view.findViewById(R.id.star_three);
        TextView game_Message = view.findViewById(R.id.message);
        Button Share = view.findViewById(R.id.share);
        Button new_Game = view.findViewById(R.id.new_game);

        /* Set Font */
        Common.Set_Font(getApplicationContext(), new View[]{draw_Icon, game_Message, Share, new_Game});

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

        game_Message.setText(message);

        if(gameWin){
            Common.Sound(getApplicationContext(), "Win");
            star_Layout.setVisibility(View.VISIBLE);

            for(ImageButton Star: new ImageButton[]{star_One, star_Two, star_Three}){
                Common.Bounce_Animation(getApplicationContext(), Star, 0.15, 5);
            }
        }
        else {
            Common.Sound(getApplicationContext(), "Draw");
            draw_Icon.setVisibility(View.VISIBLE);
        }

        Share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Common.Bounce_Animation(getApplicationContext(), v, 0.1, 10);
                Common.Sound(getApplicationContext(), "Click");
                Common.Vibration(getApplicationContext());

                if(Common.Is_Internet_Connected(getApplicationContext())){
                    if(Is_Permission_Granted(333)){
                        Share();
                    }
                }
                else {
                    Common.Custom_SnackBar(getApplicationContext(), view.findViewById(R.id.root_layout), getResources().getString(R.string.network_Error));
                }

            }
        });

        new_Game.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Common.Bounce_Animation(getApplicationContext(), v, 0.1, 10);
                Common.Sound(getApplicationContext(), "Click");
                Common.Vibration(getApplicationContext());
                Restart_Game();
                alertDialog.dismiss();
            }
        });
    }

    private void Enable_Game_Move() {

        for (TextView cell: new TextView[]{cell_1, cell_2, cell_3, cell_4, cell_5, cell_6, cell_7, cell_8, cell_9}){
            cell.setEnabled(true);
        }
    }

    private void Disable_Game_Move() {
        for (TextView cell: new TextView[]{cell_1, cell_2, cell_3, cell_4, cell_5, cell_6, cell_7, cell_8, cell_9}){
            cell.setEnabled(false);
        }
    }

    public void Play_Again(View view) {
        Restart_Game();
    }

    private void Restart_Game() {

        Stop_Timer();

        isGameWin = false;
        isGameDraw = false;

        for (View line_View: new View[]{view_Line_Left, view_Line_Top, view_Line_Right, view_Line_Bottom, view_Line_Horizontal, view_Line_Vertical, view_Line_Front, view_Line_Back}){
            line_View.setVisibility(View.INVISIBLE);
        }

        for (TextView cell : new TextView[]{cell_1, cell_2, cell_3, cell_4, cell_5, cell_6, cell_7, cell_8, cell_9}){
            cell.setText(null);
        }

        Enable_Game_Move();
        Load_First_Move();
    }

    @Override protected void onPause() {
        super.onPause();

        screenOff = true;

        if(isTimerOn){
            Stop_Timer();
        }
        Common.Set_Is_Game_Resumed(getApplicationContext(),true);
        Resume_Game();
    }

    @Override protected void onResume() {
        super.onResume();

        if(screenOff) {
            if (Common.Get_Is_Game_Resumed(getApplicationContext()) && !Common.Get_Resumed_Data(getApplicationContext()).get("SingleOrDouble").isEmpty()) {
                Reload_Game();
                screenOff = false;
            }
        }
    }

    @Override public void onBackPressed() {
        if(isTimerOn){
            Stop_Timer();
        }
        Common.Sound(getApplicationContext(), "Click");
        Common.Vibration(getApplicationContext());
        Common.Set_Is_Game_Resumed(getApplicationContext(),true);
        Resume_Game();
        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    public void Return_Home(View view) {
        if(isTimerOn){
            Stop_Timer();
        }
        Common.Sound(getApplicationContext(), "Click");
        Common.Vibration(getApplicationContext());
        Common.Set_Is_Game_Resumed(getApplicationContext(),true);
        Resume_Game();
        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    public void Return_Settings(View view) {
        if(isTimerOn){
            Stop_Timer();
        }
        Common.Sound(getApplicationContext(), "Click");
        Common.Vibration(getApplicationContext());
        Common.Set_Is_Game_Resumed(getApplicationContext(),true);
        Resume_Game();
        Common.Vibration(getApplicationContext());
        Intent intent = new Intent(getApplicationContext(),SettingsActivity.class);
        intent.putExtra("fromDoublePlayer",true);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void Resume_Game(){

        if(isCrossTurn){
            Common.Set_Resume_Data(getApplicationContext(),"FirstMove", "X");
        }
        else {
            Common.Set_Resume_Data(getApplicationContext(),"FirstMove", "O");
        }
        Common.Set_Resume_Data(getApplicationContext(),"CrossWinCount", cross_Win_Tv.getText().toString()+"");
        Common.Set_Resume_Data(getApplicationContext(),"ZeroWinCount", zero_Win_Tv.getText().toString()+"");
        Common.Set_Resume_Data(getApplicationContext(),"SingleOrDouble", "Double");

        TextView[] cells = {cell_1, cell_2, cell_3, cell_4, cell_5, cell_6, cell_7, cell_8, cell_9};

        for (int i=0; i<cells.length; i++){
            Common.Set_Resume_Data(getApplicationContext(),String.valueOf(i+1), cells[i].getText().toString());
        }

    }

    private void Reload_Game(){

        TextView[] cells = {cell_1, cell_2, cell_3, cell_4, cell_5, cell_6, cell_7, cell_8, cell_9};

        for (int i=0; i<cells.length; i++){
            cells[i].setText(Common.Get_Resumed_Data(getApplicationContext()).get(String.valueOf(i+1)).replaceAll("null",""));
        }
        for (TextView cell : new TextView[]{cell_1, cell_2, cell_3, cell_4, cell_5, cell_6, cell_7, cell_8, cell_9}) {
            Set_Text_Style(cell, cell.getText().toString());
        }

        Match_Zero();
        Match_Cross();

        if(!isGameWin && !isGameDraw){
            switch(Common.Get_Resumed_Data(getApplicationContext()).get("FirstMove")){
                case "X":
                    isCrossTurn = true;
                    Common.Custom_Toast(getApplicationContext(), player_One_Name.getText().toString()+", it's your turn.");
                    Start_Timer();
                    break;

                case "O":
                    isCrossTurn = false;
                    Common.Custom_Toast(getApplicationContext(), player_Two_Name.getText().toString()+", it's your turn.");
                    Start_Timer();
                    break;
            }
        }

        cross_Win_Tv.setText(Common.Get_Resumed_Data(getApplicationContext()).get("CrossWinCount"));
        zero_Win_Tv.setText(Common.Get_Resumed_Data(getApplicationContext()).get("ZeroWinCount"));
    }

    private void Share(){

        @SuppressLint("DefaultLocale") String path = Environment.getExternalStorageDirectory().toString() + "/" + String.format("%d.png", System.currentTimeMillis());

        OutputStream outputStream = null;
        File imageFile = new File(path);

        try {
            outputStream = new FileOutputStream(imageFile);
            Take_Screenshot(Content_Layout).compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            outputStream.flush();
        }
        catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
        finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        Intent intent = new Intent(Intent.ACTION_SEND);
        /*intent.putExtra(Intent.EXTRA_TEXT, "Hey view/download this image");*/
        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(imageFile));
        intent.setType("image/*");
        startActivity(intent);
    }

    @Override public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {

            case 333:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(Common.Is_Internet_Connected(getApplicationContext())){
                        Share();
                    }
                    else {
                        Common.Custom_SnackBar(getApplicationContext(), findViewById(R.id.root_layout), getResources().getString(R.string.network_Error));
                    }
                }
                break;
        }
    }

    public  boolean Is_Permission_Granted(int requestCode) {
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

    public static Bitmap Take_Screenshot(View view) {

        int widthSpec = View.MeasureSpec.makeMeasureSpec(view.getMeasuredWidth(), View.MeasureSpec.EXACTLY);

        int heightSpec = View.MeasureSpec.makeMeasureSpec(view.getMeasuredHeight(), View.MeasureSpec.EXACTLY);

        view.measure(widthSpec, heightSpec);

        /* set layout sizes */
        view.layout(view.getLeft(), view.getTop(), view.getMeasuredWidth() + view.getLeft(), view.getMeasuredHeight() + view.getTop());

        /* create the bitmap */
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);

        /* create a canvas used to get the view's image and draw it on the bitmap */
        Canvas c = new Canvas(bitmap);

        /* position the image inside the canvas */
        c.translate(-view.getScrollX(), -view.getScrollY());

        view.draw(c);

        return bitmap;
    }
}
