# -*- coding: utf-8 -*-

from scipy.interpolate import interp1d
from matplotlib import pyplot as plt
import numpy as np
import re
import os
from sys import stdout

access_log_path = r"D:\nv\work\rep\appserver-3.11.0\workspace\tools\accesslog-analyzer\target\temp\accesslog.dat"
file_length = os.path.getsize(access_log_path) #bytes
total_read = 0
enc = "UTF-8"
parameter_pattern = re.compile(">(.*)<")

data = {"GET":{}, "POST":{}}  

def extract(data):
    for time in sorted(data):
        for rec in data[time]:
            yield rec[0]

def each(iterable, n=10):
    l = len(iterable) // 10
    count = 0
    yield iterable[0]
    for item in iterable:
        count += 1
        if count == l:
            count = 0
            yield item


with open(access_log_path, encoding=enc) as log_file:
    if "<ROWSET>" not in log_file.readline():
        print("Wrong accesslog.dat file")
        exit()
        
    params = [None]*6
    time = 0
    method = 1
    uri = 2
    category = 3
    status = 4
    time_spent = 5
    rows = 0
    percent = 0
    print("Processing", end='')
    stdout.flush()
    for line in log_file:
        total_read += len(line)
        current_percent = int("{:3.0f}".format(100*total_read / file_length))
        if percent+1 < current_percent:
            percent = current_percent
            print('.', sep='', end='')
            stdout.flush()
        if "<ROW>" in line:
            rows += 1
            params = [None]*6
            continue
        if "<TIME_END>" in line:
            params[time] = parameter_pattern.search(line).group(1)
            continue
        if "<METHOD>" in line:
            params[method] = parameter_pattern.search(line).group(1)
            continue
        if "<URI>" in line:
            params[uri] = parameter_pattern.search(line).group(1)
            continue
        if "<CATEGORY>" in line:
            params[category] = parameter_pattern.search(line).group(1)
            continue
        if "<STATUS_PHYSICAL>" in line:
            params[status] = parameter_pattern.search(line).group(1)
            continue
        if "<TIME_SPENT>" in line:
            params[time_spent] = parameter_pattern.search(line).group(1)
            continue
        if "</ROW>" in line:
            if params[time] not in data[params[method]]:
                data[params[method]][params[time]] = []
            data[params[method]][params[time]].append((params[time_spent], params[status], params[category], params[uri]))
            continue
        
#        if "<IP>" in line:
#            continue
#        if "<LOGIN>" in line:
#            continue
#        if "<REQUEST_THRED_NAME>" in line:
#            continue
#        if "<USER_SESSION_ID>" in line:
#            continue            
#        if "<HTTP_VERSION>" in line:
#            continue
#        if "<STATUS_LOGICAL>" in line:
#            continue
#        if "<SIZE>" in line:
#            continue
#        if "<REFERER>" in line:
#            continue
#        if "<USER_AGENT>" in line:
#            continue
#        if "<BROWSER>" in line:
#            continue
#        if "<OS>" in line:
#            continue
#        if "<FORWARDER_FOR>" in line:
#            continue
#        if "<SERIAL_NUMBER>" in line:
#            continue    

print("\r","100%",sep='')        
    
 


labels = list(sorted(data["POST"]))

with plt.style.context('classic'):
    y_get = list(filter((lambda x:int(x)>0), list(extract(data["GET"]))))
    y_post = list(filter((lambda x:int(x)>0), list(extract(data["POST"]))))
       
    plt.plot(y_get, 'g', label='GET', c='#88dddd', linewidth=6)       
    x = np.linspace(0, len(y_get), num=len(y_post))
    plt.plot(x, y_post, 'r', label='POST', c='#ffbbbb', linewidth=4)

    
    plt.xticks(list(each(x)), list(each(labels)), rotation='70')
    plt.margins(0.2)
    plt.subplots_adjust(bottom=0.15)
    plt.legend(['GET', 'POST'], loc='best')
    
average_get = sum(map(int, y_get))/len(y_get)
average_post = sum(map(int, y_post))/len(y_post)
print("Average GET:", average_get, "\nAverage POST:", average_post)
plt.show()