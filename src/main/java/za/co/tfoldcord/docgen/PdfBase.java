package za.co.tfoldcord.docgen;

import java.util.List;
import java.util.Map;

import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.color.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.pdf.PdfNumber;

public class PdfBase {
	protected  Map<HeaderDetails, String> headerDetails;
	protected  Map<FooterDetails, String> footerDetails;
	protected  List<InvoiceColumn> invoiceColumns;
	protected static Color rowColor = new DeviceRgb(242, 242, 242);
	protected PdfFont normal = null;
	protected PdfFont bold = null;
	protected static final PdfNumber PORTRAIT = new PdfNumber(0);
	protected static final PdfNumber LANDSCAPE = new PdfNumber(90);
	protected static final PdfNumber INVERTEDPORTRAIT = new PdfNumber(180);
	protected static final PdfNumber SEASCAPE = new PdfNumber(270);
	

	

}
