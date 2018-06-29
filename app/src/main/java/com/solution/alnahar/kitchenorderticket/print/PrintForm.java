package com.solution.alnahar.kitchenorderticket.print;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;


import com.hp.mss.hpprint.model.PDFPrintItem;
import com.hp.mss.hpprint.model.PrintItem;
import com.hp.mss.hpprint.model.PrintJobData;
import com.hp.mss.hpprint.model.asset.PDFAsset;
import com.hp.mss.hpprint.util.PrintUtil;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.solution.alnahar.kitchenorderticket.model.ItemModel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;




/**
 * Created by Engr Shahbaz Idrees on 8/15/2016.
 */



public  class PrintForm {

    ////  Font boldFont = new Font(Font.FontFamily.TIMES_ROMAN, 25, Font.BOLD);
    private static String FILE = Environment.getExternalStorageDirectory() + "/kot.pdf";

    Context context;


    Calendar cal;
    int day;
    int month;
    int year;
    String dateString;


    String pdfFileName;
    ArrayList<ItemModel> list;
    String resturantName;
    String kotNo;
    String waiterName;
    String tabelNo;
    String orderNo;
    String type;
    String compnyName;
    String pax;
    String netAmount;

    public PrintForm(Context context, ArrayList<ItemModel> list, String resturantName, String kotNo, String waiterName, String tabelNo, String orderNo, String type,
                     String compnyName, String pax,String netAmount) {
        this.context = context;
        this.list = list;
        cal = Calendar.getInstance();
        day = cal.get(Calendar.DAY_OF_MONTH);
        month = cal.get(Calendar.MONTH);
        year = cal.get(Calendar.YEAR);
        dateString = year + "-" + month + "-" + day;

        this.resturantName = resturantName;
        this.kotNo = kotNo;
        this.waiterName = waiterName;
        this.tabelNo = tabelNo;
        this.orderNo = orderNo;
        this.type = type;
        this.compnyName = compnyName;
        this.pax = pax;
        this.netAmount = netAmount;


        print();
    }


    public void print() {


        File f = new File(FILE);
        if (!(f.exists())) {
            try {
                f.createNewFile();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                // Log.e("FileCreated", "file");


            }
        }


        try {
            com.itextpdf.text.Document document = new com.itextpdf.text.Document();


            PdfWriter.getInstance(document, new FileOutputStream(FILE));
            document.open();

            addTitlePage(document);


            document.close();

            //Toast.makeText(context, "Pdf Created",Toast.LENGTH_LONG).show();
          //  pdfOpner(f);


            PDFAsset pdfAsset4x6 = new PDFAsset(FILE, false);
            PrintItem printItemDefault = new PDFPrintItem(PrintItem.ScaleType.CENTER, pdfAsset4x6);
            PrintJobData printJobData = new PrintJobData(context, printItemDefault);
            printJobData.setJobName("Example");
            PrintUtil.setPrintJobData(printJobData);
            PrintUtil.print((Activity) context);
        } catch (Exception e) {
            Log.e("Errorrr", e.getMessage());
            Toast.makeText(context, "Error---in Pdf Created", Toast.LENGTH_LONG).show();
        }


    }


    public void pdfOpner(File path) {

        Intent target = new Intent(Intent.ACTION_VIEW);
        target.setDataAndType(Uri.fromFile(path), "application/pdf");
        target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        Intent intent = Intent.createChooser(target, "Open File");
        try {

            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {

            Toast.makeText(context, "PDF VIWER IS NOT FOUND", Toast.LENGTH_LONG).show();
        }

    }


    private void addTitlePage(com.itextpdf.text.Document document)
            throws DocumentException, IOException {


        Font customFont = itextFontWithBold(25);


        Paragraph Header = new Paragraph(compnyName, customFont);
        Header.setAlignment(Element.ALIGN_CENTER);
        document.add(Header);

        Paragraph p3 = new Paragraph();
        addEmptyLine(p3, 1);

        document.add(p3);

// date--kot---table
        smallTable(document);

        // waiter--pax---- table
        orderDateTabel(document);
        // order--table-----table
        noOfPersonsTabel(document);

        // now give space from  main Table
        Paragraph preface = new Paragraph();
        addEmptyLine(preface, 1);
        document.add(preface);


        Paragraph Header1 = new Paragraph("Kot", customFont);
        Header1.setAlignment(Element.ALIGN_CENTER);
        document.add(Header1);


        // Add a table
        createTableNew(document);


    }


    public void createTableNew(com.itextpdf.text.Document document)
            throws DocumentException, IOException {

        Font customFont = itextFontWithBold(16);


        Font customFont2 = itextFontWithBold(16);

//       float[] columnwidth={1,5,2,2,2};
        float[] columnwidth1 = {1, 5, 1, 2};
        PdfPTable table2 = new PdfPTable(columnwidth1);
        table2.setWidthPercentage(100);

        //PdfPCell c1 = new PdfPCell(new Phrase("نمبر شمار",customFont));
        PdfPCell c1 = new PdfPCell(new Phrase("Sr#", customFont2));
        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
        c1.setBackgroundColor(BaseColor.GRAY);

        //  c1.setBorderWidthLeft(Rectangle.NO_BORDER);
        c1.setBorderWidthRight(Rectangle.NO_BORDER);

        table2.addCell(c1);

//        c1 = new PdfPCell(new Phrase("تفصیل",customFont));
        c1 = new PdfPCell(new Phrase("Product", customFont));
        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
        c1.setBackgroundColor(BaseColor.GRAY);
        c1.setBorderWidthLeft(Rectangle.NO_BORDER);
        c1.setBorderWidthRight(Rectangle.NO_BORDER);
        table2.addCell(c1);


        c1 = new PdfPCell(new Phrase("Qty", customFont));
        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
        c1.setBackgroundColor(BaseColor.GRAY);
        c1.setBorderWidthLeft(Rectangle.NO_BORDER);
        c1.setBorderWidthRight(Rectangle.NO_BORDER);
        table2.addCell(c1);

        c1 = new PdfPCell(new Phrase("Uom", customFont));
        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
        c1.setBackgroundColor(BaseColor.GRAY);
        c1.setBorderWidthLeft(Rectangle.NO_BORDER);
        //  c11.setBorderWidthRight(Rectangle.NO_BORDER);
        table2.addCell(c1);

        table2.setHeaderRows(1);

        Font customFont1 = itextFontWithoutBold(13);

        for (int i = 0; i < list.size(); i++) {

            int k = i;
            k += 1;


            String rate = list.get(i).getPrice();
            String product = list.get(i).getName();
            int qty = list.get(i).getQuantity();
            String uom = list.get(i).getUom();


            //SrNo
            c1 = new PdfPCell(new Phrase(k + "", customFont1));
            c1.setHorizontalAlignment(Element.ALIGN_LEFT);
            c1.setBorderWidthRight(Rectangle.NO_BORDER);
            table2.addCell(c1);
            //description
            c1 = new PdfPCell(new Phrase(product, customFont1));
            c1.setHorizontalAlignment(Element.ALIGN_LEFT);
            c1.setBorderWidthRight(Rectangle.NO_BORDER);
            c1.setBorderWidthLeft(Rectangle.NO_BORDER);
            table2.addCell(c1);

            c1 = new PdfPCell(new Phrase(qty + "", customFont1));
            c1.setHorizontalAlignment(Element.ALIGN_LEFT);
            c1.setBorderWidthRight(Rectangle.NO_BORDER);
            c1.setBorderWidthLeft(Rectangle.NO_BORDER);
            table2.addCell(c1);

            c1 = new PdfPCell(new Phrase(uom, customFont1));
            c1.setHorizontalAlignment(Element.ALIGN_LEFT);
            c1.setBorderWidthLeft(Rectangle.NO_BORDER);
            table2.addCell(c1);


        }




        try {
            document.add(table2);

            Paragraph p3 = new Paragraph();
            addEmptyLine(p3, 1);
            document.add(p3);


            // if total amount table

                smallTabelTotalAmount(document);
            Paragraph p5=new Paragraph();
            addEmptyLine(p5, 1);
            document.add(p5);




            //First Line
            LineSeparator Line1 = new LineSeparator(customFont2);

            Line1.setLineWidth(2);
            Line1.setPercentage(25);
            Line1.setAlignment(Element.ALIGN_LEFT);
            document.add(Line1);


            //Space
            Paragraph Line_SPace = new Paragraph();
            addEmptyLine(Line_SPace, 1);
            document.add(Line_SPace);

            Paragraph Approveby = new Paragraph();

//               Approveby.add( new Paragraph("           Approved By",customFont2));
//              Approveby.add( new Paragraph("                                      Accountant ",customFont2));
//       Approveby.add( new Paragraph("                                      Received By",customFont2));


            Approveby.add("           Approved By");
//        Approveby.add("                                      Accountant ");
//        Approveby.add("                                      Received By");
            document.add(Approveby);


        }
        catch (Exception e)
        {
            e.getMessage();
        }

    }





    public  void smallTabelTotalAmount(com.itextpdf.text.Document document) throws DocumentException, IOException {
        Font customFont2=itextFontWithoutBold(15);

        float[] columnwidth = {1};
        PdfPTable table = new PdfPTable(columnwidth);
        table.setWidthPercentage(50);

        //Extra
        PdfPCell c4 = new PdfPCell(new Phrase("Total:         "+netAmount,customFont2));
        c4.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(c4);

        try {
            document.add(table);


        } catch (DocumentException e) {
            e.printStackTrace();
        }


    }



    public   void smallTable(com.itextpdf.text.Document document) throws DocumentException, IOException {


        Font customFont2=itextFontWithoutBold(15);

            float[] columnwidth = {3,1};
            PdfPTable table = new PdfPTable(columnwidth);
            table.setWidthPercentage(100);

            //Customer
            PdfPCell c1 = new PdfPCell(new Phrase("Date   : "+dateString,customFont2));
            c1.setHorizontalAlignment(Element.ALIGN_LEFT);
        c1.setBorder(Rectangle.NO_BORDER);

            table.addCell(c1);
            //Invoice
            PdfPCell c2 = new PdfPCell(new Phrase("Kot #   "+kotNo,customFont2));
            c2.setHorizontalAlignment(Element.ALIGN_LEFT);
            c2.setBorder(Rectangle.NO_BORDER);
            table.addCell(c2);


        try {
            document.add(table);


        } catch (DocumentException e) {
            e.printStackTrace();
        }



    }

    public   void orderDateTabel(com.itextpdf.text.Document document) throws DocumentException, IOException {


        Font customFont2=itextFontWithoutBold(15);
        Font customFont3=itextFontWithBold(15);

        float[] columnwidth = {3,1};
        PdfPTable table = new PdfPTable(columnwidth);
        table.setWidthPercentage(100);

        //Customer
        PdfPCell c1 = new PdfPCell(new Phrase("Waiter   : "+waiterName,customFont2));
        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
        c1.setBorder(Rectangle.NO_BORDER);

        table.addCell(c1);
        //Invoice
        PdfPCell c2 = new PdfPCell(new Phrase("Pax: "+pax,customFont3));
        c2.setHorizontalAlignment(Element.ALIGN_LEFT);
       // c2.setBackgroundColor(BaseColor.GRAY);
        c2.setBorder(Rectangle.NO_BORDER);

        table.addCell(c2);


        try {
            document.add(table);


        } catch (DocumentException e) {
            e.printStackTrace();
        }



    }

    public   void noOfPersonsTabel(com.itextpdf.text.Document document) throws DocumentException, IOException {


        Font customFont2=itextFontWithoutBold(15);
        Font customFont3=itextFontWithBold(15);

        float[] columnwidth = {3,1};
        PdfPTable table = new PdfPTable(columnwidth);
        table.setWidthPercentage(100);

        //Customer
        PdfPCell c1 = new PdfPCell(new Phrase("Order   : "+orderNo,customFont2));
        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
        c1.setBorder(Rectangle.NO_BORDER);

        table.addCell(c1);

        PdfPCell c2 = new PdfPCell(new Phrase("Table: "+tabelNo,customFont3));
        c2.setHorizontalAlignment(Element.ALIGN_LEFT);
        c2.setBorder(Rectangle.NO_BORDER);
        table.addCell(c2);


        try {
            document.add(table);


        } catch (DocumentException e) {
            e.printStackTrace();
        }



    }






    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }





    }


    public Font itextFontWithBold(int fontSize) throws DocumentException, IOException {

        BaseFont fontPath = null;

        fontPath = BaseFont.createFont("assets/fonts/Calibri.ttf", "UTF-8", BaseFont.EMBEDDED);


        Font font = new Font(fontPath, fontSize);

        font.isBold();

        return  font;

    }


    public Font itextFontWithoutBold(int fontSize) throws DocumentException, IOException {

        BaseFont fontPath= null;

        fontPath = BaseFont.createFont("assets/fonts/Calibri.ttf", "UTF-8", BaseFont.EMBEDDED);


        Font font = new Font(fontPath, fontSize);
        return  font;

    }

}
