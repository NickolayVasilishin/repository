# -*- coding: utf-8 -*-
"""
Created on Mon Sep 19 18:03:57 2016

@author: Nikolay_Vasilishin
"""

from sklearn import datasets
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.svm import SVC
import scipy as np
from sklearn.cross_validation import KFold
from sklearn.grid_search import GridSearchCV
import pandas as pd

newsgroups = datasets.fetch_20newsgroups(
                    subset='all', 
                    categories=['alt.atheism', 'sci.space']
             )
vectorizer = TfidfVectorizer()
features = vectorizer.fit_transform(newsgroups.data)
feature_mapping = vectorizer.get_feature_names()


grid = {'C': np.power(10.0, np.arange(-5, 6))}
cv = KFold(newsgroups.target.size, n_folds=5, shuffle=True, random_state=241)
clf = SVC(kernel='linear', random_state=241)
gs = GridSearchCV(clf, grid, scoring='accuracy', cv=cv)
gs.fit(features, newsgroups.target)

gs.grid_scores_.sort(key = lambda x: x.mean_validation_score, reverse=True)
print("Optimal C coefficient = %.3f with mean validation score = %.3f" % (gs.grid_scores_[0][0]['C'], gs.grid_scores_[0].mean_validation_score))

clf = SVC(C=gs.grid_scores_[0].mean_validation_score, kernel='linear', random_state=241)
clf.fit(features, newsgroups.target)
print(clf.coef_)
indices = pd.DataFrame(clf.coef_.transpose().toarray()).apply(np.absolute).nlargest(10, 0).index.tolist()

print(" ".join(sorted([feature_mapping[idx] for idx in indices])))