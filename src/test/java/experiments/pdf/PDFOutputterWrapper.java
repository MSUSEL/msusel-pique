package experiments.pdf;

import com.google.gson.annotations.Expose;
import lombok.Getter;


@Getter
public class PDFOutputterWrapper {

    @Expose
    private PDFTreatment treatment;
    @Expose
    private PDFResponse response;

    public PDFOutputterWrapper(PDFTreatment treatment, PDFResponse response){
        this.treatment = treatment;
        this.response = response;
    }

}
