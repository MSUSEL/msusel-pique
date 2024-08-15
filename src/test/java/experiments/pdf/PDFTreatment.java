package experiments.pdf;

import com.google.gson.annotations.Expose;
import lombok.Getter;
import pique.utility.PDFUtils;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
public class PDFTreatment {

    @Expose
    private UUID uuid;
    @Expose (serialize = false)
    private BigDecimal[] thresholds;
    @Expose (serialize = false)
    private BigDecimal[] evaluationDomain;
    @Expose
    private GenerationData thresholdGeneration;
    @Expose
    private GenerationData evaluationDomainGeneration;
    @Expose
    private PDFUtils.KernelFunction kernelFunction;
    @Expose
    private double bandwidth;

    public PDFTreatment(GenerationData thresholdGeneration, GenerationData evaluationDomainGeneration, PDFUtils.KernelFunction kernelFunction, double bandwidth) {
        uuid = UUID.randomUUID();
        this.kernelFunction = kernelFunction;
        this.bandwidth = bandwidth;
        this.thresholdGeneration = thresholdGeneration;
        this.thresholds = thresholdGeneration.getGenerationStrategy().generateValues(thresholdGeneration.getBeginIndex(), thresholdGeneration.getEndIndex(), thresholdGeneration.getCount());
        this.evaluationDomainGeneration = evaluationDomainGeneration;
        this.evaluationDomain = evaluationDomainGeneration.getGenerationStrategy().generateValues(evaluationDomainGeneration.getBeginIndex(), evaluationDomainGeneration.getEndIndex(), evaluationDomainGeneration.getCount());
    }


}
