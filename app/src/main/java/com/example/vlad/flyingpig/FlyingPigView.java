package com.example.vlad.flyingpig;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public class FlyingPigView extends View {

    private Bitmap pig[] = new Bitmap[2];
    private int pigX = 10;
    private int pigY;
    private int pigSpeed;
    private int canvasWidth , canvasHeight;

    private int yellowX , yellowY , yellowSpeed=16;
    private Paint yellowPaint = new Paint();

    private int greenX , greenY , greenSpeed=20;
    private Paint greenPaint = new Paint();

    private int redX , redY , redSpeed=25;
    private Paint redPaint = new Paint();

    private int score , lifeCounterOfPig;

    private boolean touch = false;

    private Bitmap backgroundImage;
    private Paint scorePaint = new Paint();
    private Bitmap Life[] = new Bitmap [2];


    public FlyingPigView(Context context) {
        super(context);
        pig[0] = BitmapFactory.decodeResource(getResources(),R.drawable.pig1);
        pig[1] = BitmapFactory.decodeResource(getResources(),R.drawable.pig2);

        backgroundImage = BitmapFactory.decodeResource(getResources(),R.drawable.background);

        yellowPaint.setColor(Color.YELLOW);
        yellowPaint.setAntiAlias(false);

        greenPaint.setColor(Color.GREEN);
        greenPaint.setAntiAlias(false);

        redPaint.setColor(Color.RED);
        redPaint.setAntiAlias(false);

        scorePaint.setColor(Color.WHITE);
        scorePaint.setTextSize(70);
        scorePaint.setTypeface(Typeface.DEFAULT_BOLD);
        scorePaint.setAntiAlias(true);

        Life[0] = BitmapFactory.decodeResource(getResources(),R.drawable.hearts);
        Life[1] = BitmapFactory.decodeResource(getResources(),R.drawable.heart_grey);

        pigY = 550;
        score = 0;
        lifeCounterOfPig = 3;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvasWidth = canvas.getWidth();
        canvasHeight = canvas.getHeight();

        canvas.drawBitmap(backgroundImage,0,0,null);


        int minPigY = pig[0].getHeight();
        int maxPigY = canvasHeight - pig[0].getHeight() * 3;
        pigY = pigY + pigSpeed;

        if ( pigY <minPigY) {
            pigY = minPigY;
        }
        if (pigY > maxPigY ) {
            pigY = maxPigY;
        }
        pigSpeed = pigSpeed + 2;

        if (touch) {
            canvas.drawBitmap(pig[1] , pigX , pigY ,null);
            touch = false;
        }
        else {
            canvas.drawBitmap(pig[0] , pigX , pigY ,null);
        }

        yellowX = yellowX - yellowSpeed;

        if (hitBallChecker(yellowX,yellowY)) {

            score = score + 10;
            yellowX = -100;

        }

        if(yellowX < 0) {
            yellowX = canvasWidth + 21;
            yellowY = (int) Math.floor(Math.random() * (maxPigY-minPigY)+minPigY);
        }
        canvas.drawCircle(yellowX,yellowY,30,yellowPaint);

        greenX = greenX - greenSpeed;

        if (hitBallChecker(greenX,greenY)) {

            score = score + 20;
            greenX = -100;

        }

        if(greenX < 0) {
            greenX = canvasWidth + 21;
            greenY = (int) Math.floor(Math.random() * (maxPigY-minPigY)+minPigY);
        }
        canvas.drawCircle(greenX,greenY,30,greenPaint);


        redX = redX - redSpeed;

        if (hitBallChecker(redX,redY)) {

            redX = -100;
            lifeCounterOfPig--;


            if(lifeCounterOfPig == 0) {
                Toast.makeText(getContext(),"GAME OVER",Toast.LENGTH_SHORT).show();

                Intent gameOverIntent = new Intent(getContext(),GameOverActivity.class);
                gameOverIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                gameOverIntent.putExtra("score",score);
                getContext().startActivity(gameOverIntent);
            }
       }
        if(redX < 0) {
            redX = canvasWidth + 21;
            redY = (int) Math.floor(Math.random() * (maxPigY-minPigY)+minPigY);
        }
        canvas.drawCircle(redX,redY,35,redPaint);

        canvas.drawText(" Score : " + score,20 , 60 , scorePaint);

        for(int i=0;i<3;i++){
            int x = (int) (580 + Life[0].getWidth() * 1.5 * i);
            int y = 30;

            if (i <lifeCounterOfPig) {

                canvas.drawBitmap(Life[0],x,y,null);
            }
            else {
                canvas.drawBitmap(Life[1],x,y,null);
            }
        }
    }

    public boolean hitBallChecker (int x , int y) {
        if(pigX <x && x<(pigX + pig[0].getWidth()) && pigY <y && y <(pigY+pig[0].getHeight())) {
            return true;
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            touch = true;
            pigSpeed = -22;
        }
        return true;
    }
}
