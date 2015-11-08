import os
import xml.etree.ElementTree as ET
import re

"JTL???"

samplelistdir = "B:\\programmersmount\\jmeter\\n.vasilishin\\reports\\2015-11-02\\jmeter\\samples"
enc = "UTF-8"
samplepattern = re.compile('<sample(?:.*)lb="([a-z]*)"(?:.*)>', re.IGNORECASE)

def getRoot(jmxfile):
    with open(jmxfile, encoding=enc) as jmxfile:
        return ET.parse(jmxfile).getroot()

try:
    for subdir, dirs, files in os.walk(samplelistdir):
            causes = {}
            for file in files:
                label = ""
                for line in open(os.path.join(subdir, file), encoding=enc):
                    if samplepattern.match(line):
                        label = samplepattern.match(line).group(1)
                        if label in causes:
                            pass
                        else:
                            causes[label] = []
                    if line.startswith("Caused by:"):
                        causes[label].append(line)
except Exception:
    print(line, file)

#for k in causes.keys():
#    print(k, causes[k], sep=" : ")

interrupt = False
while(not interrupt):
    try:
        print(causes[input("Label: ")])
    except KeyError:
        if input("Wrong label. Type 'e' to exit\n") == "e":
            interrupt = True
