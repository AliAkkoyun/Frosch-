package com.aliakkoyun.froggerapp;

import static com.aliakkoyun.froggerapp.GameView.screenSizeX;
import static com.aliakkoyun.froggerapp.GameView.screenSizeY;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

public class Car2 {
    public int speed = 30;
    int x=0,y, width, height, carCaunter=1;
    Bitmap car1,car2,car3;

    Car2(Resources res) {
        car1 = BitmapFactory.decodeResource(res, R.drawable.car2_1);
        car2 = BitmapFactory.decodeResource(res, R.drawable.car2_2);
        car3 = BitmapFactory.decodeResource(res, R.drawable.car2_3);

        width = car1.getWidth();
        height = car1.getHeight();

        width /=7;
        height /=7;

        width = (int) (width * screenSizeX);
        height = (int) (height * screenSizeY);

        car1 = Bitmap.createScaledBitmap(car1,width,height,false);
        car2 = Bitmap.createScaledBitmap(car2,width,height,false);
        car3 = Bitmap.createScaledBitmap(car3,width,height,false);

        y= -height;
    }
    Bitmap getCar(){
        if(carCaunter == 1){
            carCaunter++;
            return car1;
        }
        if(carCaunter == 2){
            carCaunter++;
            return car2;
        }
        carCaunter = 1;
        return car3;
    }

    Rect getCollisionControl(){
        return new Rect(x,y,x+width,y+height);
    }
}
