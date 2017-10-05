package za.co.tfoldcord.docgen;

/**
 * Created by sefako on 2017/08/12.
 */
public class InvoiceColumn {

    private String name ;
    private int ratio = 1;

    public InvoiceColumn(String name, int ratio) {
        this.name = name;
        this.ratio = ratio;
    }

    public InvoiceColumn(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRatio() {
        return ratio;
    }

    public void setRatio(int ratio) {
        this.ratio = ratio;
    }
}
