package com.moonturns.xoro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.app.ActionBar;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements ImageView.OnClickListener {

    private boolean won = false;
    private boolean darkTheme = false; //ui theme black or white

    private byte player1 = 1;
    private byte player2 = 2;

    private byte currentPlayer;

    private int[][] winnerColumnsRows = {{0, 1, 2}, {3, 4, 5}, {6, 7, 8}, {0, 3, 6}, {1, 4, 7}, {2, 5, 8}, {0, 4, 8}, {2, 4, 6}};
    private int[] winner = new int[9];
    private int tag = 0;
    private int playingCount = 0; //How many times players cliced imaged, the biggest value is 9

    private ConstraintLayout root;
    private GridLayout grid;
    private Button btnRestart;
    private ImageView imgTheme;
    private TextView txtWinner;

    //Get widgets
    private void crt() {
        grid = this.findViewById(R.id.grid);
        btnRestart = this.findViewById(R.id.btnRestart);
        imgTheme = this.findViewById(R.id.imgTheme);
        root = this.findViewById(R.id.root);
        txtWinner = this.findViewById(R.id.txtWinner);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        crt();
        fullScreen();
        setTheme();
        restarGame();
        currentPlayer = player1;
        setClick();
        setWinnerIndex();
    }

    @Override
    public void onClick(View v) {
        selectedItemTheme(v);
    }

    //Item's theme that selected
    private void selectedItemTheme(View v)
    {
        if (!won) {
            ImageView clickedImage = (ImageView) v;
            clickedImage.setScaleX(0);
            clickedImage.setScaleY(0);
            clickedImage.setEnabled(false); //when user click clickedImage, set enabled false for clickImage
            clickedImage.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent));
            tag = Integer.valueOf((String) clickedImage.getTag());
            winner[tag] = currentPlayer;

            for (int[] columns : winnerColumnsRows) {
                if (winner[columns[0]] != -1 && winner[columns[0]] == winner[columns[1]] && winner[columns[0]] == winner[columns[2]]) {
                    txtWinner.setTranslationY(-2000);
                    txtWinner.setVisibility(View.VISIBLE);
                    txtWinner.setText("PLAYER " + currentPlayer + " won");
                    txtWinner.animate().translationYBy(2000).setDuration(400);
                    won = true;
                    btnRestart.setVisibility(View.VISIBLE);
                }
            }
                if (currentPlayer == player1) {
                    clickedImage.setImageResource(R.drawable.x); //put picture to clickedImage
                    currentPlayer = player2; // which player is playing
                } else {
                    clickedImage.setImageResource(R.drawable.o); //put picture to clickedImage
                    currentPlayer = player1; // which player is playing
                }
                clickedImage.animate().scaleXBy(1).scaleYBy(1).alpha(1.0f).setDuration(800); //animation
                if (darkTheme)
                {
                    clickedImage.setBackgroundColor(ContextCompat.getColor(MainActivity.this, android.R.color.black));
                }else {
                    clickedImage.setBackgroundColor(ContextCompat.getColor(MainActivity.this, android.R.color.white));
                }

                playingCount++;

                if (playingCount == 9){
                    btnRestart.setVisibility(View.VISIBLE);
                }
        }
    }

    //According to darkTheme change game background theme
    private void gameBackgroundTheme()
    {
        if (darkTheme == false) {
            darkTheme = true;
            root.setBackgroundColor(ContextCompat.getColor(MainActivity.this, android.R.color.black));
            grid.setBackgroundColor(ContextCompat.getColor(MainActivity.this, android.R.color.white));
            imgTheme.setColorFilter(ContextCompat.getColor(MainActivity.this, android.R.color.white));
        } else {
            darkTheme = false;
            root.setBackgroundColor(ContextCompat.getColor(MainActivity.this, android.R.color.white));
            grid.setBackgroundColor(ContextCompat.getColor(MainActivity.this, android.R.color.black));
            imgTheme.setColorFilter(ContextCompat.getColor(MainActivity.this, android.R.color.black));
        }
    }

    //imageviewss' background color
    private void imageTheme()
    {
        if (darkTheme){
            for (int i = 0; i < grid.getChildCount(); i++) {
                ImageView img = (ImageView) grid.getChildAt(i);
                img.setBackgroundColor(ContextCompat.getColor(MainActivity.this, android.R.color.black));
            }
        }else {
            for (int i = 0; i < grid.getChildCount(); i++) {
                ImageView img = (ImageView) grid.getChildAt(i);
                img.setBackgroundColor(ContextCompat.getColor(MainActivity.this, android.R.color.white));
            }
        }
    }

    //Set ImageView for event click
    private void setClick()
    {
        for (int i = 0; i < grid.getChildCount(); i++) {
            ImageView imageView = (ImageView) grid.getChildAt(i);
            imageView.setOnClickListener(this);
        }
    }

    private void restarGame()
    {
        btnRestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                won = false;
                currentPlayer = player1;
                for (int i = 0; i < grid.getChildCount(); i++) {
                    ImageView img = (ImageView) grid.getChildAt(i);
                    img.setEnabled(true);
                    img.setImageDrawable(null);
                    img.setAlpha(0.2f);
                    winner[i] = -1;
                }
                imageTheme();
                txtWinner.setText("");
                txtWinner.setVisibility(View.GONE);
                btnRestart.setVisibility(View.GONE);
            }
        });
    }

    //imageTheme click event
    private void setTheme()
    {
        imgTheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameBackgroundTheme();
                imageTheme();
                buttonTheme();
                txtWinnerTheme();
            }
        });
    }

    //According to darkTheme change btnRestart's theme
    private void buttonTheme()
    {
        if (darkTheme){
            btnRestart.setBackgroundResource(R.drawable.button_shape_dark_theme);
        }else {
            btnRestart.setBackgroundResource(R.drawable.button_shape_white_theme);
        }
    }

    //According to darkTheme change txtWinner's theme
    private void txtWinnerTheme()
    {
        if (darkTheme) {
            txtWinner.setBackgroundResource(R.drawable.text_winner_dark_theme);
        }else {
            txtWinner.setBackgroundResource(R.drawable.text_winner_white_theme);
        }
    }

    //Hide window status bar and navigation bar
    private void fullScreen()
    {
        // Hide status bar
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    //Each index is made -1 as first value
    private void setWinnerIndex()
    {
        for (int i = 0; i < winner.length; i++)
            winner[i] = -1;
    }


}
