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