package com.azhar.pdfreport;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    EditText etName, etNoTlp, etJmlOne, etJmlTwo;
    Spinner itemSpinnerOne, itemSpinnerTwo;
    Button btnPrint;
    Bitmap bitmap, scaleBitmap;
    int pageWidth = 1200;
    Date dateTime;
    DateFormat dateFormat;
    float[] harga = new float[]{0, 21000, 22000, 25000, 22500, 21500};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etName = findViewById(R.id.etName);
        etNoTlp = findViewById(R.id.etNoTlp);
        etJmlOne = findViewById(R.id.etJmlOne);
        etJmlTwo = findViewById(R.id.etJmlTwo);
        itemSpinnerOne = findViewById(R.id.itemSpinnerOne);
        itemSpinnerTwo = findViewById(R.id.itemSpinnerTwo);
        btnPrint = findViewById(R.id.btnPrint);

        //cover header
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bg_cover);
        scaleBitmap = Bitmap.createScaledBitmap(bitmap, 1200, 518, false);

        //permission
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);

        createPDF();
    }

    private void createPDF() {
        btnPrint.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SimpleDateFormat")
            @Override
            public void onClick(View v) {

                dateTime = new Date();

                //get input
                if (etName.getText().toString().length() == 0 ||
                        etNoTlp.getText().toString().length() == 0 ||
                        etJmlOne.getText().toString().length() == 0 ||
                        etJmlTwo.getText().toString().length() == 0) {
                    Toast.makeText(MainActivity.this, "Data tidak boleh kosong!", Toast.LENGTH_LONG).show();
                } else {

                    PdfDocument pdfDocument = new PdfDocument();
                    Paint paint = new Paint();
                    Paint titlePaint = new Paint();

                    PdfDocument.PageInfo pageInfo
                            = new PdfDocument.PageInfo.Builder(1200, 2010, 1).create();
                    PdfDocument.Page page = pdfDocument.startPage(pageInfo);

                    Canvas canvas = page.getCanvas();
                    canvas.drawBitmap(scaleBitmap, 0, 0, paint);

                    paint.setColor(Color.WHITE);
                    paint.setTextSize(30f);
                    paint.setTextAlign(Paint.Align.RIGHT);
                    canvas.drawText("Berbagai macam jenis Kopi", 1160, 40, paint);
                    canvas.drawText("Pesan di : 08123456789", 1160, 80, paint);

                    titlePaint.setTextAlign(Paint.Align.CENTER);
                    titlePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                    titlePaint.setColor(Color.WHITE);
                    titlePaint.setTextSize(70);
                    canvas.drawText("Tagihan Anda", pageWidth / 2, 500, titlePaint);

                    paint.setTextAlign(Paint.Align.LEFT);
                    paint.setColor(Color.BLACK);
                    paint.setTextSize(35f);
                    canvas.drawText("Nama Pemesan: " + etName.getText(), 20, 590, paint);
                    canvas.drawText("Nomor Tlp: " + etNoTlp.getText(), 20, 640, paint);

                    paint.setTextAlign(Paint.Align.RIGHT);
                    canvas.drawText("No. Pesanan: " + "232425", pageWidth - 20, 590, paint);

                    dateFormat = new SimpleDateFormat("dd/MM/yy");
                    canvas.drawText("Tanggal: " + dateFormat.format(dateTime), pageWidth - 20, 640, paint);

                    dateFormat = new SimpleDateFormat("HH:mm:ss");
                    canvas.drawText("Pukul: " + dateFormat.format(dateTime), pageWidth - 20, 690, paint);

                    paint.setStyle(Paint.Style.STROKE);
                    paint.setStrokeWidth(2);
                    canvas.drawRect(20, 780, pageWidth - 20, 860, paint);

                    paint.setTextAlign(Paint.Align.LEFT);
                    paint.setStyle(Paint.Style.FILL);
                    canvas.drawText("No.", 40, 830, paint);
                    canvas.drawText("Menu Pesanan", 200, 830, paint);
                    canvas.drawText("Harga", 700, 830, paint);
                    canvas.drawText("Jumlah", 900, 830, paint);
                    canvas.drawText("Total", 1050, 830, paint);

                    canvas.drawLine(180, 790, 180, 840, paint);
                    canvas.drawLine(680, 790, 680, 840, paint);
                    canvas.drawLine(880, 790, 880, 840, paint);
                    canvas.drawLine(1030, 790, 1030, 840, paint);

                    float totalOne = 0, totalTwo = 0;
                    if (itemSpinnerOne.getSelectedItemPosition() != 0) {
                        canvas.drawText("1.", 40, 950, paint);
                        canvas.drawText(itemSpinnerOne.getSelectedItem().toString(), 200, 950, paint);
                        canvas.drawText(String.valueOf(harga[itemSpinnerOne.getSelectedItemPosition()]), 700, 950, paint);
                        canvas.drawText(etJmlOne.getText().toString(), 900, 950, paint);
                        totalOne = Float.parseFloat(etJmlOne.getText().toString()) * harga[itemSpinnerOne.getSelectedItemPosition()];
                        paint.setTextAlign(Paint.Align.RIGHT);
                        canvas.drawText(String.valueOf(totalOne), pageWidth - 40, 950, paint);
                        paint.setTextAlign(Paint.Align.LEFT);
                    }

                    if (itemSpinnerTwo.getSelectedItemPosition() != 0) {
                        canvas.drawText("2.", 40, 1050, paint);
                        canvas.drawText(itemSpinnerTwo.getSelectedItem().toString(), 200, 1050, paint);
                        canvas.drawText(String.valueOf(harga[itemSpinnerTwo.getSelectedItemPosition()]), 700, 1050, paint);
                        canvas.drawText(etJmlTwo.getText().toString(), 900, 1050, paint);
                        totalTwo = Float.parseFloat(etJmlTwo.getText().toString()) * harga[itemSpinnerTwo.getSelectedItemPosition()];
                        paint.setTextAlign(Paint.Align.RIGHT);
                        canvas.drawText(String.valueOf(totalTwo), pageWidth - 40, 1050, paint);
                        paint.setTextAlign(Paint.Align.LEFT);
                    }

                    float subTotal = totalOne + totalTwo;
                    canvas.drawLine(400, 1200, pageWidth - 20, 1200, paint);
                    canvas.drawText("Sub Total", 700, 1250, paint);
                    canvas.drawText(":", 900, 1250, paint);
                    paint.setTextAlign(Paint.Align.RIGHT);
                    canvas.drawText(String.valueOf(subTotal), pageWidth - 40, 1250, paint);

                    paint.setTextAlign(Paint.Align.LEFT);
                    canvas.drawText("PPN (10%)", 700, 1300, paint);
                    canvas.drawText(":", 900, 1300, paint);
                    paint.setTextAlign(Paint.Align.RIGHT);
                    canvas.drawText(String.valueOf(subTotal * 10 / 100), pageWidth - 40, 1300, paint);
                    paint.setTextAlign(Paint.Align.LEFT);

                    paint.setColor(Color.rgb(247, 147, 30));
                    canvas.drawRect(680, 1350, pageWidth - 20, 1450, paint);

                    paint.setColor(Color.BLACK);
                    paint.setTextSize(50f);
                    paint.setTextAlign(Paint.Align.LEFT);
                    canvas.drawText("Total", 700, 1415, paint);
                    paint.setTextAlign(Paint.Align.RIGHT);
                    canvas.drawText(String.valueOf(subTotal + (subTotal * 10 / 100)), pageWidth - 40, 1415, paint);

                    pdfDocument.finishPage(page);

                    File file = new File(Environment.getExternalStorageDirectory(), "/Pesanan.pdf");
                    try {
                        pdfDocument.writeTo(new FileOutputStream(file));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    pdfDocument.close();
                    Toast.makeText(MainActivity.this, "PDF sudah dibuat", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
