package experiments.pdf;


import com.google.gson.annotations.Expose;
import lombok.Getter;
import pique.utility.PDFUtils;

@Getter
public class GenerationData {
    @Expose
    private PDFUtils.GenerationStrategy generationStrategy;
    @Expose
    private int beginIndex;
    @Expose
    private int endIndex;
    @Expose
    private int count;

    public GenerationData(PDFUtils.GenerationStrategy generationStrategy, int beginIndex, int endIndex, int count) {
        this.generationStrategy = generationStrategy;
        this.beginIndex = beginIndex;
        this.endIndex = endIndex;
        this.count = count;
    }
}
