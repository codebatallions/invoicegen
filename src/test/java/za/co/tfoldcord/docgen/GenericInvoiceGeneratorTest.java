package za.co.tfoldcord.docgen;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Created by sefako on 2017/08/13.
 */

@RunWith(JUnitPlatform.class)
public class GenericInvoiceGeneratorTest {
    private List<InvoiceColumn> invoiceColumns;
    private Map<HeaderDetails, String> headerDetails;
    private Map<FooterDetails, String> footerDetails;
    private String [][] rowData;
    GenericInvoiceGenerator generator ;

    @Test
    @DisplayName("Test Generate Invoice File on users home directory")
    public void generateInvoiceBytes() {
        try{
            generator.generateInvoiceFile(rowData);
        }catch(Exception e){

        }
    }

    @BeforeEach
    public void prepareTestData() throws IOException {
        // setup headers
        Properties headers = getProperties("headers.properties");
        Properties footers = getProperties("footers.properties");
        headerDetails = new HashMap<>();
        footerDetails = new HashMap<>();
        for(HeaderDetails details : HeaderDetails.values()){
            headerDetails.put(details, (String)headers.get(details.name()));
        }
       // headerDetails.forEach((k,v)->System.out.println("HKey : " + k + "HValue : " + v));
        // setup footer
        for(FooterDetails footie : FooterDetails.values()){
            footerDetails.put(footie, (String)footers.get(footie.name()));
        }
       // footerDetails.forEach((k,v)->System.out.println("FKey : " + k + "FValue : " + v));
        // setup column names and ratio
        File data = getFile("invoice.csv");

        List<String> rows = fileLines(data);
        invoiceColumns = new ArrayList<>();
        // header row
        String names = rows.get(0);
        String [] tokenized = names.split(",");
        for(String columnLabel: tokenized){
            invoiceColumns.add(new InvoiceColumn(columnLabel, 1));
        }
        // row Data
        rowData = new String[rows.size()][invoiceColumns.size()];
        for(int i = 1; i< rows.size(); i++){

            String [] reportData = rows.get(i).split(",");
                for(String col : reportData) {
                    System.out.print(col + " \t");
                }
            System.out.println( "]");
            for(int columnIndex = 0; columnIndex < reportData.length; columnIndex++){
                rowData[i-1][columnIndex] = reportData[columnIndex];
            }
        }

         generator = new GenericInvoiceGenerator(invoiceColumns, headerDetails, footerDetails);
    }

    private File getFile(String filename) {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(filename).getFile());

        return file;
    }
    private List<String> fileLines(File file) {
        List<String> fileLines = new ArrayList<>();
        try (Scanner scanner = new Scanner(file)) {

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                    fileLines.add(line);
            }

            scanner.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return fileLines;
    }

    private Properties getProperties(String filename) throws IOException {
        Properties props = new Properties();
        InputStream inputStream;
        inputStream = getClass().getClassLoader().getResourceAsStream(filename);

        if (inputStream != null) {
            props.load(inputStream);
        } else {
            throw new FileNotFoundException("property file '" + filename + "' not found in the classpath");
        }
        return props;
    }
}
