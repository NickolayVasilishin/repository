# -*- coding: utf-8 -*-
import warnings
warnings.filterwarnings('ignore')
"""
Created on Mon Aug 22 17:52:47 2016

@author: Nikolay_Vasilishin
"""

import scipy as np
import pandas as pd
import matplotlib.pyplot as plt
import os
import pickle
from pandas.tools.plotting import scatter_matrix
from math import ceil
import itertools
from sklearn.preprocessing import StandardScaler
from sklearn.cross_validation import train_test_split
from sklearn.metrics import accuracy_score

plt.style.use('ggplot')

if os.path.exists('crx.data.pickle'):
    with open('crx.data.pickle', 'rb') as f:
        data = pickle.load(f)
else:
    url = 'https://archive.ics.uci.edu/ml/machine-learning-databases/credit-screening/crx.data'
    data = pd.read_csv(url, header=None, na_values='?')
    
data.columns = ['A' + str(i) for i in range(1, 16)] + ['class']

categorical_columns = [c for c in data.columns if data[c].dtype.name == 'object']
numerical_columns   = [c for c in data.columns if data[c].dtype.name != 'object']

print('Categorical columns', categorical_columns)
print('Numerical columns', numerical_columns)
print('_'*79)
print(data[numerical_columns].describe())
print('_'*79)
print(data[categorical_columns].describe())
print('_'*79)

print('Distinct values')
for c in categorical_columns:
    print(data[c].unique())
    
'''
Функция scatter_matrix из модуля pandas.tools.plotting позволяет построить для 
каждой количественной переменной гистограмму, а для каждой пары таких 
переменных – диаграмму рассеяния:
'''    

scatter_matrix(data, alpha=0.05, figsize=(20, 20))
plt.savefig('scatter_matrix.png')
print('Data correlation: ', data.corr(), sep='\n')

'''
Можно выбрать любую пару признаков и нарисовать диаграмму рассеяния для этой 
пары признаков, изображая точки, соответствующие объектам из разных классов 
разным цветом: + – красный, - – синий. 
'''
def plot_features():
    n_figs = (len(numerical_columns) ** 2 - len(numerical_columns)) / 2
    fig, axes = plt.subplots(ceil(n_figs ** (1/2)), ceil(n_figs ** (1/2)), figsize=(20,20))
    
    for i, (col1, col2) in enumerate(itertools.combinations(numerical_columns, 2)):
        ax = axes.flat[i]
        ax.scatter(data[col1][data['class'] == '+'],
                data[col2][data['class'] == '+'],
                alpha=0.75,
                color='red',
                marker='o',
                label='+')
        ax.scatter(data[col1][data['class'] == '-'],
                data[col2][data['class'] == '-'],
                alpha=0.75,
                color='blue',
                marker='x',
                label='-')
        ax.set_xlabel(col1)
        ax.set_ylabel(col2)
    fig.tight_layout()
    fig.savefig('features_comparsion.png')

plot_features()

print('Columns with nan entries\n', data.count(axis=0)[data.count(axis=0) - data.shape[0] != 0])
data_size = data.shape[0]
data = data.dropna(axis=0)
print('Data loss: {:.1%}'.format((data_size - data.shape[0]) / data_size))
'''
Заполнить пропущенные значения можно с помощью метода fillna. Заполним, например, медианными значениями.
data = data.fillna(data.median(axis=0), axis=0)
data_describe = data.describe(include=[object])
for c in categorical_columns:
    data[c] = data[c].fillna(data_describe[c]['top'])
'''
data_describe = data.describe(include=[object])
binary_columns    = [c for c in categorical_columns if data_describe[c]['unique'] == 2]
nonbinary_columns = [c for c in categorical_columns if data_describe[c]['unique'] > 2]
print('Binary columns:', binary_columns, 'Nonbinary columns: ', nonbinary_columns)


for c in binary_columns:
    top = data_describe[c]['top']
    top_items = data[c] == top
    data.loc[top_items, c] = 0
    data.loc[np.logical_not(top_items), c] = 1
    
'''
Заменим признак A4 тремя признаками: A4_u, A4_y, A4_l.

Если признак A4 принимает значение u, то признак A4_u равен 1, A4_y равен 0, A4_l равен 0.
Если признак A4 принимает значение y, то признак A4_y равен 0, A4_y равен 1, A4_l равен 0.
Если признак A4 принимает значение l, то признак A4_l равен 0, A4_y равен 0, A4_l равен 1.
'''
data_nonbinary = pd.get_dummies(data[nonbinary_columns])

#scaler = StandardScaler()
#data_numerical = data[numerical_columns]
#scaler.fit_transform(data_numerical)

data_numerical = data[numerical_columns]
data_numerical = (data_numerical - data_numerical.mean()) / data_numerical.std()

data = pd.concat((data_numerical, data[binary_columns], data_nonbinary), axis=1)
data = pd.DataFrame(data, dtype=float)

X = data.drop(('class'), axis=1)  # Выбрасываем столбец 'class'.
y = data['class']
feature_names = X.columns
N, d = X.shape

X_train, X_test, y_train, y_test = train_test_split(X, y, test_size = 0.3, random_state = 11)


"""
Метод	Класс
kNN – kk ближайших соседей	sklearn.neighbors.KNeighborsClassifier
LDA – линейный дискриминантный анализ	sklearn.lda.LDA
QDA – квадратичный дискриминантный анализ	sklearn.qda.QDA
Logistic – логистическая регрессия	sklearn.linear_model.LogisticRegression
SVC – машина опорных векторов	sklearn.svm.SVC
Tree – деревья решений	sklearn.tree.DecisionTreeClassifier
RF – случайный лес	sklearn.ensemble.RandomForestClassifier
AdaBoost – адаптивный бустинг	sklearn.ensemble.AdaBoostClassifier
GBT – градиентный бустинг деревьев решений	sklearn.ensemble.GradientBoostingClassifier
"""
print('_'*79)
print('_'*79)
print('k Nearest Neighbours')
from sklearn.neighbors import KNeighborsClassifier

knn = KNeighborsClassifier()
knn.fit(X_train, y_train)

y_train_predict = knn.predict(X_train)
y_test_predict = knn.predict(X_test)

print('Accuracy train', accuracy_score(y_train, y_train_predict))
print('Accuracy test', accuracy_score(y_test, y_test_predict))

from sklearn.grid_search import GridSearchCV
n_neighbors_array = list(range(1, 45, 2))
knn = KNeighborsClassifier()
grid = GridSearchCV(knn, param_grid={'n_neighbors': n_neighbors_array})
grid.fit(X_train, y_train)

best_cv_err = 1 - grid.best_score_
best_n_neighbors = grid.best_estimator_.n_neighbors
print('Best cross-validation error: {} for {} neighbours.'.format(best_cv_err, best_n_neighbors))

knn = KNeighborsClassifier(n_neighbors=best_n_neighbors)
knn.fit(X_train, y_train)
y_train_predict = knn.predict(X_train)
y_test_predict = knn.predict(X_test)

print('Accuracy train', accuracy_score(y_train, y_train_predict))
print('Accuracy test', accuracy_score(y_test, y_test_predict))

print('_'*79)
print('SVC')
from sklearn.svm import SVC
svc = SVC()
svc.fit(X_train, y_train)

print('Accuracy train', accuracy_score(y_train, svc.predict(X_train)))
print('Accuracy test', accuracy_score(y_test, svc.predict(X_test)))

C_array = np.logspace(-3, 3, num=7)
gamma_array = np.logspace(-5, 2, num=8)
svc = SVC(kernel='rbf')
grid = GridSearchCV(svc, param_grid={'C': C_array, 'gamma': gamma_array})
grid.fit(X_train, y_train)
print('CV error    = ', 1 - grid.best_score_)
print('best C      = ', grid.best_estimator_.C)
print('best gamma  = ', grid.best_estimator_.gamma)

svc = SVC(kernel='rbf', C=grid.best_estimator_.C, gamma=grid.best_estimator_.gamma)
svc.fit(X_train, y_train)
print('Accuracy train', accuracy_score(y_train, svc.predict(X_train)))
print('Accuracy test', accuracy_score(y_test, svc.predict(X_test)))

C_array = np.logspace(-3, 3, num=7)
svc = SVC(kernel='linear')
grid = GridSearchCV(svc, param_grid={'C': C_array})
grid.fit(X_train, y_train)
print('CV error    = ', 1 - grid.best_score_)
print('best C      = ', grid.best_estimator_.C)
svc = SVC(kernel='linear', C=grid.best_estimator_.C)
svc.fit(X_train, y_train)
print('Accuracy train', accuracy_score(y_train, svc.predict(X_train)))
print('Accuracy test', accuracy_score(y_test, svc.predict(X_test)))

print('_'*79)
print('Random Forest Classifier')
from sklearn import ensemble
rf = ensemble.RandomForestClassifier(n_estimators=100, random_state=11)
rf.fit(X_train, y_train)

print('Accuracy train', accuracy_score(y_train, rf.predict(X_train)))
print('Accuracy test', accuracy_score(y_test, rf.predict(X_test)))

importances = rf.feature_importances_
indices = np.argsort(importances)[::-1]

print("Feature importances:")
for f, idx in enumerate(indices):
    print("{:2d}. feature '{:5s}' ({:.4f})".format(f + 1, feature_names[idx], importances[idx]))
    
d_first = 20
plt.figure(figsize=(8, 8))
plt.title("Feature importances")
plt.bar(range(d_first), importances[indices[:d_first]], align='center')
plt.xticks(range(d_first), np.array(feature_names)[indices[:d_first]], rotation=90)
plt.xlim([-1, d_first]);
plt.savefig('feature_importances.png')