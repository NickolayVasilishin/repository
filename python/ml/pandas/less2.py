# -*- coding: utf-8 -*-
"""
Created on Mon Aug 15 14:34:44 2016

@author: Nikolay_Vasilishin
"""

import pandas as pd
from numpy import random
import matplotlib.pyplot as plt
import sys #only needed to determine Python version number
import matplotlib #only needed to determine Matplotlib version number
import os

names = ['Bob','Jessica','Mary','John','Mel']
random.seed(500)
random_names = [names[random.randint(low=0,high=len(names))] for i in range(1000)]
births = [random.randint(low=0,high=1000) for i in range(1000)]

BabyDataSet = list(zip(random_names,births))
df = pd.DataFrame(data = BabyDataSet, columns=['Names', 'Births'])

df.to_csv('births1880.txt',index=False,header=False)
df = pd.read_csv('births1880.txt', names=['Names','Births'])

print(df.info(), "\n")
print(df.head(), "\n")
os.remove('births1880.txt')

for x in df['Names'].unique():
    print(x)
print('\n')
    
print(df['Names'].describe())
print('\n')
print(df.groupby('Names').sum())
print('\n')
name = df.groupby('Names')
df = name.sum()
Sorted = df.sort_values(['Births'], ascending=False)
print(Sorted.head(1))
print('\n')
df['Births'].max()

df['Births'].plot.bar()
print("The most popular name")
df.sort_values(by='Births', ascending=False)