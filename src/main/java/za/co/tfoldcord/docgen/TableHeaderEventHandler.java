package za.co.tfoldcord.docgen;

import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.kernel.events.Event;


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
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.layout.LayoutArea;
import com.itextpdf.layout.layout.LayoutContext;
import com.itextpdf.layout.renderer.TableRenderer;
import com.itextpdf.kernel.events.IEventHandler;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * Created by sefako on 2017/07/27.
 */
public class TableHeaderEventHandler implements IEventHandler {
    protected Table table;
    protected float tableHeight;
    protected Document doc;
    protected Map<HeaderDetails, String> headerDetails;

    public TableHeaderEventHandler(Document doc, Map<HeaderDetails, String> headerDetails) {
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
        Table table = new Table(1);
        table.setWidth(500);
        //table.setBorder(Border.SOLID);
        Style style = new Style()
                .setBorder(Border.NO_BORDER);
        Cell cell = null;

        if(StringUtils.isNotEmpty(headerDetails.get(HeaderDetails.COMPANY_NAME))) {
            cell = (new Cell().add(new Paragraph(headerDetails.get(HeaderDetails.COMPANY_NAME)).setFontSize(12).setBorder(Border.NO_BORDER)).addStyle(style));
            table.addCell(cell);
        }
        if(StringUtils.isNotEmpty(headerDetails.get(HeaderDetails.PHYSICAL_ADDRESS))) {
            cell = (new Cell().add(new Paragraph(headerDetails.get(HeaderDetails.PHYSICAL_ADDRESS)).setFontSize(5).setBorder(Border.NO_BORDER)).addStyle(style));
            table.addCell(cell);
        }

        if(StringUtils.isNotEmpty(headerDetails.get(HeaderDetails.TELEPHONE)) || StringUtils.isNotEmpty(headerDetails.get(HeaderDetails.EMAIL)) ) {
            String contacts = "Tel : " + headerDetails.get(HeaderDetails.TELEPHONE) +", Email : " +headerDetails.get(HeaderDetails.EMAIL);
            cell = (new Cell().add(new Paragraph(contacts).setFontSize(5).setBorder(Border.NO_BORDER)).addStyle(style));
            table.addCell(cell);
        }
        if(StringUtils.isNotEmpty(headerDetails.get(HeaderDetails.WEBSITE))) {
            cell = (new Cell().add(new Paragraph(headerDetails.get(HeaderDetails.WEBSITE)).setFontSize(5).setBorder(Border.NO_BORDER)).addStyle(style));
            table.addCell(cell);
        }

        cell = (new Cell().add(new Paragraph("").setBorder(Border.NO_BORDER)).addStyle(style));
        table.addCell(cell);


        return table;
    }
}
