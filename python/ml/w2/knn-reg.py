# -*- coding: utf-8 -*-
"""
Created on Tue Aug 16 17:46:15 2016

@author: Nikolay_Vasilishin
"""

from sklearn.neighbors import KNeighborsRegressor
from sklearn.datasets import load_boston
from sklearn.preprocessing import scale
from sklearn.cross_validation import KFold
import scipy as sp
from sklearn.cross_validation import cross_val_score
import matplotlib.pyplot as plt
import pandas

boston_data = load_boston()

features = scale(boston_data.data)
target = boston_data.target

kf = KFold(len(features), 
               n_folds=5, 
               shuffle=True, 
               random_state=42)

results = []
for i in sp.linspace(1, 10, num=200):
    regressor = KNeighborsRegressor(n_neighbors=5, p=i, weights='distance')
    scores = cross_val_score(regressor, features, target, scoring='mean_squared_error', cv=kf)    
    results.append(scores)

means = pandas.DataFrame(sp.array(results).mean(axis=1))
means.plot()