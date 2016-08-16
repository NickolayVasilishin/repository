# -*- coding: utf-8 -*-
"""
Created on Mon Aug 15 14:19:20 2016

@author: Nikolay_Vasilishin
pandas1
"""
from pandas import DataFrame, read_csv
import matplotlib.pyplot as plt
import pandas as pd
import sys #only needed to determine Python version number
import matplotlib
import os

print('Python version ' + sys.version)
print('Pandas version ' + pd.__version__)
print('Matplotlib version ' + matplotlib.__version__)

names = ['Bob','Jessica','Mary','John','Mel']
births = [968, 155, 77, 578, 973]

BabyDataSet = list(zip(names,births))
df = pd.DataFrame(data = BabyDataSet, columns=['Names', 'Births'])

df.to_csv('births1880.csv', index=False, header=False)
df = pd.read_csv('births1880.csv', names=['Names','Births'])
os.remove('births1880.csv')

#df.dtypes
#df.Births.dtype

#==== Max value
#print(df.sort_values(['Births'], ascending=False).head(1))
#print(df['Births'].max()) eq to
#print(df.Births.max())

#==== Present
df.Births.plot()
maxName = df.Names[df.Births == df.Births.max()].values
#print(maxName)