#
# MIT License
# Copyright (c) 2019 Montana State University Software Engineering Labs
#
# Permission is hereby granted, free of charge, to any person obtaining a copy
# of this software and associated documentation files (the "Software"), to deal
# in the Software without restriction, including without limitation the rights
# to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
# copies of the Software, and to permit persons to whom the Software is
# furnished to do so, subject to the following conditions:
#
# The above copyright notice and this permission notice shall be included in all
# copies or substantial portions of the Software.
#
# THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
# IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
# FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
# AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
# LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
# OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
# SOFTWARE.
#

import json
import matplotlib.pyplot as plt
import seaborn as sns
from pathlib import Path
import pandas as pd

def bandwidthOverTime():
    title = "Scatter plot of Bandwidth over Time to run (ms)"
    xColumnHeader = "time_to_run_ms"
    yColumnHeader = "bandwidth"

    sns.scatterplot(x=xColumnHeader, y=yColumnHeader, data=data, hue="kernel_function")
    plt.title(title)
    plt.show()

def allPairwiseOverTime():
    title = "all pairwise relationships"
    print(data.columns)
    sns.pairplot(vars=["evaluation_domain_generation_count",
                          "bandwidth",
                          "max_AUC",
                          "time_to_run_ms"], data=data, hue="kernel_function")

    plt.title(title)
    plt.savefig(str(Path(__file__).resolve().parents[3]) + "/out/pdf_analysis/pairwise.png")
    plt.show()

def parse_data():
    parent_path = Path(__file__).resolve().parents[3]
    json_path = Path(str(parent_path) + "/out/pdf_output_jsons")
    json_list = list(json_path.iterdir())
    as_list = []
    for json_file_path in json_list:
        with open(json_file_path) as json_file:
            values = json.load(json_file)
        as_list.append(collapse_values(values))
    global data
    data = pd.DataFrame(as_list)

def collapse_values(values):
    data = {
        'UUID': values['treatment']['uuid'],
        'threshold_generation_strategy': values['treatment']['thresholdGeneration']['generationStrategy'],
        'threshold_generation_begin_index': values['treatment']['thresholdGeneration']['beginIndex'],
        'threshold_generation_end_index': values['treatment']['thresholdGeneration']['endIndex'],
        'threshold_generation_count': values['treatment']['thresholdGeneration']['count'],
        'evaluation_domain_generation_strategy': values['treatment']['evaluationDomainGeneration']['generationStrategy'],
        'evaluation_domain_generation_begin_index': values['treatment']['evaluationDomainGeneration']['beginIndex'],
        'evaluation_domain_generation_end_index': values['treatment']['evaluationDomainGeneration']['endIndex'],
        'evaluation_domain_generation_count': values['treatment']['evaluationDomainGeneration']['count'],
        'kernel_function': values['treatment']['kernelFunction'],
        'bandwidth': values['treatment']['bandwidth'],
        'max_AUC': values['response']['maxAUC'],
        'mean_transition_value': values['response']['transitionValueSummaryStatistics']['mean'],
        'max_transition_value': values['response']['transitionValueSummaryStatistics']['max'],
        'min_transition_value': values['response']['transitionValueSummaryStatistics']['min'],
        'std_transition_value': values['response']['transitionValueSummaryStatistics']['std'],
        'time_to_run_ms': values['response']['timeToRunMS']
    }
    return data

def main():
    parse_data()
    bandwidthOverTime()
    allPairwiseOverTime()

if __name__ == "__main__":
    main()