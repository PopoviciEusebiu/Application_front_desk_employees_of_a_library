package model.pdf;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import model.User;

public class GeneratePdfEmployeeActivity {

    public void generatePdf(final String fileName, Map<User, String> employeeActivity) throws DocumentException, IOException {
        Document document = new Document(PageSize.A4, 25, 25, 25, 25);

        PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(fileName));

        document.open();

        Paragraph title = new Paragraph("Employee activity: ");
        document.add(title);

        PdfPTable table = new PdfPTable(2);
        table.setSpacingBefore(25);
        table.setSpacingAfter(25);
        table.setWidthPercentage(100);

        PdfPCell headerCell1 = new PdfPCell(new Phrase("Employee Username"));
        PdfPCell headerCell2 = new PdfPCell(new Phrase("Book Title"));

        table.addCell(headerCell1);
        table.addCell(headerCell2);

        for (Map.Entry<User, String> entry : employeeActivity.entrySet()) {
            User employee = entry.getKey();
            String bookTitle = entry.getValue();

            PdfPCell cell1 = new PdfPCell(new Phrase("Username: " + employee.getUsername()));
            PdfPCell cell2 = new PdfPCell(new Phrase("Title: " + bookTitle));

            table.addCell(cell1);
            table.addCell(cell2);
        }

        document.add(table);

        document.close();
        pdfWriter.close();
    }
}
