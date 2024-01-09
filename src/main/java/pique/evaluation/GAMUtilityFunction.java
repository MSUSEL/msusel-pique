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
package pique.evaluation;

import com.github.rcaller.TempFileService;
import com.github.rcaller.graphics.GraphicsType;
import com.github.rcaller.rstuff.RCaller;
import com.github.rcaller.rstuff.RCallerOptions;
import com.github.rcaller.rstuff.RCode;
import com.github.rcaller.util.Globals;
import org.apache.poi.util.TempFile;
import pique.utility.BigDecimalWithContext;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Stream;

public class GAMUtilityFunction extends UtilityFunction {

    public GAMUtilityFunction(){
        super("pique.evaluation.GAMUtilityFunction", "Utility function based on a Generalized Additive Model.");
    }


    public GAMUtilityFunction(String name, String description) {
        super(name, description);
    }

    /**
     * Apply a utility function given an input value and thresholds
     * Nearly all this R code was built by Ann Marie (Thank you so much for your R expertise!!!) and is available for step-by-step tutorial in docs/BenchmakrInstabil_v0.2.html
     *
     * @param inValue    The value before applying the utility functions
     * @param values For the GAM utility function the values will be the entirety of the finding from the derivation step
     * @param positive   Whether the input value has a positive or negative effect on quality
     * @return The value of inValue after applying the utility function
     */
    @Override
    public BigDecimal utilityFunction(BigDecimal inValue, BigDecimal[] values, boolean positive) {
        RCaller rCaller = RCaller.create();
        RCode code = RCode.create();

        code.addRCode("library(mgcv)");
        code.addRCode("");

        //convert to primitive double
        double[] input = Stream.of(values).mapToDouble(BigDecimal::doubleValue).toArray();
        //add to R as findings vector
        code.addDoubleArray("findings", input);

        double valuesMin = Collections.min(Arrays.asList(values)).doubleValue();
        double valuesMax = Collections.max(Arrays.asList(values)).doubleValue();

        double bucketSize = 50;

        double stepSize = (valuesMax-valuesMin)/bucketSize;

        //build histogram from min and max and stepsize
        String histBuilder = "myHist <- hist(findings, breaks = seq("+valuesMin+","+valuesMax+","+stepSize+"))";

        //build histogram
        code.addRCode(histBuilder);

        //calculate densities (used in GAM)
        code.addRCode("densities <- diff( range(myHist$breaks), 1)/length(myHist$counts) * myHist$density");

        //I think wee only need this if this is a Diagnostic between 0,1, but idk for sure --- YES, I am pretty sure this is the case
        // and I need to run/build the ReScale function if this is the case.
        //TODO add logic that identifies if we have a finding between 0,1 and scale with rescale if so. We do not need to do this if the finding is NOT between 0,1
        //linearize the densities between [0,1]...where density is high, we want a bigger Diagnostic Value
        //code.addRCode("densities <- reScale(densities, 0, 1)");

        code.addRCode("scores <- myHist$mids");

        //second findings IS wrong...
        code.addRCode("gam_out <- gam(densities ~s(scores))");

        //can print summary if we need it, because this will be programmatic I will ignore it.
        //code.addRCode("summary(gam_out)");







        try {
            //super hack to get better image resolution. code is literally startPlot() expanded, available here:
            // https://github.com/jbytecode/rcaller/blob/master/RCaller/src/main/java/com/github/rcaller/rstuff/RCode.java
            //I just copy-pasted the code with different png params to make larger images. lol.


            File f = this.plotSize(GraphicsType.png, code);
            System.out.println("Plot will be saved to: " + f);
            code.addRCode("par(mfrow = c(1,2))");
            code.addRCode("plot(scores,fitted(gam_out),type=\"l\",ylab=\"Density of Findings\",xlab=\"Finding Result\")");
            code.addRCode("plot(myHist, xlab=\"Number of Findings\")");
            code.addRCode("dev.off()");



        }catch (Exception e){
            e.printStackTrace();
        }

        code.addRCode("gam_array <- predict(gam_out,newdata=list(scores = c("+inValue.doubleValue()+")))");
        //gam is an array automatically and RCaller cannot return arrays for some reason, so we just turn it into a single numeric value by selecting the first element
        code.addRCode("gam_predict <- gam_array[1]");


        //necessary because AFAIK Java can't dig into the R environment, we have to return the environment as a var and mine the var.. I think, idk.
        code.addRCode("result <- as.list(.GlobalEnv)");

        //set R code to run from the R caller
        //System.out.println(code);
        rCaller.setRCode(code);

        //result is a snapshot of the R enviroment, need to mind it for gam output.
        rCaller.runAndReturnResult("result");

        //mining it for gam_output
        double toRet = rCaller.getParser().getAsDoubleArray("gam_predict")[0];

        return new BigDecimalWithContext(toRet);
    }

    private File plotSize(GraphicsType type, RCode code) throws IOException {
        TempFileService tempFileService = new TempFileService();
        //File f = File.createTempFile("RPlot", "." + type.name());
        File f = tempFileService.createTempFile("RPlot", "." + type.name());
        switch (type) {
            case png:
                code.addRCode("png(\"" + f.toString().replace("\\", "/") + "\",width=1280,height=720)");
                break;
            case jpeg:
                code.addRCode("jpeg(\"" + f.toString().replace("\\", "/") + "\")");
                break;
            case tiff:
                code.addRCode("tiff(\"" + f.toString().replace("\\", "/") + "\")");
                break;
            case bmp:
                code.addRCode("bmp(\"" + f.toString().replace("\\", "/") + "\")");
                break;
            default:
                code.addRCode("png(\"" + f.toString().replace("\\", "/") + "\",width=1280,height=720)");
        }
        code.addRCode(Globals.theme.generateRCode());
        return (f);
    }

    public BigDecimal reScale(){
        //TODO
        return null;
    }

}
