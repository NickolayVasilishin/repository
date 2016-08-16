# -*- coding: utf-8 -*-
"""
Created on Mon Aug 15 16:59:28 2016

@author: Nikolay_Vasilishin
"""

import pandas as pd
import sys

# Our small data set
d = {'one':[1,1],'two':[2,2]}
i = ['a','b']

# Create dataframe
df = pd.DataFrame(data = d, index = i)
# Bring the columns and place them in the index
stack = df.stack()
unstack = df.unstack()

transpose = df.T