package model.pdf;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import model.Book;
import model.User;


public class GeneratePdfSoldBooks {

    public void generatePdf(final String fileName, List<Book> bookList) throws DocumentException, IOException {
        Document document = new Document(PageSize.A4, 25, 25, 25, 25);

        PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(fileName));

        document.open();

        Paragraph title = new Paragraph("Sold Books");

        document.add(title);

        PdfPTable t = new PdfPTable(4);
        t.setSpacingBefore(25);
        t.setSpacingAfter(25);

        t.addCell(new PdfPCell(new Phrase("Book ID")));
        t.addCell(new PdfPCell(new Phrase("Book Title")));
        t.addCell(new PdfPCell(new Phrase("Book Author")));
        t.addCell(new PdfPCell(new Phrase("Book Published Date")));

        for(Book b : bookList) {
            t.addCell(String.valueOf(b.getId()));
            t.addCell(b.getTitle());
            t.addCell(b.getAuthor());
            t.addCell(String.valueOf(b.getPublishedDate()));
        }

        document.add(t);

        document.close();
        pdfWriter.close();
    }
}