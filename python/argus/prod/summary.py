import os
import xml.etree.ElementTree as ET
#from multiprocessing import Pool
from multiprocessing.dummy import Pool
from collections import defaultdict
from operator import itemgetter

from validate import validate
from common_statistics import getPagePerMinute

"""Collects all sample data table*.xml in dictionary.
    format: { "LABEL":["FULL TEST CLASS AND METHOD NAME" , ("TIME", "TIME PER TEST", "SUCCEEDED true/false", "EXCEPTION"), ... , .......}

    If parsing fails, uncomment multiprocessing.dummy import to see, which file is corrupted.
"""

name = "Василишин" 
target = "Цель" 
resume = "Итог" 
testplan = "SupportServiceTestPlanLoad.jmx" 
environment = "jboss14, v---" 
target_spm_per_worker = []
total_spm = None
sample_error = "se"
report_idle = "idle"
get_good_page_pm = " "
msc_count_pm = "msc-1" #
msc_time_pm = "msc-2" #
sessions = "sess" #
cores = 2 #
requests_per_core = "reqperc"
ram = "5 gb" #
heap_total = "3000" #
eden = "e" #
eden_heap = "eh" # 
old_gen = "og" #
const_mem = "cm" #
empty_field = "-"

reports_directory = r"B:/programmersmount/jmeter/n.vasilishin/reports/"
host_path = r"/host"
idle_csv = r"/jconsole/cpu.csv"
eden_csv = r"/jconsole/eden.csv"
old_gen_csv = r"/jconsole/old_gen.csv"
html_statistics_path = r"/host/accesslog-reports"
summary_path = r"/jmeter/tables"

tmstmp = None

summarysource = r"\\FILESERVER3\Artifacts\Нагрузочное тестирование\ver_3.9.1\2015.11.15 14_00 2015.11.16 13_44\jmeter\tables"

enc = "UTF-8"
FIELD_TIME = 0
FIELD_SAMPLETIME = 1
FIELD_SUCCEDED = 2
FIELD_EXCEPTION = 3

quick_summary_template = "{0:50s} : {1:140s} : {2:3.0f}% errors"
summary_table_header = "||(?)||Label in jmeter||Class Name||errors||"
summary_table_row = "|{0}|{1}|{2:3.0f}% errors|||"

ignored_tests = ["incidentCancellationScenario", "incidentForSelectedService", "incidentForSelectedServiceScenario", "incidentWithPhoneScenarioIT"]
supportservice_tests = ["incidentCancellationScenario", "incidentForSelectedService", "incidentForSelectedServiceScenario", "incidentWithPhoneScenarioIT", "incidentWithVisitScenarioIT"]

#gets summary from file
#==TODO== remove tmstmp
def getSummary(filename):
    samplelist = defaultdict(list)
    with open(filename , encoding=enc) as samples:
        try:
            root = ET.parse(samples).getroot()
        except ET.ParseError as error:
            print("Failed to parse file " + filename)
            print(error)
        for sample in root:
            if tmstmp and int(sample.attrib["ts"]) > tmstmp:
                return samplelist
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
    #format: {label:[test, (ts, t, success, err)..., ...}
    return samplelist

#parses table*.xml files in directory by 4 threads
#calculates samples per minute for worker
def mapAllSummary(directory, procs = 4):
    if tmstmp:
        print("Warning: all records after " + str(tmstmp) + " will be ignored")
    filelist = []
    for subdir, dirs, files in os.walk(directory):
        for file in files:
            if len(file.split(".")) == 2 and file.split(".")[1] == "xml":
                filelist.append(os.path.join(subdir,file))
    pool = Pool(processes=procs)
    summaries = list(pool.map(getSummary, filelist))
    map(t, ["a","b","c"])
    list(map(calculateSamplesPerMinute, summaries))
    return summaries

#reduces all dictionaries in list into one
def reduceSummary(summaries):
    for key in summaries[0]:
        for other in summaries[1:]:
            if key in other:
                for item in other[key]:
                    #do not add the same test class names
                    if item == summaries[0][key][0]:
                        continue
                    summaries[0][key].append(item)
    return summaries[0]

####################################################

#calculates rate of errors (default) or successful attempts
#{'label':rate}
def calculateRatio(summary, res = "fails"):
    ratio = defaultdict(list)
    for key in summary:
        for sample in summary[key][1:]:
            if key not in ratio:
                ratio[key]=[0,1]
            if sample[2] == "true":
                 ratio[key][0] += 1
            ratio[key][1] += 1
    for each in ratio:
        if res == 'fails':
            ratio[each] = 1 - ratio[each][0]/ratio[each][1]
        else:
            ratio[each] = ratio[each][0]/ratio[each][1]
    return ratio

def t(m = "There"):
    print("="*10, m)
    
######################### ERROR
def calculateSamplesPerMinute(summary):
    minute = 60000
    starttime = 0
    first = True
    global target_spm_per_worker
    global total_spm 
    spm = []
    for key in summary:
        for sample in summary[key][1:]:
            spm.append(int(sample[1])//minute)    
    target_spm_per_worker.append(sum(spm)/len(spm))

def calculateSamplesPerMinuteTotal(summary):
    minute = 60000
    starttime = 0
    first = True
    global target_spm_per_worker
    global total_spm 
    spm = []
    for key in summary:
        for sample in summary[key][1:]:
           for sample in summary[key][1:]:
            spm.append(int(sample[1])//minute)
    total_spm = sum(spm)/len(spm)
    target_spm_per_worker = sum(target_spm_per_worker)/len(target_spm_per_worker)

def calculateSampleError(statistics):
    error = 0
    global sample_error
    for item in statistics:
        error += item[2]
    sample_error = str(error/len(statistics))
    return str(error/len(statistics))

def calculateIdle(idle_csv):
    global report_idle
    total_idle = 0
    lines = 0
    with open(idle_csv) as idles:
        idles.readline()
        for idle in idles:
            lines += 1
            idle.split(',')
            total_idle += 100 - int(idle[1])
    report_idle = str(total_idle // lines) + "%"

def calculatePagepm(file):
    global get_good_page_pm
    get_good_page_pm = getPagePerMinute(file)

def calculatePagepm_core():
    global requests_per_core
    requests_per_core = get_good_page_pm/cores

def calculateMemory(file_csv):
    global eden
    global old_gen
    lines = 0
    total = 0
    with open(file_csv) as file:
        file.readline()
        for record in file:
            lines += 1
            total += int(record.split(",")[1])
    if "eden" in file_csv:
        eden = (total / lines)/1000
    if "old_gen" in file_csv:
        old_gen = (total / lines)/1000

       
####################################################

#[(label, class, rate),..]
def getStatistics(summary):
    rate = calculateRatio(summary)
    statistics = []
    for key in rate:
        statistics.append((key, ".".join(summary[key][0].split(".")), rate[key]*100))
    return statistics

def printStatistics(summary): 
    for l in sorted(getStatistics(summary), key=itemgetter(2)):
        print(quick_summary_template.format(*l))

def printTable(summary, full = False):
    print(summary_table_header)
    for line in sorted(getStatistics(summary), key=itemgetter(2)):
        output = summary_table_row
        if line[2] == 100:
            output = "|(x)|" + output
        elif line[2] > 30:
            output = "|(!)|" + output
        else:
            output = "|(/)|" + output
        testclass = line[1]
        if not full:
            testclass = ".".join(line[1].split(".")[2:-1])
        print(output.format(line[0], testclass, line[2]))

def getErrors(summary, name):
    errors = set()
    for case in summary[name][2:]:
        if case[-1] not in errors:
            errors.add(case[-1])

def printAllReports(date):
    global eden_heap
    directory = reports_directory + date + r"/"
    summary_reports = directory + summary_path
    summary = reduceSummary(mapAllSummary(summary_reports))

    calculateSamplesPerMinuteTotal(summary)
    
    statistics = getStatistics(summary)
    calculateSampleError(statistics)

    idle_csv_path = directory + host_path + idle_csv
    eden_csv_path = directory + host_path + eden_csv
    old_gen_csv_path = directory + host_path + old_gen_csv

    try:
        calculateIdle(idle_csv_path)
    except Exception:
        print("Unable to calculate idle csv data")

    #try:
    calculateMemory(eden_csv_path)
    #except Exception:
        #print("Unable to calculate eden csv data")

    try:
        eden_heap = eden / (int(heap_total) * 1000)
    except Exception:
        print("Unable to calculate eden/heap csv data")

    try:
        calculateMemory(old_gen_csv_path)
    except Exception:
        print("Unable to calculate old gen csv data")

    

    #Собираем путь к htm_statistics
    html_report = directory + html_statistics_path
    for subdir, dirs, files in os.walk(html_report):
        for file in files:
            if "common-statistic.html" in file:
                html_report += r"/" + file
                break
    calculatePagepm(html_report)
    calculatePagepm_core()

    printAsAccessReport()
    printStatistics(summary)
    printTable(summary)
    
def printAsAccessReport():
    print(name, target, resume, testplan, environment, target_spm_per_worker, total_spm, sample_error, report_idle, empty_field, get_good_page_pm, empty_field, msc_count_pm, msc_time_pm, sessions, cores, requests_per_core, empty_field, empty_field,empty_field, ram, heap_total, eden, eden_heap, old_gen, const_mem, sep="\t")

         
if __name__ == '__main__':
    #print(calculateSampleError(getStatistics(reduceSummary(mapAllSummary(summarysource)))))
    printAllReports("2015-11-23")
    #summary = reduceSummary(mapAllSummary(summarysource))
    
    #print(summary["GlobalProblemScenarioIT"][0])
    #printStatistics(summary)
    print("\n")
    #printTable(summary)
    
    #print("\n".join(sorted(list(errors))))
    
