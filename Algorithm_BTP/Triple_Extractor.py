from collections import defaultdict
import json

def extract_triples(triple_file, pos_file):
    triples_map = defaultdict(list)
    with open(triple_file, 'r') as f_triple, open(pos_file, 'r') as f_pos:
        triple_lines = f_triple.readlines()
        pos_lines = f_pos.readlines()
        
        prev = 1
        for triple_line in triple_lines:
            triple_parts = triple_line.strip().split("||")
            sentence_number = int(triple_parts[0])
            triple_data = eval(triple_parts[1])  # safely evaluate the triple data
            

            newtriple = []

            if len(triple_data) >= 3:  # Check if triple_data has at least three elements
                if sentence_number <= len(pos_lines):
                    # sentence = pos_lines[sentence_number - 1].strip(',')
                    extractsentence = pos_lines[sentence_number - 1].replace('[', '').replace(']', '').replace(',', '')  # Remove square brackets and commas from the sentence
                    elements = extractsentence.split()  # Split the sentence into individual elements
                    # print(extractsentence)
                    # Create a list to store the elements
                    sentence= [element.strip() for element in elements]
                    # Check if the entire subject and object are present in the sentence
                    if any(word in sentence for word in triple_data[0]) and any(word in sentence for word in triple_data[2]):
                        newtriple.append(triple_data)
            
            if newtriple:
                triples_map[sentence_number].append(triple_data)
            prev = sentence_number
            
    return triples_map

# Example usage:
# Assuming 'triples' is a Python dictionary
# List of tool names and input file names
tools_and_files = [
    ("triple_clauseie", "triple_clauseie.txt"),
    ("triple_minie", "triple_minie.txt"),
    ("stanford_4.5.3_openie", "stanford_4.5.3_openie.txt"),
    ("stanford_4.5.6_openie", "stanford_4.5.6_openie.txt")
]

# Loop over each tool and input file
for tool, file in tools_and_files:
    # Call extract_triples function with the specified tool and input file
    triples = extract_triples( file, "output_pos.txt")
    
    # Save the extracted triples to a JSON file
    output_file = f"{tool}.json"
    with open(output_file, 'w') as json_file:
        json.dump(triples, json_file)

    print(f"Triples extracted by {tool} saved to {output_file}")
# for sentence_number, triples_list in triples.items():
#     print(f"{sentence_number} -> {triples_list}")
