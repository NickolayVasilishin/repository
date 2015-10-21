import xml.etree.ElementTree as ET
import operator

def countProtocols(protocols):
    protonames = {}
    for name in protocols:
        if name in protonames:
            protonames[name] += 1
        else:
            protonames[name] = 1
    return protonames

def sortDictionary(dictionary):
    keys = list(dictionary)
    keys.sort()
    for key in keys:
        yield [key, dictionary[key]]

def getProtocolName(root):
    for packet in root:
        for proto in packet:
            yield proto.attrib['name']

def printSortedNames(names):
    list(map(print, sorted(list(names))))

def printSortedCounts(protocols):
    for proto, count in sorted(list(sortDictionary(protocols)), key=operator.itemgetter(1), reverse=True):
        print(proto, count, sep=': ')
    
tree = ET.parse('pcap.xml')
root = tree.getroot()

protonames = countProtocols(getProtocolName(root))
printSortedNames(protonames)
printSortedCounts(protonames)
