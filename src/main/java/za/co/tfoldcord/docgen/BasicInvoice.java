package za.co.tfoldcord.docgen;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import com.itextpdf.io.font.FontConstants;
import com.itextpdf.kernel.color.Color;
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
import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.border.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;


	/**
	 * Used to create pdf using itext 7
	 * Created by sefako on 2017/10/22.
	 */
public class BasicInvoice extends PdfBase{

	    
	    private static final String FILE_NAME = "Team-Invoice-" + DocGenUtils.nowDate() + ".pdf";
	    private Map<HeaderDetails, String> invoiceHeader; 
	    
	    public BasicInvoice(Map<HeaderDetails, String> invoiceHeader) {
	    	this.invoiceHeader = invoiceHeader;
	    }
	   
	   

	    /**
	     * 
	     * @param eventDTo actual row items on document
	     * @param baos byte stream where file should be created
	     * @throws DocGenException 
	     */

	    public void generateInvoiceBytes(BasicInvoiceEventDTO eventDTo, ByteArrayOutputStream baos) throws DocGenException{
	             PdfWriter writer = new PdfWriter(baos);
				try{
	            PdfFont bold = PdfFontFactory.createFont(FontConstants.TIMES_BOLD);
	            PdfFont normal = PdfFontFactory.createFont(FontConstants.TIMES_ROMAN);

	            PdfDocument pdf = new PdfDocument(writer);

	            PageRotationEventHandler eventHandler = new PageRotationEventHandler();

            	
	            pdf.addEventHandler(PdfDocumentEvent.START_PAGE, eventHandler);
	            Document document = new Document(pdf);
	            
	            // header details
	            BasicInvoiceHeaderEventHandler handler = new BasicInvoiceHeaderEventHandler(document, invoiceHeader);
	            pdf.addEventHandler(PdfDocumentEvent.END_PAGE, handler);
	            document.setMargins(20 + handler.getTableHeight(), 36, 36, 36);





	            Table clientDetails = createClientDetails(eventDTo.getTeamName(), eventDTo.getTeamContact(), eventDTo.getTeamAddress());
	            clientDetails.setHorizontalAlignment(HorizontalAlignment.CENTER);
	            document.add(clientDetails);
	            Table invoiceDetails =  createInvoiceDetails(eventDTo, bold);
	            invoiceDetails.setWidthPercent(100);


	             document.add(invoiceDetails);
	             BasicInvoiceFooterEventHandler  footerHandler = new BasicInvoiceFooterEventHandler(eventDTo.getPaymentDetails());
	             pdf.addEventHandler(PdfDocumentEvent.END_PAGE,footerHandler);
	            document.close();


	        } catch (Exception e) {
	                e.printStackTrace();
	                throw new DocGenException("Document Generation Error", e.getCause());

	        }

	    }

	    /**
	     *  This method will generate document in user home directory
	     * @param eventDTo row data for document
	     * @throws DocGenException
	     */
	    public void generateInvoiceFile(BasicInvoiceEventDTO eventDTo) throws DocGenException {
	        String userHomDir = System.getProperty("user.home") + File.separator;
	        File invoiceFile = new File(userHomDir + FILE_NAME);
	        invoiceFile.getParentFile().mkdirs();
	        try {
	            PdfWriter writer = new PdfWriter(userHomDir + FILE_NAME);
	            PdfFont bold = PdfFontFactory.createFont(FontConstants.TIMES_BOLD);
	            PdfFont normal = PdfFontFactory.createFont(FontConstants.TIMES_ROMAN);

	            PdfDocument pdf = new PdfDocument(writer);
	            PageRotationEventHandler eventHandler = new PageRotationEventHandler();


	            pdf.addEventHandler(PdfDocumentEvent.START_PAGE, eventHandler);
	            Document document = new Document(pdf);
	            // header details
	            BasicInvoiceHeaderEventHandler handler = new BasicInvoiceHeaderEventHandler(document, invoiceHeader);
	            pdf.addEventHandler(PdfDocumentEvent.END_PAGE, handler);
	            document.setMargins(20 + handler.getTableHeight(), 36, 36, 36);






	            Table clientDetails = createClientDetails(eventDTo.getTeamName(), eventDTo.getTeamContact(), eventDTo.getTeamAddress());
	            clientDetails.setHorizontalAlignment(HorizontalAlignment.CENTER);
	            document.add(clientDetails);
	            Table invoiceDetails =  createInvoiceDetails(eventDTo, bold);
	            invoiceDetails.setWidthPercent(100);
	           
	          
	             document.add(invoiceDetails);
	            BasicInvoiceFooterEventHandler footerHandler = new BasicInvoiceFooterEventHandler(eventDTo.getPaymentDetails());
	            pdf.addEventHandler(PdfDocumentEvent.END_PAGE,footerHandler);
	            document.close();

	        } catch (Exception e) {
	                e.printStackTrace();
	                throw new DocGenException("Document Generation Error", e.getCause());

	        }
	    }


	    private Table createClientDetails(String clientName, String clientContact, String clientAddress) {

	    	Table table = new Table(2);
	         table.setWidth(400);
	         addBlankCell(table, Color.WHITE);
	         addBlankCell(table, Color.WHITE);
	         Paragraph para = new Paragraph();
	         
	         para.add(new Text(clientName+ "\n").setFontSize(7));
	         para.add(new Text(clientContact).setFontSize(7));
	         Cell cell = new Cell().add(para);
	         cell.setBorder(Border.NO_BORDER);
	         table.addCell(cell);
	         String [] address = clientAddress == null ? new String [] {""} : clientAddress.split(",");
	         para = new Paragraph();
	         for(String addrLine : address) {
	        	 para.add(new Text(addrLine+"\n").setFontSize(7));
	         }
	         cell = new Cell().add(para);	
	         cell.setBorder(Border.NO_BORDER);
	     	 table.addCell(cell);
	     	 
	     	addBlankCell(table, Color.WHITE);
	     	addBlankCell(table, Color.WHITE);
	         
			return table;
		}



		private void addRowContent(BasicInvoiceEventDTO invoiceEventDTO, Table table, PdfFont bold) {
	        boolean bgColor = false;

			Cell cell = new Cell(invoiceEventDTO.getActivities().size()+1, 1).add(new Paragraph(invoiceEventDTO.getEventName() + "(" + invoiceEventDTO.getPackageName() + ")").setFontSize(8));
			table.addCell(cell);
			for(String activity : invoiceEventDTO.getActivities()){
				String [] items = activity.split(",");
				cell = new Cell(1,1).add(new Paragraph(items[0]).setFontSize(8));
				table.addCell(cell);
				cell = new Cell(1,1).add(new Paragraph(items[1]).setFontSize(8));
				table.addCell(cell.setTextAlignment(TextAlignment.RIGHT));
			}
			cell = new Cell(1,1).add(new Paragraph("Total").setFont(bold));
			table.addCell(cell);
			cell = new Cell(1,1).add(new Paragraph(invoiceEventDTO.getTeamTotal()));
			table.addCell(cell.setTextAlignment(TextAlignment.RIGHT));
	           // bgColor =!bgColor;

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

	    private Table createInvoiceDetails(BasicInvoiceEventDTO invoiceEventDTO, PdfFont bold){
	        float [] tableScale = new float[3];
	        tableScale[0] = 1;
			tableScale[1] = 1;
			tableScale[2] = 1;
	        Table table = new Table(tableScale);
	        createTableHeader(table, bold);
	        addRowContent(invoiceEventDTO, table, bold);
	        return table;
	    }


		private void createTableHeader(Table table, PdfFont headerFont){	       	        
            table.addCell(createCell("Event & Package", 1, 1, TextAlignment.CENTER, headerFont, false, true, 10));
            table.addCell(createCell("Activities ", 1, 1, TextAlignment.CENTER, headerFont, false, true, 10));
            table.addCell(createCell("Amount ", 1, 1, TextAlignment.CENTER, headerFont, false, true, 10));
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

	    protected void addBlankCell(Table table, Color color){
	    	Cell cell = new Cell().add(new Paragraph("    ").setBorder(Border.NO_BORDER));
	    	cell.setBorder(Border.NO_BORDER);
	    	cell.setBackgroundColor(color);
	    	table.addCell(cell);
	    }
	


}
