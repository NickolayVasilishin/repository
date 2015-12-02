# -*- coding: utf-8 -*-

import xml.etree.ElementTree as ET
import pylab
access_log_path = r"D:\nv\work\rep\appserver-3.11.0\workspace\tools\accesslog-analyzer\target\temp\accesslog.dat "
enc = "UTF-8"

time_i, method_i, uri_i, category_i, status_i, time_spent_i = 2,5,6,7,9,12
data = {"GET":[], "POST":[]}  

with open(access_log_path, encoding=enc) as log_file: 
    root = ET.parse(log_file).getroot()
    for row in root:
        method = row[method_i].text 
        data[method].append(row[time_i])
        data[method].append(row[time_spent_i])
        data[method].append(row[uri_i])
        data[method].append(row[category_i])
        data[method].append(row[status_i])
        
pylab.plot(range(10), 'o')
