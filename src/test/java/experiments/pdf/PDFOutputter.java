package experiments.pdf;

import com.google.gson.annotations.Expose;

public class PDFOutputter {

    @Expose
    private PDFTreatment treatment;
    @Expose
    private PDFResponse response;

    public PDFOutputter(PDFTreatment treatment, PDFResponse response){
        this.treatment = treatment;
        this.response = response;
    }
}
