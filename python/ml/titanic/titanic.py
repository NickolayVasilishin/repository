# -*- coding: utf-8 -*-
"""
Created on Thu Oct 20 17:50:22 2016

@author: Nikolay_Vasilishin
"""

import pandas as pd

train = pd.read_csv("train.csv")
train.Age = train.Age.fillna(train.Age.median())

#get all males/females and only Sex solumn and assign number
for number, sex in enumerate(train.Sex.unique()):
    train.loc[train.Sex == sex, 'Sex'] = number    
train.Sex = pd.to_numeric(train.Sex, errors='coerce')


train.Embarked = train.Embarked.fillna('S')
for number, col in enumerate(train.Embarked.unique()):
    train.loc[train.Embarked == col, 'Embarked'] = number
train.Embarked = pd.to_numeric(train.Embarked, errors='coerce')
    
from sklearn.linear_model import LinearRegression
from sklearn.cross_validation import KFold

predictors = ['Pclass', 'Sex','Age','SibSp','Parch','Fare','Embarked']

alg = LinearRegression()

kf = KFold(train.shape[0], n_folds=3, random_state=1)

predictions = []
for tr, tst in kf:
    train_predictors = (train[predictors].iloc[tr,:])
    train_target = train.Survived.iloc[tr]
    alg.fit(train_predictors, train_target)
    
    test_predictions = alg.predict(train[predictors].iloc[tst, :])
    predictions.append(test_predictions)