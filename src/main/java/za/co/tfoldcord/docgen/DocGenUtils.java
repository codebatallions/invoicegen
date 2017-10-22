package za.co.tfoldcord.docgen;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DocGenUtils {
	
	protected static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	
	public static String nowDate() {
		return LocalDate.now().format(formatter);
	}

}
