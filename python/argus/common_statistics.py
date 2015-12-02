import xml.etree.ElementTree as ET
import re

pattern_start = re.compile("^<table .*>$")
pattern_end = re.compile("^</table>$")

def getTable(html_statistic):
    table = ""
    toRecord = False
    with open(html_statistic) as html:
        for line in html:
            if pattern_start.match(line):
                toRecord = True
            if toRecord:
                if "nbsp" in line:
                    line = "----\n"
                table += line
            if pattern_end.match(line):
                return table

def countGetPage(table):
    getPageTime = 0
    count = 0
    root = ET.fromstring(table)
    for tr in root[1:]:
        if "----" in tr[0].text:
            continue
        getPageTime += float(tr[13].text.replace("\n", "").lstrip())
        count += 1
    return getPageTime / count

def getPagePerMinute(html_statistic):
    return countGetPage(getTable(html_statistic))

if __name__ == '__main__':
    countGetPage(getTable(r"B:\programmersmount\jmeter\n.vasilishin\reports\2015-11-15\host\2015-11-14_13_57_21__2015-11-16_14_05_23.common-statistic.html"))
