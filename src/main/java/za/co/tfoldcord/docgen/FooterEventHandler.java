package za.co.tfoldcord.docgen;

import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.Style;
import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * Created by sefako on 2017/07/27.
 */
public class FooterEventHandler  implements IEventHandler {
    private Table table;
    private Map<FooterDetails, String> footerDetails;

    public FooterEventHandler( Map<FooterDetails, String> footerDetails) {
        this.table = footerTable();
        this.footerDetails = footerDetails;
    }


    public void handleEvent(Event event) {
        PdfDocumentEvent docEvent = (PdfDocumentEvent) event;
        PdfDocument pdfDoc = docEvent.getDocument();
        PdfPage page = docEvent.getPage();
        PdfCanvas canvas = new PdfCanvas(page.newContentStreamBefore(), page.getResources(), pdfDoc);
        new Canvas(canvas, pdfDoc, new Rectangle(36, 20, page.getPageSize().getWidth() - 72, 50))
                .add(table);
    }

    private Table footerTable()  {
        Table table = new Table(1);
        Style style = new Style()
                .setBorder(Border.NO_BORDER);
        table.setWidth(225);
        Cell cell = null;

        this.footerDetails.forEach((k,v)->System.out.println("FKey : " + k + "FValue : " + v));
        if(StringUtils.isNotEmpty(footerDetails.get(FooterDetails.COMMENTS))) {
            cell = (new Cell().add(new Paragraph(footerDetails.get(FooterDetails.COMMENTS)).setFontSize(5).setBorder(Border.NO_BORDER)).addStyle(style));
        }

        if(StringUtils.isNotEmpty(footerDetails.get(FooterDetails.COPYRIGHT))) {
            cell = (new Cell().add(new Paragraph(footerDetails.get(FooterDetails.COPYRIGHT)).setFontSize(5).setBorder(Border.NO_BORDER)).addStyle(style));
            table.addCell(cell);
        }


        table.addCell(cell);
        return table;
    }
}
