package za.co.tfoldcord.docgen;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.color.DeviceRgb;
import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.Style;
import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.layout.LayoutArea;
import com.itextpdf.layout.layout.LayoutContext;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;
import com.itextpdf.layout.renderer.TableRenderer;


/**
 * Created by sefako@gmail.com on 2017/10/22.
 */

public class BasicInvoiceHeaderEventHandler implements IEventHandler {
	protected Table table;
    protected float tableHeight;
    protected Document doc;
    protected Map<HeaderDetails, String> headerDetails;

    public BasicInvoiceHeaderEventHandler(Document doc, Map<HeaderDetails, String> headerDetails) {
        this.doc = doc;
        this.headerDetails = headerDetails;
        table = headerTable();
        TableRenderer renderer = (TableRenderer) table.createRendererSubTree();
        renderer.setParent(new Document(new PdfDocument(new PdfWriter(new ByteArrayOutputStream()))).getRenderer());
        tableHeight = renderer.layout(new LayoutContext(new LayoutArea(0, PageSize.A4))).getOccupiedArea().getBBox().getHeight();
    }


    public void handleEvent(Event event) {
        PdfDocumentEvent docEvent = (PdfDocumentEvent) event;
        PdfDocument pdfDoc = docEvent.getDocument();
        PdfPage page = docEvent.getPage();
        PdfCanvas canvas = new PdfCanvas(page.newContentStreamBefore(), page.getResources(), pdfDoc);
        Rectangle rect = new Rectangle(pdfDoc.getDefaultPageSize().getX() + doc.getLeftMargin(),
                pdfDoc.getDefaultPageSize().getTop() - doc.getTopMargin(), 100, getTableHeight());
        new Canvas(canvas, pdfDoc, rect)
                .add(table);
        Paragraph p = new Paragraph();
        p.add(new Text("               "));
        doc.add(p);

    }

    public float getTableHeight() {
        return tableHeight;
    }

    private Table headerTable(){
        Table table = new Table(2);
        table.setWidth(500);
        Color rightColor =  new DeviceRgb(176,224,230);
        Color leftColor =  new DeviceRgb(176,224,230);// new DeviceRgb(76,196,222);
        //table.setBorder(Border.SOLID);
        Style style = new Style()
                .setBorder(Border.NO_BORDER);
        Cell cell = null;

        //left side 
        if(StringUtils.isNotEmpty(headerDetails.get(HeaderDetails.COMPANY_NAME))) {
            cell = (new Cell().add(new Paragraph(headerDetails.get(HeaderDetails.COMPANY_NAME)).setFontSize(24).setBorder(Border.NO_BORDER)).addStyle(style));
            cell.setBackgroundColor(leftColor);
            table.addCell(cell);
        }else {
        	addBlankCell(table, leftColor);
        }
        
        // right side 
        cell = (new Cell().add(new Paragraph("Invoice").setFontSize(24).setBorder(Border.NO_BORDER)).addStyle(style));
        cell.setTextAlignment(TextAlignment.RIGHT);
        cell.setBackgroundColor(rightColor);
        table.addCell(cell);
        //left side
        if(StringUtils.isNotEmpty(headerDetails.get(HeaderDetails.PHYSICAL_ADDRESS))) {
        	String [] addressLines = headerDetails.get(HeaderDetails.PHYSICAL_ADDRESS).split(",");
        	Paragraph address = new Paragraph();
        	for(String line : addressLines) {
        		
        		Text t1 = new Text(line + "\n");        		
        		t1.setFontSize(5);
        		//t1.setBorder(Border.NO_BORDER);
        		address.add(t1);
        	}
        	//address.setBorder(Border.NO_BORDER);
        	cell = (new Cell().add(address.setFontSize(5)).addStyle(style));
        	cell.setBackgroundColor(leftColor);
        	
    		table.addCell(cell);
        }else {
        	addBlankCell(table, leftColor);
        }
        
        // right side - invoice date
        cell = (new Cell().add(new Paragraph(DocGenUtils.nowDate()).setFontSize(7).setBorder(Border.NO_BORDER)).addStyle(style));
        cell.setTextAlignment(TextAlignment.RIGHT);
        cell.setVerticalAlignment(VerticalAlignment.MIDDLE);
        cell.setBackgroundColor(rightColor);
        table.addCell(cell);
        
        
        //left side        
        if(StringUtils.isNotEmpty(headerDetails.get(HeaderDetails.TELEPHONE)) || StringUtils.isNotEmpty(headerDetails.get(HeaderDetails.EMAIL)) ) {
           
            Text t1 = new Text("Tel : " + headerDetails.get(HeaderDetails.TELEPHONE) + "\n");
            t1.setFontSize(5);
            Text t2 = new Text("Email : " +headerDetails.get(HeaderDetails.EMAIL));
            t2.setFontSize(5);
            Paragraph para = new Paragraph();
            para.setBorder(Border.NO_BORDER);
            para.add(t1);
            para.add(t2);
            cell = (new Cell().add(para.setBorder(Border.NO_BORDER)).addStyle(style));
            cell.setBackgroundColor(leftColor);
            table.addCell(cell);
        }
        else {
        	addBlankCell(table, leftColor);
        }
       
        //right side
        addBlankCell(table, rightColor);
        
        
        //left side
        if(StringUtils.isNotEmpty(headerDetails.get(HeaderDetails.WEBSITE))) {
            cell = (new Cell().add(new Paragraph(headerDetails.get(HeaderDetails.WEBSITE)).setFontSize(5).setBorder(Border.NO_BORDER)).addStyle(style));
            cell.setBackgroundColor(leftColor);
            table.addCell(cell);
        }
        else {
        	addBlankCell(table, leftColor);
        }
        //right side
       
        addBlankCell(table, rightColor); 
        
       
        //add empty row
        addBlankCell(table, Color.WHITE);
        addBlankCell(table, Color.WHITE);
        addBlankCell(table, Color.WHITE);
        addBlankCell(table, Color.WHITE);


        return table;
    }
    
    protected void addBlankCell(Table table, Color color){
    	Cell cell = new Cell().add(new Paragraph("    ").setBorder(Border.NO_BORDER));
    	cell.setBorder(Border.NO_BORDER);
    	cell.setBackgroundColor(color);
    	table.addCell(cell);
    }

}
