package za.co.tfoldcord.docgen;

import com.itextpdf.io.font.FontConstants;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.color.DeviceRgb;
import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfNumber;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.border.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import org.apache.commons.lang3.StringUtils;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

/**
 * Used to create pdf using itext
 * Created by sefako on 2017/08/12.
 */
public class GenericInvoiceGenerator {

    private  Map<HeaderDetails, String> headerDetails;
    private  Map<FooterDetails, String> footerDetails;
    private  List<InvoiceColumn> invoiceColumns;
    public static Color rowColor = new DeviceRgb(242, 242, 242);
    private PdfFont normal = null;
    private PdfFont bold = null;
    public static final PdfNumber PORTRAIT = new PdfNumber(0);
    public static final PdfNumber LANDSCAPE = new PdfNumber(90);
    public static final PdfNumber INVERTEDPORTRAIT = new PdfNumber(180);
    public static final PdfNumber SEASCAPE = new PdfNumber(270);


    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final String FILE_NAME = "Invoice-" + LocalDate.now().format(formatter) + ".pdf";

    /**
     * 
     * @param invoiceColumns column labels and sizes on document
     * @param headerDetails  header details on document
     * @param footerDetails  footer details on document
     */
    public  GenericInvoiceGenerator(List<InvoiceColumn> invoiceColumns, Map<HeaderDetails, String> headerDetails, Map<FooterDetails, String> footerDetails){
        this.headerDetails = headerDetails;
        this.footerDetails = footerDetails;
        this.invoiceColumns = invoiceColumns;
    }

    /*
    public OutputStream generateInvoiceStream() throws DocGenException{

    }*/

    /**
     * 
     * @param rowData actual row items on document
     * @param baos byte stream where file should be created
     * @throws DocGenException 
     */
    public void generateInvoiceBytes(String [][] rowData, ByteArrayOutputStream baos) throws DocGenException{
        PdfWriter writer = new PdfWriter(baos);

         PdfDocument pdf = new PdfDocument(writer);
         PageRotationEventHandler eventHandler = new PageRotationEventHandler();
         pdf.addEventHandler(PdfDocumentEvent.START_PAGE, eventHandler);
         Document document = new Document(pdf);
         // header details
         TableHeaderEventHandler handler = new TableHeaderEventHandler(document, headerDetails);
         pdf.addEventHandler(PdfDocumentEvent.END_PAGE, handler);
         document.setMargins(20 + handler.getTableHeight(), 36, 36, 36);
         // end of header details
         // add a footer
        // pdf.addEventHandler(PdfDocumentEvent.END_PAGE, new FooterEventHandler(footerDetails));
         // end of footer
         try {
			bold = PdfFontFactory.createFont(FontConstants.TIMES_BOLD);
			normal = PdfFontFactory.createFont(FontConstants.TIMES_ROMAN);
		} catch (IOException e) {
			throw new DocGenException("Doc Genertion error");
		}
         


         Table table =  createTable(bold);
         table.setWidthPercent(100);
         addRowContent(table,rowData, normal);
         document.add(table);
         document.close();

    }

    /**
     *  This method will generate document in user home directory
     * @param rowData row data for document
     * @throws DocGenException
     */
    public void generateInvoiceFile(String [][] rowData) throws DocGenException {
        String userHomDir = System.getProperty("user.home") + File.separator;
        File invoiceFile = new File(userHomDir + FILE_NAME);
        invoiceFile.getParentFile().mkdirs();
        try {
            PdfWriter writer = new PdfWriter(userHomDir + FILE_NAME);
            bold = PdfFontFactory.createFont(FontConstants.TIMES_BOLD);
            normal = PdfFontFactory.createFont(FontConstants.TIMES_ROMAN);

            PdfDocument pdf = new PdfDocument(writer);
            PageRotationEventHandler eventHandler = new PageRotationEventHandler();


            pdf.addEventHandler(PdfDocumentEvent.START_PAGE, eventHandler);
            Document document = new Document(pdf);
            // header details
            TableHeaderEventHandler handler = new TableHeaderEventHandler(document, headerDetails);
            pdf.addEventHandler(PdfDocumentEvent.END_PAGE, handler);
            document.setMargins(20 + handler.getTableHeight(), 36, 36, 36);






            Table table =  createTable(bold);
            table.setWidthPercent(100);
            int index = 0;
            addRowContent(table,rowData, normal);
            document.add(table);
            //FooterEventHandler footerHandler = new FooterEventHandler(footerDetails);
            //pdf.addEventHandler(PdfDocumentEvent.END_PAGE,footerHandler);
            document.close();

        } catch (Exception e) {
                e.printStackTrace();
                throw new DocGenException("Document Generation Error", e.getCause());

        }
    }


    private void addRowContent(Table table, String [][] rowData, PdfFont normal) {
        boolean bgColor = false;
        for(String [] rowDetail : rowData){
            for(String columnData : rowDetail) {
                if(StringUtils.isEmpty(columnData))
                    break;
                table.addCell(createCell(columnData, 1, 1, TextAlignment.LEFT, normal, bgColor, false, 8));
            }
            bgColor =!bgColor;
        }
    }



    /**
     * HeaderFooter header = new HeaderFooter(phrase1, true);
     HeaderFooter footer = new HeaderFooter(phrase2, true);
     document.setHeader(header);
     document.setFooter(footer);
     */

    private Cell createCell(String content, float borderWidth, int colspan, TextAlignment alignment, PdfFont font, boolean bgColor, boolean header, float size) {

        Cell cell = new Cell(1, colspan).add(new Paragraph(content).setFont(font).setFontSize(size));
        cell.setTextAlignment(alignment);
        if(bgColor) {
            if(header)
                cell.setBackgroundColor(Color.LIGHT_GRAY);
            else{
                cell.setBackgroundColor(rowColor).setFontSize(8);
            }
        }
        cell.setBorder(new SolidBorder(borderWidth));
        return cell;
    }

    private Table createTable(PdfFont headerFont){
        float [] tableScale = new float[invoiceColumns.size()];
        int ratioIndex = 0;
        for(InvoiceColumn column : invoiceColumns){
            tableScale[ratioIndex++] = column.getRatio();
        }
        Table table = new Table(tableScale);
        createTableHeader(table, headerFont);
        return table;
    }

    private void createTableHeader(Table table, PdfFont headerFont){
        invoiceColumns.forEach(column -> System.out.println(" Column [" +column.getName() +","  + column.getRatio()+"]"));
        for(InvoiceColumn column : invoiceColumns){
                table.addCell(createCell(column.getName(), 1, 1, TextAlignment.CENTER, headerFont, true, true, 10));
            }

    }





    protected class PageRotationEventHandler implements IEventHandler {
        protected PdfNumber rotation  = PORTRAIT;
        public void setRotation(PdfNumber orientation) {
            this.rotation = orientation;
        }
        @Override
        public void handleEvent(Event event) {
            PdfDocumentEvent docEvent = (PdfDocumentEvent) event;
            docEvent.getPage().put(PdfName.Rotate, rotation);
        }
    }


}
