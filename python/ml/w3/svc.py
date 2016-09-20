# -*- coding: utf-8 -*-
"""
Created on Mon Sep 19 17:44:37 2016

@author: Nikolay_Vasilishin
"""

import pandas as pd
from sklearn.svm import SVC
import scipy as np

dataset = pd.read_csv("svm-data.csv", names=['target', 'f1', 'f2'])

target = dataset.target.as_matrix()
features = dataset[['f1', 'f2']].as_matrix()

svc = SVC(C=100000, kernel='linear', random_state=241)
svc.fit(features, target)


