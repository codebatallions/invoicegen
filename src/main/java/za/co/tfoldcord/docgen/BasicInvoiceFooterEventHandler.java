package za.co.tfoldcord.docgen;

import com.itextpdf.kernel.color.DeviceRgb;
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
import com.itextpdf.layout.border.OutsetBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xwpf.usermodel.Borders;

import java.util.Map;

/**
 * Created by sefako on 2017/07/27.
 */
public class BasicInvoiceFooterEventHandler  implements IEventHandler {
    private Table table;
    private String footerDetails;

    public BasicInvoiceFooterEventHandler( String footerDetails) {
        this.table = new Table(2);
        this.table.setWidth(300);
        this.footerDetails = footerDetails;
    }


    public void handleEvent(Event event) {
        PdfDocumentEvent docEvent = (PdfDocumentEvent) event;
        PdfDocument pdfDoc = docEvent.getDocument();
        PdfPage page = docEvent.getPage();
        PdfCanvas canvas = new PdfCanvas(page.newContentStreamBefore(), page.getResources(), pdfDoc);
        new Canvas(canvas, pdfDoc, new Rectangle(36, 20, page.getPageSize().getWidth() - 72, 50))
                .add(footerTable());
    }

    private Table footerTable()  {
    	this.table.setBorder(new OutsetBorder(new DeviceRgb(51, 76, 158), 1));
        if(StringUtils.isNotEmpty(footerDetails)){
	        Cell cell = null;
	        String [] payDetails = footerDetails.split(",");
	        for(String details : payDetails) {
		        cell = (new Cell().add(new Paragraph(details).setFontSize(5).setBorder(Border.NO_BORDER)));
		        cell.setTextAlignment(TextAlignment.LEFT);
		        cell.setVerticalAlignment(VerticalAlignment.MIDDLE);
		        cell.setBorder(Border.NO_BORDER);
                System.out.println("Addint cell Detail   : " + details);
                table.addCell(cell);
	        }
        }

        
        return table;
    }
}
