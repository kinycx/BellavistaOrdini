package com.bro.bellavistaordini;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.fonts.Font;
import android.os.Bundle;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.provider.DocumentsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.LineSeparator;
import com.itextpdf.layout.element.List;
import com.itextpdf.layout.element.ListItem;
import com.itextpdf.layout.element.Paragraph;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import java.io.File;
import java.io.FileOutputStream;

import com.itextpdf.*;

public class MainActivity extends AppCompatActivity {

    Button btnCreatePDF;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnCreatePDF = (Button) findViewById(R.id.btn_create_pdf);

        Dexter.withActivity(this)
                .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                       btnCreatePDF.setOnClickListener(new View.OnClickListener() {

                           @Override
                           public void onClick(View v) {
                               createPDFFile(Common.getAppPath(MainActivity.this) + "test_pdf.pdf");

                           }
                       });
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                    }
                })
                .check();
    }

    private void addNewItem(Document document, String text, int align){

    }

    private void addLineSeparator(Document document){
        //LineSeparator lineSeparator = new LineSeparator();
        //lineSeparator.setLineColor(new BaseColor(0,0,0));
        //addLineSpace(document);
        //document.add(new Chunk(lineSeparator));
        //addLineSpace(document);
    }

    private void createPDFFile(String path){
        if(new File(path).exists())
            new File(path).delete();
        try {
            PdfWriter writer = new PdfWriter(path);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf, PageSize.A6);
            // Create a PdfFont
            PdfFont font = PdfFontFactory.createFont("res/font/times.ttf");
            // Add a Paragraph
            document.add(new Paragraph("iText is:").setFont(font));
            // Create a List
            List list = new List()
                    .setSymbolIndent(12)
                    .setListSymbol("\u2022")
                    .setFont(font);
            // Add ListItem objects
            list.add(new ListItem("Never gonna give you up"))
                    .add(new ListItem("Never gonna let you down"))
                    .add(new ListItem("Never gonna run around and desert you"))
                    .add(new ListItem("Never gonna make you cry"))
                    .add(new ListItem("Never gonna say goodbye"))
                    .add(new ListItem("Never gonna tell a lie and hurt you"));
            // Add the list
            document.add(list);

            //Close document
            document.close();

            Toast.makeText(this, "SUCCESS", Toast.LENGTH_LONG).show();

            printPDF();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void printPDF() {
        PrintManager printManager = (PrintManager) getSystemService(Context.PRINT_SERVICE);
        try{
            PrintDocumentAdapter printDocumentAdapter = new PdfDocumentAdapter(MainActivity.this, Common.getAppPath(MainActivity.this) + ("test_pdf.pdf"));
            printManager.print("Document", printDocumentAdapter, new PrintAttributes.Builder().build());
        }catch(Exception ex){
            Log.e("BroUsers", ""+ ex.getMessage());
        }
    }
}