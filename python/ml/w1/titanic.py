# -*- coding: utf-8 -*-
"""
Spyder Editor

This is a temporary script file.
"""
import operator
import pandas


data = pandas.read_csv('titanic.csv', index_col='PassengerId')

sex_count = data['Sex'].value_counts()

survived = data.Survived[data.Survived==1].size
#data['Survived'].value_counts()
survived_part = survived / data.size

first_class = data.Pclass[data.Pclass==1].size
#data['Pclass'].value_counts()
first_class_part = first_class / data.size

age_mean = data.Age.mean()
age_median = data.Age.median()



corr_s_p = data.SibSp.corr(data.Parch)

females = data[data.Sex=='female'].Name
females = females.apply(lambda x: x.split(".")[1].replace('"', '').replace('(', '').replace(')', '').split(" "))

l = []
females.apply(lambda x: l.extend(x))
while '' in l:
    l.remove('')

name_stat = {}    
for name in l:
    if name in name_stat:
        name_stat[name] = name_stat[name] + 1
    else:
        name_stat[name] = 1    

name_stat = sorted(name_stat.items(), key=operator.itemgetter(1), reverse=True)
most_popular_name = name_stat[0]