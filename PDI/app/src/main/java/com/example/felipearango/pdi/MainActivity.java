package com.example.felipearango.pdi;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileDescriptor;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    //////////////////////////////
    //Variables
    /////////////////////////////

    private static final int PICK_IMAGE = 100;
    ManejoImagenes manImg = new ManejoImagenes();
    Uri imageUri;
    Bitmap imag;
    ImageView iVfotoGallery;
    TextView tVMedidas;
    Button btnOpen, btnEscalaG, btnNegativo;
    SeekBar seekBar;
    int picw ;
    int pich;
    int[][][] matriz;
    int[][][] bitTo;
    int[][][] scale;
    //////////////////////////////
    //Main
    /////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        instanciar();
        actionsButtons();
    }

    //////////////////////////////
    //Metodos
    /////////////////////////////

    /***************************************
     *Metodo que instancia los objetos de
     * xml a objetos java
     ***************************************/
    public void instanciar(){
        iVfotoGallery = (ImageView) findViewById(R.id.iVfotoGallery);
        btnOpen = (Button) findViewById(R.id.btnOpen);
        btnEscalaG = (Button) findViewById(R.id.btnEscalaG);
        btnNegativo = (Button) findViewById(R.id.btnNegativo);
        tVMedidas = (TextView) findViewById(R.id.tVMedidas);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
    }

    /***********************************
     *Metodo que inicializa
     * los metodos listener
     ***********************************/

    public void actionsButtons(){
        iVfotoGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //openGallery();
                
            }
        });

        btnOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        btnEscalaG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toScaleGray();
            }
        });

        btnNegativo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toNegative();
            }
        });


        /******************************
         *
         ******************************/

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int brihtness = 0;
                if(progress >= 255){
                    brihtness = progress-255;
                    brillo(brihtness);
                }else{
                    brihtness = 255 - progress;
                    brihtness = brihtness *-1;
                    brillo(brihtness);
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    /***********************************
     *Metodo que inicia el intent
     * galeria de imagenes
     ***********************************/

    private void openGallery(){
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }

    /***********************************
     *Metodo que imprime en el
     * TextView las medidas de la imagen
     ************************************/
    public void medidas(){
        picw =  imag.getWidth();
        pich =  imag.getHeight();
        tVMedidas.setText("ANCHO: "+picw+" LARGO: "+pich);
        //Toast.makeText(getApplicationContext(),picw+""+pich,Toast.LENGTH_LONG).show();
    }

    /***********************
     *
     ***********************/

    public void toScaleGray(){
        nuevoMatrizBitMat();
        scale = manImg.grayScale(bitTo,picw,pich);
        volverMatrizABit();
    }

    public void hacerImg(){
        scale = manImg.makeImg();
        picw = 100;
        pich = 100;
        tVMedidas.setText("ANCHO: "+picw+" LARGO: "+pich);
        volverMatrizABit();
    }

    /************************************
     *Metodo que crea una nueva matriz
     * y la asigna a imgView llama el
     * metodo que genera un negativo
     * de la imagen actual
     ************************************/

    public void toNegative(){
        nuevoMatrizBitMat();
        scale = manImg.negativoImagen(bitTo,picw,pich);
        volverMatrizABit();
    }


    /***********************************
     *Metodo que realiza el llamado
     * a los metodos que suben el brillo
     * y asignan la imagen al imgView
     ************************************/

    public void brillo(int progress){
        nuevoMatrizBitMat();
        scale = manImg.brillo(bitTo,picw,pich,progress);
        volverMatrizABit();
    }
    /***************************************
     *Metodo que genera una nueva matriz
     *****************************************/

    public void nuevoMatrizBitMat(){
        matriz = new int[picw][pich][4];
        bitTo = manImg.bitToMat(imag);
    }
    /*******************************************
     *Metodo que hace llamado
     * a el metodo que convierte
     * una matriz en bitmap y la asigna a un imgVIew
     ************************************************/

    public void volverMatrizABit(){
        Bitmap imgGray = manImg.matToBit(scale,picw,pich);
        iVfotoGallery.setImageBitmap(imgGray);
    }

    /******************************
     *Metodo que abre la galeria
     * de imagenes y asigna unn uri
     * a un imgView
     * @param requestCode
     * @param resultCode
     * @param data
     ******************************/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode == RESULT_OK && requestCode == PICK_IMAGE){
            imageUri = data.getData();
            try {
                imag = getBitmapFromUri(imageUri);
                iVfotoGallery.setImageBitmap(imag);
                medidas();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    /********************************
     *Metodo que convierte un uri
     * en bitmp
     * @param uri
     * @return
     * @throws IOException
     ******************************/
    public Bitmap getBitmapFromUri(Uri uri) throws IOException{
        ParcelFileDescriptor parcelFileDescriptor = getContentResolver().openFileDescriptor(uri,"r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return bitmap;
    }

}
