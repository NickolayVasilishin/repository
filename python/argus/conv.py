# -*- coding: utf-8 -*-
"""
Created on Mon Nov 30 17:47:52 2015

@author: n.vasilishin
"""
import re
p = re.compile(r'([\w+-\/]*)\s*(.*\.jar)\s+(\d+)\s+(\d+)')

print(("||{0}||{1}||{2}||{3}||").format("Path", "Name", "Size", "CRC"))

with open("D:\similar_deps.txt", 'r') as file:
    count = 1
    print("|{}|{}|{}|{}|".format(count, *(["----"]*3)))
    for line in file:
        match = re.match(p, line)
        if not match:
            count += 1
            print("|{}|{}|{}|{}|".format(count, *(["----"]*3)))
            continue
        print("|{}|{}|{}|{}|".format(match.group(1),match.group(2),match.group(3),match.group(4)))