# msusel-pique
## Introduction
This project is a fork of the QATCH project found from QuthEceSoftEng's [GitHub](https://github.com/AuthEceSoftEng/qatch) and [Website](http://softeng.issel.ee.auth.gr/).  

This fork intends to modify QATCH to behave more like a library by modularizing code, introducing maven project structure, removing GUI elements, removing main methods, and having all methods be language and tool agnostic.

Due to the major changes of intent and design, this fork renames the project to PIQUE: a *Platform for Investigative software Quality Understanding and Evaluation*.
QATCH legacy build, config, rulesets, and default model files are left in an archive folder.

PIQUE is a collection of library functions and runner entry points designed to support experimental software quality analysis from a language-agnostic perspective.
To remain language-agnostic, this project provides the abstractions, interfaces, and algorithms necessary for quality assessment, but leaves the task of defining language-specific static analysis operations to dependent language-specific projects that will use MSUSEL-PIQUE as a dependency.
To facilitate newcomers, this platform provides default classes for each quality assessment component to allow the platform to be used "out of the box", and for those familiar with quality assessment approaches, the platform allows each component to be overridden with experimental approaches.

Confused yet?
Apart from reading through a thesis, the best way to get started is to reference an existing, simple C# PIQUE system at [msusel-pique-csharp](https://github.com/msusel-pique/msusel-pique-csharp) which contains full examples of quality assessment using PIQUE's default mechanisms and overriding said mechanisms.

PIQUE is not yet added to the Maven central repository, so this project will need to be [built](#building) and installed (via Maven) before it can be used as a library. 
___

## Components
PIQUE provides five components that work together to achieve quality assessment: *Runner, Analysis, Calibration, Model*, and *Evaluation*.
Language specific extensions of PIQUE fulfill the interface contracts required by these components to achieve language-specific assessment without needing to invest major time into constructing a quality assessment engine.
- **Runner**: The *Runner* component utilizes the constructs of PIQUE to automate the two components necessary for quality assessment: (1) Deriving a quality model, and (2) Using that model to assess quality of a system.
- **Analysis**: The *Analysis* component provides the abstractions necessary for a language-specific PIQUE extension to instance its static analysis tools as PIQUE domain objects.
- **Calibration**: The *Calibration* component provides the abstractions necessary for two of the three components of quality model experimental design: (1) Edge weighting, and (2) Benchmarking.  Additionally, a default edge weighting concrete class and a default benchmarking concrete class are provided.
- **Model**: PIQUE assumes quality assessment will use a tree structure as its evaluation model.  The *Model* component provides the abstract objects necessary for such an assessment.  The model is instantiated via a *.json* quality model configuration file. A generic example of a model can be found in `src/test/resources/quality_models` and numerous concrete quality models can be found in the [msusel-pique-csharp](https://github.com/msusel-pique/msusel-pique-csharp) project example.
- **Evaluation**: The *Evaluation* component provides the third component necessary for quality model experimental design: (3) the algorithms and strategies used for model evaluation aggregation; specifically, normalization, aggregation, and utility functions. Additionally, this component provides default concrete evaluators for each model node type, a default normalizer, and a default utility function. 
___

## Build Environment
- Java 8+
- Maven
___
## Building
1. Ensure the [Build Environment](#build-environment) requirements are met.
1. Clone repository into `<project_root>` folder
1. Run `mvn test` from `<project_root>`. Fix test errors if needed. Errors, if they occur, will likely be from misconfiguration of R and the jsonlite library.
1. Run `mvn install` from `<project_root>`. 
msusel-pique is now available as a dependency to extend in a personal project. 
*(Eventually `mvn deploy` will be used instead.)*

___
## Running
- For project evaluation, extend the framework in your own language-specific project and call `pique.runnable.SingleProjectEvaluator.runEvaluator()`. At a minimum, the language-specific project will need to implement `IAnalyzer`, `IFindingsAggregator`, `IMetricsAggregator`, and `IFindingsResultsImporter`. Reference [msusel-pique-csharp](https://github.com/msusel-pique/msusel-pique-csharp) for numerous, simple examples.
- For an example of model derivation and quality evaluation using mocked static analysis tool results, reference `testDeriveModel()` and `testRunEvaluator()` in `src/test/java/pique/thesis_tests/ThesisTests.java`.
