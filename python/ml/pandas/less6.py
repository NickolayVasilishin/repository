# -*- coding: utf-8 -*-
"""
Created on Mon Aug 15 17:04:38 2016

@author: Nikolay_Vasilishin
"""

# Import libraries
import pandas as pd
import sys

# Our small data set
d = {'one':[1,1,1,1,1],
     'two':[2,2,2,2,2],
     'letter':['a','a','b','b','c']}

# Create dataframe
df = pd.DataFrame(d)

one = df.groupby('letter')
letterone = df.groupby(['letter','one']).sum()
letterone = df.groupby(['letter','one'], as_index=False).sum()