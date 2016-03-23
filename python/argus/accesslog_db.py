# -*- coding: utf-8 -*-
"""
Created on Fri Feb 26 13:46:01 2016

@author: n.vasilishin
"""
import pickle
import re
import os
from sys import stdout

access_log_path = r"D:\nv\work\rep\appserver-3.10.0\workspace\tools\accesslog-analyzer\target\temp\accesslog.dat"
file_length = os.path.getsize(access_log_path) #bytes
total_read = 0
enc = "UTF-8"
parameter_pattern = re.compile(">(.*)<")

with open(access_log_path, encoding=enc) as log_file:
    if "<ROWSET>" not in log_file.readline():
        print("Wrong accesslog.dat file")
        exit()
        
    params = []

    rows = 0
    percent = 0
    print("Processing", end='')
    stdout.flush()
    
    logs = []    
    
    for line in log_file:
        total_read += len(line)
        current_percent = int("{:3.0f}".format(100*total_read / file_length))
        if percent+1 < current_percent:
            percent = current_percent
            print('.', sep='', end='')
            stdout.flush()
        if "<ROW>" in line:
            rows += 1
            params = []
            continue
        
        if "</ROW>" in line:
            logs.append(params)
            continue
        
        try:
            params.append(parameter_pattern.search(line).group(1))
        except Exception:
            params.append("")
     
with open('logs.pickle', 'wb') as f:
    pickle.dump(logs, f)
        
#        if "<TIME_END>" in line:
#            params[time] = parameter_pattern.search(line).group(1)
#            continue
#        if "<METHOD>" in line:
#            params[method] = parameter_pattern.search(line).group(1)
#            continue
#        if "<URI>" in line:
#            params[uri] = parameter_pattern.search(line).group(1)
#            continue
#        if "<CATEGORY>" in line:
#            params[category] = parameter_pattern.search(line).group(1)
#            continue
#        if "<STATUS_PHYSICAL>" in line:
#            params[status] = parameter_pattern.search(line).group(1)
#            continue
#        if "<TIME_SPENT>" in line:
#            params[time_spent] = parameter_pattern.search(line).group(1)
#            continue
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
        

