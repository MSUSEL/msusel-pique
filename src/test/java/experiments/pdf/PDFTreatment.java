/**
 * MIT License
 * Copyright (c) 2019 Montana State University Software Engineering Labs
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
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
