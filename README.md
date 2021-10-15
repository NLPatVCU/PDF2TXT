# PDF2TXT

PDF2TXT can be used to either convert a single .pdf file to a .txt file or all .pdf files in a given directory to .txt files.

![alt text](https://nlp.cs.vcu.edu/images/Edit_NanomedicineDatabase.png "Nanoinformatics")

Installation
============
To install PDF2TXT:
```python
git clone https://github.com/NLPatVCU/PDF2TXT.git
```
You would also need to install the Haystack framework
```python
pip install farm-haystack
```
When running PDF2TXT in a python3 venv, install haystack using
'''python
pip3 install farm-haystack
'''

If you experience any difficulties, try visiting their site: https://github.com/deepset-ai/haystack

Use
===
When running PDF2TXT in a python3 venv, all commands must be preceded by "python3"

To convert a single file, run:
```python
pdf2txt.py -f <input_file_path>
```

To convert an entire directory, run:
```python
pdf2txt.py -d <input_directory_path>
```
To write output files into a specific directory, append with:
```python
-o <output_directory_path>
```
License
=======
This package is licensed under the GNU General Public License

Acknowledgments
===============
- [VCU Natural Language Processing Lab](https://nlp.cs.vcu.edu/)     ![alt text](https://nlp.cs.vcu.edu/images/vcu_head_logo "VCU")
- [Nanoinformatics Vertically Integrated Projects](https://rampages.us/nanoinformatics/)
