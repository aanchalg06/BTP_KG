OpenIE Triple Extraction Pipeline

This project extracts noun phrases from sentences, uses them to filter Open Information Extraction (OIE) triples, and finally processes the filtered triples with a Java program.

Prerequisites

Before you begin, ensure you have the following installed:
* Python 3
* Java (JDK and JRE)
* spaCy Python library: pip install spacy
* spaCy English Model: python -m spacy download en_core_web_sm
* json-simple-1.1.1.jar: Download this file and place it in the root directory.

Project Structure

* POS.py: Python script to extract noun phrases using spaCy.
* Triple_Extractor.py: Python script to filter triples based on noun phrases.
* Main.java: Java program to process the final JSON files.
* run.sh: Bash script to run the main extraction and Java processing pipeline.
* sentences_input.txt: (Manual Input) You must create this file. Each line should contain one sentence.
* *.txt (e.g., triple_clauseie.txt, triple_minie.txt): (Manual Input) These are the raw OIE triple files required by Triple_Extractor.py.
* Json/: (Manual Input) You must create this folder and place the required .json "tools" file inside it, which Main.java will read.

Execution Steps

Follow these steps in order to run the full pipeline.

1. Prepare Input Files

1.  Create sentences_input.txt in the root directory and add your sentences (one per line).
2.  Place your raw OIE triple files in the root directory (e.g., triple_clauseie.txt, triple_minie.txt, stanford_4.5.3_openie.txt, stanford_4.5.6_openie.txt).
3.  Create a folder named Json.
4.  Place the required .json file that Main.java needs to read inside the Json folder.

2. Step 1: Generate Noun Phrases

First, run the POS.py script to process your sentences and generate the noun phrase file.

python POS.py

* Input: sentences_input.txt
* Output: output_pos.txt

3. Step 2: Run the Main Pipeline

After output_pos.txt has been generated, run the run.sh script.

./run.sh

This script will perform the following actions:

1.  Run Triple_Extractor.py:
    * Input: output_pos.txt and the various *.txt triple files.
    * Output: Generates corresponding *.json files (e.g., triple_clauseie.json, triple_minie.json, etc.).
2.  Compile Main.java:
    * Compiles the Java code using the json-simple library.
3.  Run Main.java:
    * Executes the compiled Java program.
    * Input: Reads the .json file you manually placed in the Json/ folder.
