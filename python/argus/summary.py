import os
import xml.etree.ElementTree as ET
from multiprocessing import Pool
from collections import defaultdict
from operator import itemgetter

"""Collects all sample data table*.xml in dictionary.
    format: { "LABEL":["FULL TEST CLASS AND METHOD NAME" , ("TIME", "TIME PER TEST", "SUCCEEDED true/false", "EXCEPTION"), ... , .......}
"""

summarysource = r"B:\programmersmount\jmeter\n.vasilishin\reports\2015-11-09 - control\jmeter\tables"

enc = "UTF-8"
FIELD_TIME = 0
FIELD_SAMPLETIME = 1
FIELD_SUCCEDED = 2
FIELD_EXCEPTION = 3

#gets summary from file
def getSummary(filename):
    print("thread")
    samplelist = defaultdict(list)
    with open(filename , encoding=enc) as samples:
        root = ET.parse(samples).getroot()
        for sample in root:
            attributes = []
            attributes.append(sample.attrib["ts"])
            attributes.append(sample.attrib["t"])
            attributes.append(sample.attrib["s"])
            try:
                attributes.append(sample[1].text.split('\n', 1)[0])
            except AttributeError:
                #attributes.append("")
                pass
            if sample.attrib["lb"] not in samplelist:
                samplelist[sample.attrib["lb"]].append(sample[-1].text)
            samplelist[sample.attrib["lb"]].append(tuple(attributes))
    return samplelist

#parses table*.xml files in directory by 4 threads
def mapAllSummary(directory, procs = 4):
    filelist = []
    for subdir, dirs, files in os.walk(directory):
        for file in files:
            if len(file.split(".")) == 2 and file.split(".")[1] == "xml":
                filelist.append(os.path.join(subdir,file))
    pool = Pool(processes=procs)
    return pool.map(getSummary, filelist)

#reduces all dictionaries in list into one
def reduceSummary(summaries):
    for key in summaries[0]:
        for other in summaries[1:]:
            if key in other:
                for item in other[key]:
                    summaries[0][key].append(item)
    return summaries[0]

def calculateSuccessRatio(summary):
    ratio = defaultdict(list)
    for key in summary:
        for sample in summary[key][1:]:
            if key not in ratio:
                ratio[key]=[0,1]
            if sample[2] == 'true':
                 ratio[key][0] += 1
            ratio[key][1] += 1

    for each in ratio:
        ratio[each] = ratio[each][0]/ratio[each][1]
    return ratio
          
if __name__ == '__main__':
    summary = reduceSummary(mapAllSummary(summarysource))
    rate = calculateSuccessRatio(summary)
    l = []
    for key in rate:
        l.append([key, rate[key]])
    for s in sorted(l, key = itemgetter(1)):
        print(s, sep="\n")
    #print(summary["searchServices"][2])
