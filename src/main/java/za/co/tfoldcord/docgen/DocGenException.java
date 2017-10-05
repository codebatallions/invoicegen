package za.co.tfoldcord.docgen;

/**
 * Created by sefako on 2017/08/12.
 */
public class DocGenException extends Exception {
    
	/**
	 * 
	 * @param description for document generation error
	 */
	public DocGenException(String message) {
        super(message);
    }

    public DocGenException(String message, Throwable cause) {
        super(message, cause);
    }
}
