import setuptools

def readme():
    with open('README.md') as f:
        return f.read()

setuptools.setup(
    name='pdf2txt',
    version='0.1',
    description='Converts a pdf document to text',
    long_description=readme(),
    packages=setuptools.find_packages(),
    url='https://github.com/NLPatVCU/PDF2TXT',
    author="Shekinah Veeravalli, Bridget McInnes",
    author_email='veeravalliss@vcu.edu',
    classifiers=[
        'Development Status :: 4 - Beta',
        'License :: OSI Approved :: GNU General Public License (GPL)',
        'Programming Language :: Python :: 3.5',
        'Natural Language :: English',
        'Topic :: Text Processing :: Linguistic',
        'Intended Audience :: Science/Research'
    ],
    install_requires=[
     'farm-haystack @ git+https://github.com/deepset-ai/haystack.git@v0.7.0'
    ],
)

