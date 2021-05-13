package com.bro.bellavistaordini;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.fonts.Font;
import android.os.Bundle;
import android.os.Environment;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.provider.DocumentsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.itextpdf.io.IOException;
import com.itextpdf.kernel.color.DeviceGray;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.LineSeparator;
import com.itextpdf.layout.element.List;
import com.itextpdf.layout.element.ListItem;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Objects;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.UnitValue;

import com.itextpdf.*;

public class MainActivity extends AppCompatActivity {

    Button btnCreatePDF;
    public static final String PATH = (Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString());

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
                               File file = new File(PATH);
                               Objects.requireNonNull(file.getParentFile()).mkdirs();
                               createPDFFile(PATH);
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

    private void createPDFFile(String path) throws IOException{

        try {
            File file = new File(PATH, "1.pdf");
            OutputStream outputStream = new FileOutputStream(file);
            PdfWriter writer = new PdfWriter(String.valueOf(file));
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document doc = new Document(pdfDoc, PageSize.A4.rotate());

            float[] columnWidths = {1, 5, 5};
            Table table = new Table(UnitValue.createPercentArray(columnWidths));

            //PdfFont f = PdfFontFactory.createFont(StandardFonts.HELVETICA);
            Cell cell = new Cell(1, 3)
                    .add(new Paragraph("This is a header"))
                    //.setFont(f)
                    .setFontSize(13)
                    .setFontColor(DeviceGray.WHITE)
                    .setBackgroundColor(DeviceGray.BLACK)
                    .setTextAlignment(TextAlignment.CENTER);

            table.addHeaderCell(cell);

            for (int i = 0; i < 2; i++) {
                Cell[] headerFooter = new Cell[]{
                        new Cell().setBackgroundColor(new DeviceGray(0.75f)).add(new Paragraph("#")),
                        new Cell().setBackgroundColor(new DeviceGray(0.75f)).add(new Paragraph("Key")),
                        new Cell().setBackgroundColor(new DeviceGray(0.75f)).add(new Paragraph("Value"))
                };

                for (Cell hfCell : headerFooter) {
                    if (i == 0) {
                        table.addHeaderCell(hfCell);
                    } else {
                        table.addFooterCell(hfCell);
                    }
                }
            }

            for (int counter = 0; counter < 100; counter++) {
                table.addCell(new Cell().setTextAlignment(TextAlignment.CENTER).add(new Paragraph(String.valueOf(counter + 1))));
                table.addCell(new Cell().setTextAlignment(TextAlignment.CENTER).add(new Paragraph("key " + (counter + 1))));
                table.addCell(new Cell().setTextAlignment(TextAlignment.CENTER).add(new Paragraph("value " + (counter + 1))));
            }

            doc.add(table);

            doc.close();

            Toast.makeText(this, "SUCCESS", Toast.LENGTH_LONG).show();

            printPDF();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "PORCODIO NON FUNGEEEEEE", Toast.LENGTH_LONG).show();
        }
    }

    private void printPDF() {
        PrintManager printManager = (PrintManager) getSystemService(Context.PRINT_SERVICE);
        try{
            PrintDocumentAdapter printDocumentAdapter = new PdfDocumentAdapter(MainActivity.this, PATH + File.pathSeparator + ("1.pdf"));
            printManager.print("Document", printDocumentAdapter, new PrintAttributes.Builder().build());
        }catch(Exception ex){
            Log.e("BroUsers", ""+ ex.getMessage());
        }
    }
}