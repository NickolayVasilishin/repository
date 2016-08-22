# -*- coding: utf-8 -*-
"""
Created on Tue Aug 16 19:17:59 2016

@author: Nikolay_Vasilishin
"""

from sklearn.linear_model import Perceptron
import scipy as sp
from sklearn.preprocessing import StandardScaler
import pandas as pd
from matplotlib import pyplot as plt
from sklearn.metrics import accuracy_score

"""
Example
X = np.array([[1, 2], [3, 4], [5, 6]])
y = np.array([0, 1, 0])
clf = Perceptron()
clf.fit(X, y)
predictions = clf.predict(X)

Функция fit_transform данного класса находит параметры нормализации (средние и 
дисперсии каждого признака) по выборке, и сразу же делает нормализацию выборки 
с использованием этих параметров.
Функция transform делает нормализацию на основе уже найденных параметров.

scaler = StandardScaler()
X_train = np.array([[100.0, 2.0], [50.0, 4.0], [70.0, 6.0]])
X_test = np.array([[90.0, 1], [40.0, 3], [60.0, 4]])
X_train_scaled = scaler.fit_transform(X_train)
X_test_scaled = scaler.transform(X_test)
"""
scaler = StandardScaler()
train = pd.read_csv('perceptron-train.csv')
test = pd.read_csv('perceptron-test.csv')

X = train.as_matrix()[:,(1,2)]
Y = train.as_matrix()[:,0]

Xn = scaler.fit_transform(X.copy())

fig,axes = plt.subplots(2, 2)
for i, ax in enumerate(axes.flat):
    if i < 2:
        ax.scatter(list(range(X.shape[0])), X[:,i%2])
    else:
        ax.scatter(list(range(X.shape[0])), Xn[:,i%2])


clf = Perceptron(random_state=241)
clf.fit(X, Y)
predictions = clf.predict(test.as_matrix()[:,(1,2)])
print(accuracy_score(test.as_matrix()[:,0], predictions))
#correct = predictions[predictions == test.as_matrix()[:,0]]
#print(predictions.shape[0], correct.shape[0],  correct.shape[0] / predictions.shape[0])
correctness = accuracy_score(test.as_matrix()[:,0], predictions)

X = Xn

clf = Perceptron()
clf.fit(X, Y)

predictions = clf.predict(scaler.fit_transform(test.as_matrix()[:,(1,2)]))
print(accuracy_score(test.as_matrix()[:,0], predictions))
correctness -= accuracy_score(test.as_matrix()[:,0], predictions)
print(correctness)
#plt.scatter(list(range(X.shape[0])), X[:,0])
#plt.clf()
#plt.scatter(list(range(X.shape[0])), X[:,1])
