import os
import re

os.chdir("d:\\nv\\logs")
pattern = r'\[((?:\d+)\/(?:\w+)\/(?:\d+)\:(?:\d+)\:(?:\d+)\:(?:\d+) (?:\+|\-)(?:\d+))\] (\d+.\d+.\d+.\d+) ((?:\d+.\d+.\d+.\d+)|(?:\S+)) (\S+) (\[.+\]) (\S+) (POST|GET|OPTIONS|HEAD|PUT|PATCH|DELETE|TRACE|LINK|UNLINK|CONNECT) (\S+) (\S+) (\S+) (\S+) (\S+) (\S+) (\S+) "((?:[^"]+)|(?:\S+))" (\S+)'

date = "date"
ip = "ip"
field3 = "field3"
user = "user"
thread = "thread"
cookie = "cookie"
method = "method"
uri = "uri"
http = "http"
code1 = "code1"
code2 = "code2"
size = "size"
num = "num"
uri2 = "uri2"
useragent = "useragent"
other = "other"

fields = [date, ip, field3, user, thread, cookie, method, uri, http, code1, code2, size, num, uri2, useragent, other]



def iterate_logfile(criteria):
        with open("access.log") as file:
                for line in file:
                        if criteria:
                                if line.find(criteria) > -1:
                                        yield line
                        else:
                                yield line

def parse_log(line):
        log = {}
        matcher = re.match(pattern, line)
        group_index = 1
        for field in fields:
                try:
                        if field == uri:
                                log[field] = matcher.group(group_index)[:matcher.group(group_index).find("?")]        
                        else:
                                log[field] = matcher.group(group_index)
                except Exception:
                        return log
                group_index += 1
        return log
        

logs = []

for log in iterate_logfile("OPTIONS"):
        logs.append(parse_log(log))

def getUri():
        for log in logs:
                yield log[uri]        

list(map(print,sorted(set(getUri()))))
