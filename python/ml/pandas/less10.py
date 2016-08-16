# -*- coding: utf-8 -*-
"""
Created on Mon Aug 15 17:56:06 2016

@author: Nikolay_Vasilishin

From DataFrame to Excel
From Excel to DataFrame
From DataFrame to JSON
From JSON to DataFrame
"""

import pandas as pd
import sys

# Create DataFrame
d = [1,2,3,4,5,6,7,8,9]
df = pd.DataFrame(d, columns = ['Number'])

# Export to Excel
df.to_excel('Lesson10.xlsx', sheet_name = 'testing', index = False)
# Path to excel file
# Your path will be different, please modify the path below.
location = r'Lesson10.xlsx'
# Parse the excel file
df = pd.read_excel(location, 0)
df.head()


# JSON
df.to_json('Lesson10.json')
# read json file
df2 = pd.read_json(location)