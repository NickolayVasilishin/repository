"""
    This script prints out test classes names of tests which were runned.
    It requires test-plan file (.jmx) and exported summary from jmeter
"""

import xml.etree.ElementTree as ET
from operator import itemgetter

labels_file = "summary.csv"
testplan_file = "SupportServiceTestPlanLoad.jmx"
enc = "UTF-8"

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

def printAll(classnames, labels):
    for key in sorted(classnames, key=itemgetter(1)):
        output = "{0:50s} : {1:140s} : {2:3.0f}% errors"
        print(output.format(key, classnames[key], labels[key]))

def printAllAsTable(classnames, labels):
    print("||(?)||Label in jmeter||Class Name||errors||status||Решение||")
    for key in sorted(classnames, key=itemgetter(1)):
        output = "|{0}|{1}|{2:3.0f}% errors|||"
        if labels[key] == 100:
            output = "|(x)|" + output
        elif labels[key] > 30:
            output = "|(!)|" + output
        else:
            output = "|(/)|" + output
        print(output.format(key, classnames[key], labels[key]))

def printNameAndClass(classnames):
    for key in classnames:
        output = "{0:40s} : {1:40s}"
        print(output.format(key, classnames[key].split(".")[-1]))

printAll(getClassNames(getLabels(labels_file), testplan_file), getLabels(labels_file))
print("\n\n\n\n")
'''printNameAndClass(getClassNames(getLabels(labels_file), testplan_file))
print("\n\n\n\n")'''
printAllAsTable(getClassNames(getLabels(labels_file), testplan_file), getLabels(labels_file))
