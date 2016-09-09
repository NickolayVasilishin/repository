# -*- coding: utf-8 -*-
"""
Created on Tue Aug 30 20:15:44 2016

@author: Nikolay_Vasilishin

В данном скрипте я попытался кластеризовать точки по всей Украине. Результат визуализировал на графике.
"""

import pandas as pd
import scipy as np
from sklearn.cluster import DBSCAN

touchpoints = pd.read_excel(r"C:\Users\Nikolay_Vasilishin\Downloads\contest\data\raw_from_customer\POS\Ret UNI 2015 02 27.xlsx")
touchpoints = touchpoints[touchpoints.AddressGeographicalX != 0]
touchpoints = touchpoints[~sp.isnan(touchpoints.AddressGeographicalX)]

 
X = touchpoints[['AddressGeographicalX', 'AddressGeographicalY']].as_matrix()


db = DBSCAN(eps=0.04, min_samples=1).fit(X)
core_samples_mask = np.zeros_like(db.labels_, dtype=bool)
core_samples_mask[db.core_sample_indices_] = True
labels = db.labels_
# Number of clusters in labels, ignoring noise if present.
n_clusters_ = len(set(labels)) - (1 if -1 in labels else 0)
touchpoints['cluster'] = pd.Series(db.labels_, index = touchpoints.index)

# Plot result
import matplotlib.pyplot as plt

# Black removed and is used for noise instead.
plt.figure(figsize=(30,30))
unique_labels = set(labels)
colors = plt.cm.Spectral(np.linspace(0, 1, len(unique_labels)))
for k, col in zip(unique_labels, colors):
    if k == -1:
        # Black used for noise.
        col = 'k'

    class_member_mask = (labels == k)

    xy = X[class_member_mask & core_samples_mask]
    plt.plot(xy[:, 0], xy[:, 1], 'o', markerfacecolor=col,
             markeredgecolor='k', markersize=14)

    xy = X[class_member_mask & ~core_samples_mask]
    plt.plot(xy[:, 0], xy[:, 1], 'o', markerfacecolor=col,
             markeredgecolor='k', markersize=6)

plt.title('Estimated number of clusters: %d, %d' % (n_clusters_, len(labels[labels == -1])))
plt.show()
    
