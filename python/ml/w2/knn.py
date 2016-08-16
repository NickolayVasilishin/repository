# -*- coding: utf-8 -*-
"""
Created on Tue Aug 16 14:43:07 2016

@author: Nikolay_Vasilishin
"""
import pandas
import scipy as sp
from sklearn.cross_validation import KFold
from sklearn.neighbors import KNeighborsClassifier
import matplotlib.pyplot as plt
from sklearn.preprocessing import scale
features = 'Class','Alcohol','Malic acid' ,'Ash','Alcalinity of ash','Magnesium','Total phenols','Flavanoids','Nonflavanoid phenols','Proanthocyanins','Color intensity','Hue','OD280/OD315 of diluted wines','Proline'
data = pandas.read_csv('wine.data', names = features)

classes = data.Class.as_matrix()
wines = data.copy()
del wines['Class']
wines = scale(wines.as_matrix())
#wines = wines.as_matrix()
results = [None]*51
kf = KFold(len(wines), 
               n_folds=5, 
               shuffle=True, 
               random_state=42)

for i in range(1, 51):
    classifier = KNeighborsClassifier(n_neighbors=i)
    results[i] = []
    for train, test in kf:
        classifier.fit(wines[train], classes[train])
        prediction = classifier.predict(wines[test])
        curmean = sp.mean(prediction == classes[test])
        results[i].append(curmean)
    results[i] = sp.mean(results[i])
    
dresults = pandas.DataFrame(data=results)
dresults.plot()
print(dresults)
dresults.nlargest(5, 0)
""" 34 0.72 """
""" 29 0.978 """
