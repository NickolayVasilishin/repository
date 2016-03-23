# -*- coding: utf-8 -*-
"""
Created on Sat Feb 27 15:40:05 2016

@author: n.vasilishin
"""
from datetime import datetime
import re
import functools
p = re.compile(r'((?:\d+[\.\: ]?)+)\t(.*)\t(\d+)')
enc = "UTF-8"

def header():
    print("date\t\t  user_session_id\t   time_spent")
def diff_sec(one, two):
    return (datetime.strptime(two, '%d.%m.%y %H:%M:%S') - datetime.strptime(one, '%d.%m.%y %H:%M:%S')).seconds
#datetime.strptime(p.match(line).group(1), '%d.%m.%y %H:%M:%S')
logs = []
access_log_path = r"from_db"
with open(access_log_path, encoding=enc) as log_file:
    for line in log_file:
        if p.match(line):
            logs.append(( p.match(line).group(1),
                          p.match(line).group(2),
                          p.match(line).group(3) ))
            
first = True          

streaks = []
streak = []
streak.append(logs[0])
for log in logs:
    if first:
        first = False
        continue
    if log[1] == streak[-1][1] and diff_sec(streak[-1][0], log[0]) < 10:
        streak.append(log)
        continue
    elif len(streak) > 1:
        if len(streaks) > 0 and diff_sec(streaks[-1][-1][0], streak[0][0]) < 10:
            streaks[-1].extend(streak)
        else:
            streaks.append(streak)
    streak = [log]
        
        
#list(map(print, streaks))        
feb18 = [streak for streak in streaks if streak[0][0].split(" ")[0] == '18.02.16']
feb19 = [streak for streak in streaks if streak[0][0].split(" ")[0] == '19.02.16']
feb20 = [streak for streak in streaks if streak[0][0].split(" ")[0] == '20.02.16']
feb24 = [streak for streak in streaks if streak[0][0].split(" ")[0] == '24.02.16']

print("Всего серий: ", len(streaks))
print("Всего серий за 18.02: ", len(feb18))
print("Всего серий за 19.02: ", len(feb19))
print("Всего серий за 20.02: ", len(feb20))
print("Всего серий за 24.02: ", len(feb24))

print("\nМаксимальная последовательность: ", max(map(len, streaks)))
print("{noformat}")
header()
n = 5
for i in filter( (lambda x: len(x) == max(map(len, streaks))), streaks ):
    m = 3
    for it in i:
        print(*it)
        m = m - 1
        if m == 0:
            print("...")
            break
    n = n-1
    if n == 0:
        print("...")
        break
print("{noformat}")
print("Минимальная последовательность: ", min(map(len, streaks)))
print("Средний размер последовательности: ", sum(map(len, streaks)) / len(streaks))
def time(streak):
    return datetime.strptime(streak[-1][0], '%d.%m.%y %H:%M:%S') - datetime.strptime(streak[0][0], '%d.%m.%y %H:%M:%S')


max_time = max(map(time, streaks))
print("Максимальная продолжительность: ", max_time)
print("{noformat}")
header()
n = 5
for i in filter( (lambda x: time(x) == max_time), streaks ):
    m = 3
    for it in i:
        print(*it)
        m = m - 1
        if m == 0:
            print("...")
            break
    n = n-1
    if n == 0:
        print("...")
        break
    print()
print("{noformat}")

print("Общая продолжительность: ", functools.reduce(lambda x, y: x + y, map(time, streaks)))
print("Средняя продолжительность: ", functools.reduce(lambda x, y: x + y, map(time, streaks)) / len(streaks))


#print("{noformat}")
#for streak in streaks:
#    for log in streak:
#        print(*log)
#    print()
#print("{noformat}")