package com.rihansr.tic_tac_toe;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Objects;

@SuppressLint({"SetTextI18n","ClickableViewAccessibility"})
public class SettingsActivity extends AppCompatActivity {

    private AutoWritingText appName;
    private LinearLayout Center_Layout;
    private LinearLayout player_Two_Name_Layout;
    private TextView player_One_Name, player_Two_Name;
    private EditText player_One_etxt, player_Two_etxt;

    private HorizontalScrollView player_Two_Avatar_Layout;
    private ImageView p_One_Avatar_One, p_One_Avatar_Two, p_One_Avatar_Three, p_One_Avatar_Four, p_One_Avatar_Five, p_One_Avatar_Six, p_One_Avatar_Seven, p_One_Avatar_Eight;
    private ImageView p_Two_Avatar_One, p_Two_Avatar_Two, p_Two_Avatar_Three, p_Two_Avatar_Four, p_Two_Avatar_Five, p_Two_Avatar_Six, p_Two_Avatar_Seven, p_Two_Avatar_Eight;

    private TextView First_Move_Tv;
    private TextView move_Cross, move_Zero, move_Cross_Zero;

    private LinearLayout play_Mode_Layout;
    private TextView Game_Mode_Tv;
    private TextView mode_Classic, mode_Timer, mode_Easy, mode_Medium, mode_Hard;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Set_Id();

        View[] views = {appName, player_One_Name, player_One_etxt, player_Two_Name, player_Two_etxt, First_Move_Tv, move_Cross,
                move_Zero, move_Cross_Zero, Game_Mode_Tv, mode_Classic, mode_Timer, mode_Easy, mode_Medium, mode_Hard};
        Common.Set_Font(getApplicationContext(), views);

        Set_Layout_Size();

        Set_Name();

        Set_Avatar();

        Set_First_Move();

        Set_Game_Mode();

        Set_Play_Mode();

        appName.animateText(getResources().getString(R.string.app_name));
        appName.setCharacterDelay(180);
        appName.setShadowLayer(20f, 5.0f, 5.0f, Color.parseColor("#2F0400"));

        if (Objects.requireNonNull(getIntent().getExtras()).containsKey("toSinglePlayer")  || Objects.requireNonNull(getIntent().getExtras()).containsKey("fromSinglePlayer")) {
            player_One_Name.setText("Name");
            player_Two_Name_Layout.setVisibility(View.GONE);
            player_Two_Avatar_Layout.setVisibility(View.GONE);
            play_Mode_Layout.setVisibility(View.VISIBLE);
        }
        if (Objects.requireNonNull(getIntent().getExtras()).containsKey("toDoublePlayer")  || Objects.requireNonNull(getIntent().getExtras()).containsKey("fromDoublePlayer")) {
            player_One_Name.setText("Player One");
            player_Two_Name_Layout.setVisibility(View.VISIBLE);
            player_Two_Avatar_Layout.setVisibility(View.VISIBLE);
            play_Mode_Layout.setVisibility(View.GONE);
        }
    }

    private void Set_Id() {
        appName = findViewById(R.id.appName_id);
        Center_Layout = findViewById(R.id.center_layout);

        player_One_Name = findViewById(R.id.player_one_name);
        player_One_etxt = findViewById(R.id.player_one_etxt);
        p_One_Avatar_One = findViewById(R.id.p_one_avator_one);
        p_One_Avatar_Two = findViewById(R.id.p_one_avator_two);
        p_One_Avatar_Three = findViewById(R.id.p_one_avator_three);
        p_One_Avatar_Four = findViewById(R.id.p_one_avator_four);
        p_One_Avatar_Five = findViewById(R.id.p_one_avator_five);
        p_One_Avatar_Six = findViewById(R.id.p_one_avator_six);
        p_One_Avatar_Seven = findViewById(R.id.p_one_avator_seven);
        p_One_Avatar_Eight = findViewById(R.id.p_one_avator_eight);

        player_Two_Name_Layout = findViewById(R.id.player_two_name_layout);
        player_Two_Name = findViewById(R.id.player_two_name);
        player_Two_etxt = findViewById(R.id.player_two_etxt);
        player_Two_Avatar_Layout = findViewById(R.id.player_two_avatar_layout);
        p_Two_Avatar_One = findViewById(R.id.p_two_avator_one);
        p_Two_Avatar_Two = findViewById(R.id.p_two_avator_two);
        p_Two_Avatar_Three = findViewById(R.id.p_two_avator_three);
        p_Two_Avatar_Four = findViewById(R.id.p_two_avator_four);
        p_Two_Avatar_Five = findViewById(R.id.p_two_avator_five);
        p_Two_Avatar_Six = findViewById(R.id.p_two_avator_six);
        p_Two_Avatar_Seven = findViewById(R.id.p_two_avator_seven);
        p_Two_Avatar_Eight = findViewById(R.id.p_two_avator_eight);

        First_Move_Tv = findViewById(R.id.first_move_tv);
        move_Cross = findViewById(R.id.move_cross);
        move_Zero = findViewById(R.id.move_zero);
        move_Cross_Zero = findViewById(R.id.move_cross_zero);

        Game_Mode_Tv = findViewById(R.id.game_mode_tv);
        mode_Classic = findViewById(R.id.mode_classic);
        mode_Timer = findViewById(R.id.mode_timer);

        play_Mode_Layout = findViewById(R.id.play_mode_layout);
        mode_Easy = findViewById(R.id.mode_easy);
        mode_Medium = findViewById(R.id.mode_medium);
        mode_Hard = findViewById(R.id.mode_hard);
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


    private void Set_Name(){

        String NameOne = Common.Get_Settings_Data(getApplicationContext()).get(Common.PlayerOneName_Key);
        String NameTwo = Common.Get_Settings_Data(getApplicationContext()).get(Common.PlayerTwoName_Key);

        if(!NameOne.isEmpty()){
            player_One_etxt.setText(NameOne);
        }
        else {
            player_One_etxt.setText("Player One");
        }

        if(!NameTwo.isEmpty()){
            player_Two_etxt.setText(NameTwo);
        }
        else {
            player_Two_etxt.setText("Player Two");
        }

        player_One_etxt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                player_One_etxt.setText(null);
                return false;
            }
        });

        player_Two_etxt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                player_Two_etxt.setText(null);
                return false;
            }
        });

        player_One_etxt.addTextChangedListener(new TextWatcher() {

            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override public void afterTextChanged(Editable text) {
                Common.Set_Settings_Data(getApplicationContext(), Common.PlayerOneName_Key, text.toString());
            }
        });

        player_Two_etxt.addTextChangedListener(new TextWatcher() {

            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override public void afterTextChanged(Editable text) {
                Common.Set_Settings_Data(getApplicationContext(), Common.PlayerTwoName_Key, text.toString());
            }
        });

    }

    public void Choose_Avatar_One(View view) {

        for(ImageView avatar: new ImageView[]{p_One_Avatar_One, p_One_Avatar_Two, p_One_Avatar_Three, p_One_Avatar_Four,
                                             p_One_Avatar_Five, p_One_Avatar_Six, p_One_Avatar_Seven, p_One_Avatar_Eight}){

            avatar.setBackground(getResources().getDrawable(R.drawable.shape_avatar_unselect));
        }

        if(view instanceof ImageView){
            ImageView avatar = (ImageView) view;
            Common.Bounce_Animation(getApplicationContext(), view, 0.15, 5);
            Common.Sound(getApplicationContext(), "Move");
            Common.Vibration(getApplicationContext());
            avatar.setBackground(getResources().getDrawable(R.drawable.shape_avatar_select));
        }

        if(view.getId() == R.id.p_one_avator_one){
            Common.Set_Settings_Data(getApplicationContext(), Common.PlayerOneAvatar_Key,"AvatarOne");
        }

        if(view.getId() == R.id.p_one_avator_two){
            Common.Set_Settings_Data(getApplicationContext(), Common.PlayerOneAvatar_Key,"AvatarTwo");
        }

        if(view.getId() == R.id.p_one_avator_three){
            Common.Set_Settings_Data(getApplicationContext(), Common.PlayerOneAvatar_Key,"AvatarThree");
        }

        if(view.getId() == R.id.p_one_avator_four){
            Common.Set_Settings_Data(getApplicationContext(), Common.PlayerOneAvatar_Key,"AvatarFour");
        }

        if(view.getId() == R.id.p_one_avator_five){
            Common.Set_Settings_Data(getApplicationContext(), Common.PlayerOneAvatar_Key,"AvatarFive");
        }

        if(view.getId() == R.id.p_one_avator_six){
            Common.Set_Settings_Data(getApplicationContext(), Common.PlayerOneAvatar_Key,"AvatarSix");
        }

        if(view.getId() == R.id.p_one_avator_seven){
            Common.Set_Settings_Data(getApplicationContext(), Common.PlayerOneAvatar_Key,"AvatarSeven");
        }

        if(view.getId() == R.id.p_one_avator_eight){
            Common.Set_Settings_Data(getApplicationContext(), Common.PlayerOneAvatar_Key,"AvatarEight");
        }
    }

    public void Choose_Avatar_Two(View view) {
        for(ImageView avatar: new ImageView[]{p_Two_Avatar_One, p_Two_Avatar_Two, p_Two_Avatar_Three, p_Two_Avatar_Four,
                                         p_Two_Avatar_Five, p_Two_Avatar_Six, p_Two_Avatar_Seven, p_Two_Avatar_Eight}){

            avatar.setBackground(getResources().getDrawable(R.drawable.shape_avatar_unselect));
        }

        if(view instanceof ImageView){
            ImageView avatar = (ImageView) view;
            Common.Bounce_Animation(getApplicationContext(), view, 0.15, 5);
            Common.Sound(getApplicationContext(), "Move");
            Common.Vibration(getApplicationContext());
            avatar.setBackground(getResources().getDrawable(R.drawable.shape_avatar_select));
        }

        if(view.getId() == R.id.p_two_avator_one){
            Common.Set_Settings_Data(getApplicationContext(), Common.PlayerTwoAvatar_Key,"AvatarOne");
        }

        if(view.getId() == R.id.p_two_avator_two){
            Common.Set_Settings_Data(getApplicationContext(), Common.PlayerTwoAvatar_Key,"AvatarTwo");
        }

        if(view.getId() == R.id.p_two_avator_three){
            Common.Set_Settings_Data(getApplicationContext(), Common.PlayerTwoAvatar_Key,"AvatarThree");
        }

        if(view.getId() == R.id.p_two_avator_four){
            Common.Set_Settings_Data(getApplicationContext(), Common.PlayerTwoAvatar_Key,"AvatarFour");
        }

        if(view.getId() == R.id.p_two_avator_five){
            Common.Set_Settings_Data(getApplicationContext(), Common.PlayerTwoAvatar_Key,"AvatarFive");
        }

        if(view.getId() == R.id.p_two_avator_six){
            Common.Set_Settings_Data(getApplicationContext(), Common.PlayerTwoAvatar_Key,"AvatarSix");
        }

        if(view.getId() == R.id.p_two_avator_seven){
            Common.Set_Settings_Data(getApplicationContext(), Common.PlayerTwoAvatar_Key,"AvatarSeven");
        }

        if(view.getId() == R.id.p_two_avator_eight){
            Common.Set_Settings_Data(getApplicationContext(), Common.PlayerTwoAvatar_Key,"AvatarEight");
        }

    }

    private void Set_Avatar() {

        switch (Common.Get_Settings_Data(getApplicationContext()).get(Common.PlayerOneAvatar_Key)){

            case "AvatarOne":
                p_One_Avatar_One.setBackground(getResources().getDrawable(R.drawable.shape_avatar_select));
                break;

            case "AvatarTwo":
                p_One_Avatar_Two.setBackground(getResources().getDrawable(R.drawable.shape_avatar_select));
                break;

            case "AvatarThree":
                p_One_Avatar_Three.setBackground(getResources().getDrawable(R.drawable.shape_avatar_select));
                break;

            case "AvatarFour":
                p_One_Avatar_Four.setBackground(getResources().getDrawable(R.drawable.shape_avatar_select));
                break;

            case "AvatarFive":
                p_One_Avatar_Five.setBackground(getResources().getDrawable(R.drawable.shape_avatar_select));
                break;

            case "AvatarSix":
                p_One_Avatar_Six.setBackground(getResources().getDrawable(R.drawable.shape_avatar_select));
                break;

            case "AvatarSeven":
                p_One_Avatar_Seven.setBackground(getResources().getDrawable(R.drawable.shape_avatar_select));
                break;

            case "AvatarEight":
                p_One_Avatar_Eight.setBackground(getResources().getDrawable(R.drawable.shape_avatar_select));
                break;

            default:
                p_One_Avatar_One.setBackground(getResources().getDrawable(R.drawable.shape_avatar_select));

        }

        switch (Common.Get_Settings_Data(getApplicationContext()).get(Common.PlayerTwoAvatar_Key)){

            case "AvatarOne":
                p_Two_Avatar_One.setBackground(getResources().getDrawable(R.drawable.shape_avatar_select));
                break;

            case "AvatarTwo":
                p_Two_Avatar_Two.setBackground(getResources().getDrawable(R.drawable.shape_avatar_select));
                break;

            case "AvatarThree":
                p_Two_Avatar_Three.setBackground(getResources().getDrawable(R.drawable.shape_avatar_select));
                break;

            case "AvatarFour":
                p_Two_Avatar_Four.setBackground(getResources().getDrawable(R.drawable.shape_avatar_select));
                break;

            case "AvatarFive":
                p_Two_Avatar_Five.setBackground(getResources().getDrawable(R.drawable.shape_avatar_select));
                break;

            case "AvatarSix":
                p_Two_Avatar_Six.setBackground(getResources().getDrawable(R.drawable.shape_avatar_select));
                break;

            case "AvatarSeven":
                p_Two_Avatar_Seven.setBackground(getResources().getDrawable(R.drawable.shape_avatar_select));
                break;

            case "AvatarEight":
                p_Two_Avatar_Eight.setBackground(getResources().getDrawable(R.drawable.shape_avatar_select));
                break;

            default:
                p_Two_Avatar_Two.setBackground(getResources().getDrawable(R.drawable.shape_avatar_select));

        }

    }

    public void Choose_First_Move(View view){

        for(TextView CrossZero: new TextView[]{move_Cross, move_Zero, move_Cross_Zero}){
            CrossZero.setBackground(getResources().getDrawable(R.drawable.shape_button_uncheck));
            CrossZero.setTextColor(Color.WHITE);
        }

        if(view instanceof TextView){
            TextView button = (TextView) view;
            Common.Bounce_Animation(getApplicationContext(), view, 0.15, 5);
            Common.Sound(getApplicationContext(), "Move");
            Common.Vibration(getApplicationContext());
            button.setBackground(getResources().getDrawable(R.drawable.shape_button_check));
            button.setTextColor(Color.BLACK);
        }

        if(view.getId() == R.id.move_cross){
            Common.Set_Settings_Data(getApplicationContext(), Common.FirstMove_Key,"X");
        }
        if(view.getId() == R.id.move_zero){
            Common.Set_Settings_Data(getApplicationContext(), Common.FirstMove_Key,"O");
        }
        if(view.getId() == R.id.move_cross_zero){
            Common.Set_Settings_Data(getApplicationContext(), Common.FirstMove_Key,"XO");
        }
    }

    private void Set_First_Move(){

        switch (Common.Get_Settings_Data(getApplicationContext()).get(Common.FirstMove_Key)){

            case "X":
                move_Cross.setBackground(getResources().getDrawable(R.drawable.shape_button_check));
                move_Cross.setTextColor(Color.BLACK);
                break;

            case "O":
                move_Zero.setBackground(getResources().getDrawable(R.drawable.shape_button_check));
                move_Zero.setTextColor(Color.BLACK);
                break;

            case "XO":
                move_Cross_Zero.setBackground(getResources().getDrawable(R.drawable.shape_button_check));
                move_Cross_Zero.setTextColor(Color.BLACK);
                break;

            default:
                move_Cross_Zero.setBackground(getResources().getDrawable(R.drawable.shape_button_check));
                move_Cross_Zero.setTextColor(Color.BLACK);

        }
    }

    public void Choose_Game_Mode(View view){

        for(TextView Mode: new TextView[]{mode_Classic, mode_Timer}){
            Mode.setBackground(getResources().getDrawable(R.drawable.shape_button_uncheck));
            Mode.setTextColor(Color.WHITE);
        }

        if(view instanceof TextView){
            TextView button = (TextView) view;
            Common.Bounce_Animation(getApplicationContext(), view, 0.15, 5);
            Common.Sound(getApplicationContext(), "Move");
            Common.Vibration(getApplicationContext());
            button.setBackground(getResources().getDrawable(R.drawable.shape_button_check));
            button.setTextColor(Color.BLACK);
        }

        if(view.getId() == R.id.mode_classic){
            Common.Set_Settings_Data(getApplicationContext(), Common.GameMode_Key,"Classic");
        }
        if(view.getId() == R.id.mode_timer){
            Common.Set_Settings_Data(getApplicationContext(), Common.GameMode_Key,"Timer");
        }
    }

    private void Set_Game_Mode(){

        switch (Common.Get_Settings_Data(getApplicationContext()).get(Common.GameMode_Key)){

            case "Classic":
                mode_Classic.setBackground(getResources().getDrawable(R.drawable.shape_button_check));
                mode_Classic.setTextColor(Color.BLACK);
                break;

            case "Timer":
                mode_Timer.setBackground(getResources().getDrawable(R.drawable.shape_button_check));
                mode_Timer.setTextColor(Color.BLACK);
                break;

            default:
                mode_Timer.setBackground(getResources().getDrawable(R.drawable.shape_button_check));
                mode_Timer.setTextColor(Color.BLACK);
        }
    }

    public void Choose_Play_Mode(View view){

        for(TextView Mode: new TextView[]{mode_Easy, mode_Medium, mode_Hard}){
            Mode.setBackground(getResources().getDrawable(R.drawable.shape_button_uncheck));
            Mode.setTextColor(Color.WHITE);
        }

        if(view instanceof TextView){
            TextView button = (TextView) view;
            Common.Bounce_Animation(getApplicationContext(), view, 0.15, 5);
            Common.Sound(getApplicationContext(), "Move");
            Common.Vibration(getApplicationContext());
            button.setBackground(getResources().getDrawable(R.drawable.shape_button_check));
            button.setTextColor(Color.BLACK);
        }

        if(view.getId() == R.id.mode_easy){
            Common.Set_Settings_Data(getApplicationContext(), Common.PlayMode_Key,"Easy");
        }
        if(view.getId() == R.id.mode_medium){
            Common.Set_Settings_Data(getApplicationContext(), Common.PlayMode_Key,"Medium");
        }
        if(view.getId() == R.id.mode_hard){
            Common.Set_Settings_Data(getApplicationContext(), Common.PlayMode_Key,"Hard");
        }
    }

    private void Set_Play_Mode(){
        switch (Common.Get_Settings_Data(getApplicationContext()).get(Common.PlayMode_Key)){

            case "Easy":
                mode_Easy.setBackground(getResources().getDrawable(R.drawable.shape_button_check));
                mode_Easy.setTextColor(Color.BLACK);
                break;

            case "Medium":
                mode_Medium.setBackground(getResources().getDrawable(R.drawable.shape_button_check));
                mode_Medium.setTextColor(Color.BLACK);
                break;

            case "Hard":
                mode_Hard.setBackground(getResources().getDrawable(R.drawable.shape_button_check));
                mode_Hard.setTextColor(Color.BLACK);
                break;

            default:
                mode_Easy.setBackground(getResources().getDrawable(R.drawable.shape_button_check));
                mode_Easy.setTextColor(Color.BLACK);
        }
    }

    public void Start_Game(View view) {

        Common.Sound(getApplicationContext(), "Click");
        Common.Vibration(getApplicationContext());

        if (Objects.requireNonNull(getIntent().getExtras()).containsKey("toSinglePlayer") || Objects.requireNonNull(getIntent().getExtras()).containsKey("fromSinglePlayer")) {
            Intent intent = new Intent(getApplicationContext(), SinglePlayerMode.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }
        if (Objects.requireNonNull(getIntent().getExtras()).containsKey("toDoublePlayer") || Objects.requireNonNull(getIntent().getExtras()).containsKey("fromDoublePlayer")) {
            Intent intent = new Intent(getApplicationContext(), DoublePlayerMode.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }

    }

    public void Return_Home(View view) {
        Common.Sound(getApplicationContext(), "Click");
        Common.Vibration(getApplicationContext());
        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
