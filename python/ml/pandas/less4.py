# -*- coding: utf-8 -*-
"""
Created on Mon Aug 15 16:54:16 2016

@author: Nikolay_Vasilishin
"""

import pandas as pd
import sys

d = [0,1,2,3,4,5,6,7,8,9]

df = pd.DataFrame(d)
#Name column
df.columns = ['Rev']
#Add new column and fill with 5
df['NewCol'] = 5
#Increment all column
df['NewCol'] = df['NewCol'] + 1
print(df)
del df['NewCol']

df['test'] = 3
df['col'] = df['Rev']

# If we wanted, we could change the name of the index
i = ['a','b','c','d','e','f','g','h','i','j']
df.index = i

#Select by index
df.loc['a']
# df.loc[inclusive:inclusive]
df.loc['a':'d']
# df.iloc[inclusive:exclusive]
# Note: .iloc is strictly integer position based. It is available from [version 0.11.0] (http://pandas.pydata.org/pandas-docs/stable/whatsnew.html#v0-11-0-april-22-2013) 
df.iloc[0:3]
#by cloumn
df['Rev']
df[['Rev', 'test']]
# df.ix[rows,columns]
df.ix[0:3,'Rev']
df.ix[5:,'col']
df.ix[:3,['col', 'test']]