package experiments.pdf;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
public class PDFTreatment {

    private BigDecimal[] thresholds;
    private BigDecimal[] evaluationDomain;
    private PDFUtils.KernelFunction kernelFunction;
    private double bandwidth;

    public PDFTreatment(BigDecimal[] thresholds, BigDecimal[] evaluationDomain, PDFUtils.KernelFunction kernelFunction, double bandwidth) {
        this.thresholds = thresholds;
        this.evaluationDomain = evaluationDomain;
        this.kernelFunction = kernelFunction;
        this.bandwidth = bandwidth;
    }
}
