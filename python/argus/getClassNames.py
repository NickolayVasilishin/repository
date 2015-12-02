"""
    This script prints out test classes names of tests which were runned.
    It requires test-plan file (.jmx) and exported summary from jmeter
"""

import xml.etree.ElementTree as ET
from operator import itemgetter
import os

labels_file = "summaries/summary{}.csv".format(4)
testplan_file = "testplan.jmx"
labels_dir = r"B:\programmersmount\jmeter\n.vasilishin\reports\2015-11-10\jmeter\summaries"
enc = "UTF-8"

def getAllLabels(directory):
    labels = {}
    summaries = []
    for subdir, dirs, files in os.walk(directory):
        for file in files:
            summaries.append(getLabels(os.path.join(subdir, file)))

    for summary in summaries:
        for item in summary:
            if item not in labels:
                labels[item] = [summary[item]]
            else:
                labels[item].append(summary[item])
    for label in labels:
        labels[label] = sum(labels[label])/len(labels[label])
    return labels

def getLabels(file):
    labels = {}
    with open(file, encoding=enc) as label_file:
        for line in label_file:
            if line.split(",")[0] == "sampler_label" or line.split(",")[0] == "TOTAL":
                continue
            labels[line.split(',')[0]] = float(line.split(',')[6])*100
    return labels

def getClassNames(labels, file, withMethods = False):
    classnames = {}
    with open(file, encoding=enc) as jmxfile:
       root = ET.parse(jmxfile).getroot()
       nodes = root.findall(".//JUnitSampler[@testname]")
       for node in nodes:
           if node.attrib["testname"] in labels:
               if withMethods:
                   classnames[node.attrib["testname"]] = "" + node[0].text + "#" + node[2].text
               else:
                   classnames[node.attrib["testname"]] = node[0].text
    return classnames

def getStatistics(classnames, labels, columnToSort = 1):
    statistics = []
    for label in sorted(labels):
        statistics.append((label, classnames[label], labels[label]))
    return tuple(sorted(statistics, key = itemgetter(columnToSort)))
            
def printAllStatistics(statistics):
    output = "{0:50s} : {1:140s} : {2:3.0f}% errors"
    for line in statistics:
        print(output.format(line[0], line[1], line[2]))

def printAllStatisticsAsTable(statistics):
    print("||(?)||Label in jmeter||Class Name||errors||status||Решение||")
    output = "|{0}|{1}|{2:3.0f}% errors|||"
    for line in statistics:
        output = "|{0}|{1}|{2:3.0f}% errors|||"
        if line[2] == 100:
            output = "|(x)|" + output
        elif line[2] > 30:
            output = "|(!)|" + output
        else:
            output = "|(/)|" + output
        print(output.format(line[0], line[1], line[2]))

def printNameAndClass(classnames):
    for key in classnames:
        output = "{0:40s} : {1:40s}"
        print(output.format(key, classnames[key].split(".")[-1]))

print("\n\n\n\n")
#statistics = getStatistics(getClassNames(getLabels(labels_file), testplan_file), getLabels(labels_file))
statisticsAll = getStatistics(getClassNames(getAllLabels(labels_dir), testplan_file, False), getAllLabels(labels_dir), 2)
printAllStatistics(statisticsAll)
print("\n\n\n\n")
printAllStatisticsAsTable(statisticsAll)
