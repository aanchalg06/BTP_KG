import spacy

# Load the English language model
nlp = spacy.load('en_core_web_sm')

def extract_and_save_noun_phrases(input_file_path, output_file_path):
    # Open input file for reading
    with open(input_file_path, 'r', encoding='utf-8') as input_file:
        # Open output file for writing
        with open(output_file_path, 'w', encoding='utf-8') as output_file:
            # Process each line (sentence) in the input file
            for line_number, line in enumerate(input_file, start=1):
                # Strip any leading/trailing whitespace and newline characters
                line = line.strip()
                
                # Process the sentence using spaCy
                doc = nlp(line)
                
                # Extract noun phrases
                noun_phrases = []
                for chunk in doc.noun_chunks:
                    noun_phrases.append(chunk.text)
                
                # Format the output line with line number and noun phrases
                output_line = f"[{', '.join(noun_phrases)}]\n"
                
                # Write the formatted line to the output file
                output_file.write(output_line)

# Example usage:
input_file_path = 'sentences_input.txt'
output_file_path = 'output_pos.txt'

# Extract and save noun phrases from each line in the input file
extract_and_save_noun_phrases(input_file_path, output_file_path)
