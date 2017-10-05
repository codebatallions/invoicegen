package za.co.tfoldcord.docgen;

import com.itextpdf.io.font.FontConstants;
import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;

import java.io.IOException;

/**
 * Created by sefako on 2017/07/27.
 */
public class HeaderEventHandler implements IEventHandler {
    protected String header;

    public void setHeader(String header) {
        this.header = header;
    }


    public void handleEvent(Event event) {
        PdfDocumentEvent documentEvent = (PdfDocumentEvent) event;
        try {
            new PdfCanvas(documentEvent.getPage())
                    .beginText()
                    .setFontAndSize(PdfFontFactory.createFont(FontConstants.HELVETICA), 12)
                    .moveText(450, 806)
                    .showText(header)
                    .endText()
                    .stroke();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
