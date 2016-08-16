# -*- coding: utf-8 -*-
"""
Created on Mon Aug 15 18:06:29 2016

@author: Nikolay_Vasilishin
"""
import pandas as pd
import numpy as np
import sys

""" COMPUTE """
""" COMPUTE """
""" COMPUTE """

# Get sum and length of a group
df = pd.DataFrame({'group1':["a","a","b","b"],
                'value':[10,20,30,40]})                
"""
  group1  value
0      a     10
1      a     20
2      b     30
3      b     40
"""
group = df.groupby('group1')
group.agg([len,sum])
"""
       value    
         len sum
group1          
a          2  30
b          2  70
"""

# Add a column that is equal to the sum of a group
df = pd.DataFrame({'labels':["a","a","b","b"],
                'value':[10,20,30,40]})  
group = df.groupby('labels')['value']
df['value.sum'] = group.transform('sum')
"""
  labels  value  value.sum
0      a     10         30
1      a     20         30
2      b     30         70
3      b     40         70
"""

# Get the month name out of a date column
df = pd.DataFrame({'col1':[pd.Timestamp('20130102000030'),
                         pd.Timestamp('2013-02-03 00:00:30'),
                         pd.Timestamp('3/4/2013 000030')]})
df['MonthNumber'] = df['col1'].apply(lambda x: x.month)
df['Day'] = df['col1'].apply(lambda x: x.day)
df['Year'] = df['col1'].apply(lambda x: x.year)
df['MonthName'] = df['col1'].apply(lambda x: x.strftime('%B'))
df['WeekDay'] = df['col1'].apply(lambda x: x.strftime('%A'))
"""
                 col1  MonthNumber  Day  Year MonthName    WeekDay
0 2013-01-02 00:00:30            1    2  2013   January  Wednesday
1 2013-02-03 00:00:30            2    3  2013  February     Sunday
2 2013-03-04 00:00:30            3    4  2013     March     Monday
"""

# Create a column based on two other columns
df = pd.DataFrame({'col1':['minus','minus','positive','nan'],
                'col2':[10,20,30,40]})
df['col3'] = df['col2']*df['col1'].apply(lambda x: -1 if x=='minus' else (1 if x=='positive' else np.nan))
"""
       col1  col2  col3
0     minus    10 -10.0
1     minus    20 -20.0
2  positive    30  30.0
3       nan    40   NaN
"""

# Apply a function to a group and add the results to my original data frame
df = pd.DataFrame({'group1':['a','a','a','b','b','b'],
                       'group2':['c','c','d','d','d','e'],
                       'value1':[1.1,2,3,4,5,6],
                       'value2':[7.1,8,9,10,11,12]})
group = df.groupby(['group1','group2'])
def Half(x):
    return x.sum()
df['new'] = group['value1'].transform(Half)
# For multiple functions
def HalfPlus(x):
    return x.sum() + 1
newcol = group['value1'].agg([Half,HalfPlus])
df.merge(newcol, left_on=['group1','group2'], right_index=True)

# Add two data frames and not get null values
df1 = pd.DataFrame(data=[26371, 1755, 2], index=[-9999, 240, 138.99], columns=['value'])
df2 = pd.DataFrame(data=[26371, 1755, 6, 4], index=[-9999, 240, 113.03, 110], columns=['value'])
df1.add(df2, fill_value=0)


""" MERGE """
""" MERGE """
""" MERGE """

# one of the dataframes has a timestamp and this is preventing me from adding the dataframes together
df1 = pd.DataFrame({'col1':[pd.Timestamp('20130102000030'),
                         pd.Timestamp('2013-01-03 00:00:30'),
                         pd.Timestamp('1/4/2013 000030')],
                 'col2':[1,10,18]
                 })
df1 = df1.set_index('col1')
d = {'col2':[22,10,113]}

i = [pd.Timestamp('20130102'),
     pd.Timestamp('2013-01-03'),
     pd.Timestamp('1/4/2013')]
                 

df2 = pd.DataFrame(data=d, index = i)
df2.index.name = 'col1'
df2 = df2.reindex(df1.index, method='pad')
df1+df2

# Add two dataframes together by row
df1 = pd.DataFrame([1,2,3])
df2 = pd.DataFrame([4,5,6])
pd.concat([df1,df2])

# join two data frames by index
df1.merge(df2, left_index=True, right_index=True, how='left')


""" SELECT """
""" SELECT """
""" SELECT """

# Select a random sample of a group
df = pd.DataFrame({'group1' : ["a","b","a","a","b","c","c","c","c",
                            "c","a","a","a","b","b","b","b"],
                'group2' : [1,2,3,4,1,3,5,6,5,4,1,2,3,4,3,2,1],
                'value'  : ["apple","pear","orange","apple",
                            "banana","durian","lemon","lime",
                            "raspberry","durian","peach","nectarine",
                            "banana","lemon","guava","blackberry","grape"]})
from random import choice
grouped = df.groupby(['group1','group2'])
grouped.size()
#df.loc[select a random record from each group]
df.loc[[choice(x) for x in grouped.groups.values()]]

# Slice each row of a column
df = pd.DataFrame(data=['abcdef']*10, columns=['text'])
df['text'].apply(lambda x: x[:2])

# Select rows of my dataframe based on a "complex" filter applied to multiple columns
d = {'Dates':[pd.Timestamp('2013-01-02'),
              pd.Timestamp('2013-01-03'),
              pd.Timestamp('2013-01-04')],
     'Num1':[1,2,3],
     'Num2':[-1,-2,-3]}              
df = pd.DataFrame(data=d)
# where all values in column "Num1" are positive
positive = df['Num1'] > 0
# where values in column "Num2" is equal to -1
negativeOne = df['Num2'] == -1
# where values in the column "Dates" are in (1/2/2013 or 1/20/2013)
Dates = df['Dates'].isin(['2013-01-02','2013-01-20'])
df[positive & negativeOne & Dates]

# Get the maximum value of a group
df = pd.DataFrame({'col1':['minus','minus','positive','nan'],
                'col2':[10,20,30,40],
                'col3':[-10,-20,30,np.nan]})
# Method 1
df.groupby('col1').apply(lambda x: x.max())
# Method 2
df.groupby('col1').agg('max')

# Select records from one level of a multi-index data frame
df = pd.DataFrame({'group1' : ["a","b","a","a","b","c","c","c","c",
                            "c","a","a","a","b","b","b","b"],
                'value' : [1,2,3,4,1,3,5,6,5,4,1,2,3,4,3,2,1],
                'group2'  : ["apple","pear","orange","apple",
                            "banana","durian","lemon","lime",
                            "raspberry","durian","peach","nectarine",
                            "banana","lemon","guava","blackberry","grape"]})
df = df.set_index(['group1','group2'])
df.xs('a', level='group1')

# Reset the index when the index names are the same as the column names
df = pd.DataFrame({"Name":["Alice", "Bob", "Mallory", "Mallory", "Bob" , "Mallory"] , 
                "City":["Seattle", "Seattle", "Portland", "Seattle", "Seattle", "Portland"]})
group = df.groupby(['City','Name'])
s = group.agg('size')
s.add_suffix('_size').reset_index()

"""
http://nbviewer.jupyter.org/urls/bitbucket.org/hrojas/learn-pandas/raw/master/lessons/Python_101.ipynb
"""