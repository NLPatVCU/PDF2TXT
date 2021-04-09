import os, sys, getopt
from pathlib import Path
from haystack.file_converter.pdf import PDFToTextConverter

converter = PDFToTextConverter(remove_numeric_tables=True, valid_languages=["en"])

def file_convert(pdf_file_path):
	if pdf_file_path.endswith(".pdf"):
		doc = converter.convert(file_path=pdf_file_path)
		docStr = (str(doc))
		base = Path(pdf_file_path).stem
		f = open(base + ".txt", 'w')
		docStr = docStr.replace(r"\n", "\n")
		docStr = docStr.replace(r"\x0c", "\x0c")
		docStr = docStr.replace(r"\", 'meta': None}", "")
		docStr = docStr.replace(r"{'text': '", "")
		f.write(docStr)
		f.close

def dir_convert(directory):
	for filename in os.listdir(directory):
		if filename.endswith(".pdf"):
                	doc = converter.convert(file_path = os.path.join(directory, filename), meta=None)
                	docStr = (str(doc))
                	base = filename[:-4]
                	f = open(base + ".txt", 'w')
                	docStr = docStr.replace(r"\n", "\n")
                	docStr = docStr.replace(r"\x0c", "\x0c")
                	docStr = docStr.replace(r"\", 'meta': None}", "")
                	docStr = docStr.replace(r"{'text': '", "")
                	f.write(docStr)
                	f.close
		else:
			continue

argv = sys.argv[1:]

try:
	opts, args = getopt.getopt(argv, "f:d:o")
except:
	print("Error, not a valid option")

def main():
	if sys.argv[1] == "-f" or sys.argv[1] == "-d":
		for opt, arg in opts:
			if opt in ["-f"]:
				pdf_file_path = sys.argv[2]
				if len(sys.argv) >= 4:
					if sys.argv[3] == "-o":
						output_dir = sys.argv[4]
						os.chdir(output_dir)
					else:
						print("Error, must be: -o <output_directory_path> to change output directory")
				file_convert(pdf_file_path)
			elif opt in ["-d"]:
				directory = sys.argv[2]
				if len(sys.argv) >= 4:
					if sys.argv[3] == "-o":
						output_dir = sys.argv[4]
						os.chdir(output_dir)
					else:
						print("Error, must be: -o <output_directory_path> to change output directory")
				dir_convert(directory)
	else:
		print("Must specify either -f for a single file or -d for a directory")

if __name__ == '__main__':
        main()
