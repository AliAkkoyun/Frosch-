package com.aliakkoyun.froggerapp;

import static com.aliakkoyun.froggerapp.GameView.screenSizeX;
import static com.aliakkoyun.froggerapp.GameView.screenSizeY;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

public class Frog {
     boolean goUp = false;
     boolean goLeft = false;
     boolean goRight = false;

    int x, y;
    int width, height;
    Bitmap frog_normal, frog_up, frog_left, frog_right,frog_dead;

    Frog (int screenX, Resources res) {

        frog_normal = BitmapFactory.decodeResource(res, R.drawable.frog1);
        frog_up = BitmapFactory.decodeResource(res, R.drawable.frog_up);
        frog_left = BitmapFactory.decodeResource(res, R.drawable.frog_left);
        frog_right = BitmapFactory.decodeResource(res, R.drawable.frog_right);
        frog_dead = BitmapFactory.decodeResource(res, R.drawable.frog_dead);

        width = frog_normal.getWidth();
        height = frog_normal.getHeight();

        width /= 1.3;
        height /= 1.3;

        width = (int) (width * screenSizeX);
        height = (int) (height * screenSizeY);


        frog_normal = Bitmap.createScaledBitmap(frog_normal, width,height,false);
        frog_up = Bitmap.createScaledBitmap(frog_up, width,height,false);
        frog_right = Bitmap.createScaledBitmap(frog_right,width,height,false);
        frog_left = Bitmap.createScaledBitmap(frog_left,width,height,false);
        frog_dead = Bitmap.createScaledBitmap(frog_dead,width,height,false);

        //aşağıdaki kod bloğu kurbağanın konumunu ayarlıyor
        x = screenX / 2;
        // y ekseni diğer cihazlarda configrasyon sorunu verebilir aklında bulunsun!!
        y = (int)(screenSizeY*1050);
    }

    Bitmap getFrog(){

        if(goUp) return frog_up;
        if(goLeft) return frog_left;
        if(goRight) return frog_right;

        return frog_normal;
    }
    Rect getCollisionControl(){
        return new Rect(x,y,x+width,y+height);
    }
    Bitmap getCrash(){
        return frog_dead;
    }
}
