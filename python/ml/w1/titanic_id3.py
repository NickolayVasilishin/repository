# -*- coding: utf-8 -*-
"""
Created on Mon Aug 15 19:45:39 2016

@author: Nikolay_Vasilishin
"""
from sklearn.externals.six import StringIO 
import operator
import pandas as pd
import numpy as np
import scipy as sp
from sklearn.tree import DecisionTreeClassifier

data = pd.read_csv('titanic.csv', index_col='PassengerId')
data_all = pd.read_csv('titanic.csv', index_col='PassengerId')

""" Example """
X = np.array([[1, 2], [3, 4], [5, 6]])
y = np.array([0, 1, 0])
clf = DecisionTreeClassifier()
clf.fit(X, y)
print(clf.predict(np.array([3, 3.8]).reshape(1, -1)))
""" Example """

features = 'Pclass', 'Fare', 'Age', 'Sex', 'Survived'
# Extract only needed features
for c in data.columns:
    if c not in features:
        del data[c]
#0 for man, 1 for woman
data.Sex = data.Sex.apply(lambda x: 0 if x == 'male' else 1)
# Remove all Age nan items
data = data[~sp.isnan(data.Age)]
Y = data.Survived[:]
del data["Survived"]
X = data[:]
print(X.columns)
clf = DecisionTreeClassifier(random_state=241)
clf.fit(X, Y)

print(clf.feature_importances_)