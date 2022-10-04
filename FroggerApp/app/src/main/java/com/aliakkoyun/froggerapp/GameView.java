package com.aliakkoyun.froggerapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.view.MotionEvent;
import android.view.SurfaceView;

import java.util.Random;

public class GameView extends SurfaceView implements Runnable{

    private Thread thread;
    private boolean isPlaying, gameOver = false;
    private int screenX, screenY, score =0;
    public static float screenSizeX, screenSizeY;
    private Background background1, background2;
    private Frog frog;
    private Car[] cars;
    private Car2[] cars2;
    private SoundPool soundPool;
    private int sound1;
    private int sound2;
    private int sound3;
    private int sound4;
    private SharedPreferences preferences;
    private Random random;
    private Paint paint;
    private static int speedFactor = 20;
    private static int frogSpeed = 15;
    private static int minSpeedFactor = 10;
    private static int counter = 0;
    private GameActivity2 activity;
    private int temp=0;

    public GameView(GameActivity2 activity, int screenX, int screenY) {
        super(activity);

        this.activity = activity;


        preferences = activity.getSharedPreferences("game",Context.MODE_PRIVATE);


        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .build();
            soundPool = new SoundPool.Builder().setAudioAttributes(audioAttributes).build();
        } else {
            soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC,0);
        }

        sound1 = soundPool.load(activity,R.raw.dead_and_start,1);
        sound2 = soundPool.load(activity,R.raw.arraving_up,1);
        sound3 = soundPool.load(activity,R.raw.go_up,1);
        sound4 = soundPool.load(activity,R.raw.go_left_and_right,1);


        this.screenX = screenX;
        this.screenY = screenY;
        screenSizeX = 1920f/ screenX;
        screenSizeY = 1080f/ screenY;

        background1 = new Background(screenX,screenY,getResources());
        background2 = new Background(screenX,screenY,getResources());

        //y eksenine göre obje yaratıldı
        frog = new Frog(screenX,getResources());

        background2.x = screenX;
        paint = new Paint();

        paint.setTextSize(128);
        paint.setColor(Color.WHITE);

        cars = new Car[3];
        cars2 = new Car2[3];

        //araba animasyonu için döngü kurdum
        for (int i=0; i< cars.length;i++){
            Car car = new Car(getResources());
            Car2 car2 = new Car2(getResources());
            cars2[i] = car2;
            cars[i] = car;
        }
        random = new Random();

    }

    @Override
    public void run() {
        while(isPlaying){

            update();
            draw();
            sleep();

        }
    }
    private void update(){
        background1.x -= 3 * screenSizeX;
        background2.x -= 3 * screenSizeX;

        if (background1.x + background1.background.getWidth() < 0){
            background1.x = screenX;
        }
        if (background2.x + background2.background.getWidth() < 0){
            background2.x = screenX;
        }
        //y ekseni ayarlandı
        // çarpım katsayısını ileride değiştirebilir yap!! örnek:( frog.y -=15*screenSizeY; ) bu kodu
        if(frog.goUp){

            int n;
            frog.y -=frogSpeed*screenSizeY; // çarpım katsayısı kurbağının y eksenindeki hızını arttırıyor.
            temp++;
            if(temp % 10 ==0 ){
               n = random.nextInt(8);
               score+=n;
               temp =0;
            }
        }
      /* else {
            frog.y += 30 * screenSizeY;
        }*/
        if(frog.goRight){
            frog.x +=frogSpeed*screenSizeX;
        }
        if(frog.goLeft){
            frog.x -= frogSpeed*screenSizeX;
        }

        if (frog.y < 0){
            frog.y = (int)(screenSizeY*screenY);
            speedSet();
            if(!preferences.getBoolean("quite",false)){
                soundPool.play(sound2,1,1,0,0,1);
            }
        }
        if (frog.x < 0) {
            frog.x = 0;

        }
        if(frog.x >= screenX - frog.width){
            frog.x = screenX - frog.width;

        }
        for(Car car : cars) {
            car.x -=car.speed;
            if(car.x + car.width < 0) {
                int randomCar = (int) (speedFactor * screenSizeX);
                car.speed = random.nextInt(randomCar);

                if(car.speed < minSpeedFactor*screenSizeX) car.speed = (int)(minSpeedFactor*screenSizeX);


                car.x = screenX;
                car.y = random.nextInt(screenY-car.height);
            }


            if(Rect.intersects(car.getCollisionControl(),frog.getCollisionControl())){
                gameOver = true;
                return;
            }
        }
        for(Car2 car : cars2) {
            car.x -=car.speed;
            if(car.x + car.width < 0) {
                int randomCar = (int) (speedFactor * screenSizeX);
                car.speed = random.nextInt(randomCar);

                if(car.speed < minSpeedFactor*screenSizeX) car.speed = (int)(minSpeedFactor*screenSizeX);


                car.x = screenX;
                car.y = random.nextInt(screenY-car.height);
            }


            if(Rect.intersects(car.getCollisionControl(),frog.getCollisionControl())){
                gameOver = true;
                return;
            }
        }

    }
    private void draw(){
        if(getHolder().getSurface().isValid()){
            Canvas canvas = getHolder().lockCanvas();
            canvas.drawBitmap(background1.background, background1.x, background1.y, paint);
            canvas.drawBitmap(background2.background, background2.x, background2.y, paint);

            for(Car car : cars){
                canvas.drawBitmap(car.getCar(),car.x,car.y,paint);

            }


            for(Car2 car : cars2){
                canvas.drawBitmap(car.getCar(),car.x,car.y,paint);

            }
            canvas.drawText(score + "", screenX/2f,164,paint);

            if(gameOver){

                isPlaying = false;
                canvas.drawBitmap(frog.getCrash(), frog.x,frog.y,paint);
                getHolder().unlockCanvasAndPost(canvas);
                saveHighScore();
                if(!preferences.getBoolean("quite",false)){
                    soundPool.play(sound1,1,1,0,0,1);
                }
                waitAndOut();
                return;
            }


            canvas.drawBitmap(frog.getFrog(),frog.x,frog.y,paint);
            getHolder().unlockCanvasAndPost(canvas);


        }

    }

    private void waitAndOut() {
        speedFactor = 20;
        frogSpeed = 15;
        minSpeedFactor = 10;
        try {
            Thread.sleep(2000);
            activity.startActivity(new Intent(activity,MainActivity.class));
            activity.finish();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void saveHighScore(){
        preferences.edit().putInt("highscore", 0).commit();
        if(preferences.getInt("highscore", 0) < score){
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt("highscore", score);
            editor.apply();
        }

    }
    private void sleep(){
        try {
            Thread.sleep(20);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void resume(){

        isPlaying = true;
        if(!preferences.getBoolean("quite",false)){
            soundPool.play(sound1,1,1,0,0,1);
        }
        thread = new Thread(this);
        thread.start();
    }
    public void pause(){

        try {
            isPlaying = false;
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void speedSet(){
        minSpeedFactor+=2;
        frogSpeed+=2;
        counter++;
        if(counter % 2 == 0){
            speedFactor += 5;
        }
    }

    //kurbağa için dokunma hareketleri
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int n;
    boolean up_touch = screenX/3 <= event.getX() && (screenX*2)/3 >= event.getX();
    boolean left_touch = screenX/3 > event.getX();
    boolean right_touch =  (screenX*2)/3 < event.getX();

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                if(up_touch) {
                    frog.goUp = true;
                    if(!preferences.getBoolean("quite",false)){
                        soundPool.play(sound3,1,1,0,0,1);
                    }

                }
                if(left_touch){
                    frog.goLeft = true;
                    if(!preferences.getBoolean("quite",false)){
                        soundPool.play(sound4,1,1,0,0,1);
                    }
                }
                if(right_touch){
                    frog.goRight = true;
                    if(!preferences.getBoolean("quite",false)){
                        soundPool.play(sound4,1,1,0,0,1);
                    }
                }

                break;
            case MotionEvent.ACTION_UP:
                frog.goUp = false;
                frog.goRight = false;
                frog.goLeft = false;


                break;
        }
        return true;
    }



}
