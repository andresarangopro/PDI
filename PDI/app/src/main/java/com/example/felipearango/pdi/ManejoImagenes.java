package com.example.felipearango.pdi;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.ParcelFileDescriptor;

import java.io.FileDescriptor;
import java.io.IOException;

/**
 * Created by Felipe Arango on 26/04/2017.
 */

public class ManejoImagenes {



    /***********************************
     *Metodo que convierte un bitmap
     * en matriz de colores
     *
     * @param bmp
     * @return
     ***********************************/
    public int[][][] bitToMat(Bitmap bmp) {
        int picw = bmp.getWidth(); int pich = bmp.getHeight();
        int[] pix = new int[picw * pich];
        bmp.getPixels(pix, 0, picw, 0, 0, picw, pich);
        int matriz[][][] = new int[picw][pich][4];
        for (int y = 0; y < pich; y++)
            for (int x = 0; x < picw; x++)
            {
                int index = y * picw + x;
                matriz[x][y][0] = (pix[index] >> 24) & 0xff;
                matriz[x][y][1] = (pix[index] >> 16) & 0xff;
                matriz[x][y][2] = (pix[index] >> 8) & 0xff;
                matriz[x][y][3] = pix[index] & 0xff;
            }
        return matriz;
    }

    /***********************************
     *Metodo que pasa a escala de gris
     * una imagen
     * @param mat
     * @param w
     * @param h
     * @return
     ************************************/
    public int[][][] grayScale(int mat[][][], int w, int h)
    {
        int matriz[][][] = new int[w][h][4];
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int r = mat[x][y][1];
                int g = mat[x][y][2];
                int b = mat[x][y][3];
                int R = (int) (0.299 * r + 0.587 * g + 0.114 * b);
                matriz[x][y][1] = R;
                matriz[x][y][2] = R;
                matriz[x][y][3] = R;
            }
        }
        return matriz;
    }

    /**************************************
     * Metodo que hace una imagen verde
     * de 100 x 100
     * @return
     ***************************************/
    public int[][][] makeImg(){
        int matriz[][][] = new int[100][100][4];
        for (int y = 0; y < 100; y++) {
            for (int x = 0; x < 100; x++) {
                int R = (int)0;
                matriz[x][y][1] = 0;
                matriz[x][y][2] = 150;
                matriz[x][y][3] = 0;
            }
        }
        return matriz;
    }

    /**
     *
     * @param mat
     * @param w
     * @param h
     * @return
     */
    public  int[][][] negativoImagen(int mat[][][], int w, int h){
        int matriz[][][] = new int[w][h][4];
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int r = mat[x][y][1];
                int g = mat[x][y][2];
                int b = mat[x][y][3];
                matriz[x][y][1] =255- r;
                matriz[x][y][2] =255- g;
                matriz[x][y][3] =255- b;
            }
        }
        return matriz;
    }

    /***************************************************
     *Metodo que cambia el brillo de una imagen
     * @param mat
     * @param w
     * @param h
     * @param aum
     * @return
     *************************************************/
    public  int[][][] brillo(int mat[][][], int w, int h, int aum){
        int matriz[][][] = new int[w][h][4];
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int red = mat[x][y][1];
                int green = mat[x][y][2];
                int blue = mat[x][y][3];

                red = red + aum;
                green = green + aum;
                blue = blue + aum;

                if (red < 0) {
                    red = 0;
                }else if (red > 255) {
                    red = 255;
                }
                if (blue < 0) {
                    blue = 0;
                }else  if (blue > 255) {
                    blue = 255;
                }
                if (green < 0) {
                    green = 0;
                }else if (green > 255) {
                    green = 255;
                }
                matriz[x][y][1] =red ;
                matriz[x][y][2] =green;
                matriz[x][y][3] =blue ;
            }
        }
        return matriz;
    }

    /***************************************
     *Metodo que convierte una matriz en
     * bitmap
     * @param mat
     * @param w
     * @param h
     * @return
     ************************************/
    public Bitmap matToBit(int mat[][][], int w, int h)
    {
        int[] pix = new int[w * h];
        for (int y = 0; y < h; y++)
            for (int x = 0; x < w; x++)
            {
                int index = y * w + x;
                pix[index] = 0xff000000 | (mat[x][y][1] << 16) | (mat[x][y][2] << 8) | mat[x][y][3];
            }
        Bitmap bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_4444);
        bm.setPixels(pix, 0, w, 0, 0, w, h);
        return bm;
    }
}
