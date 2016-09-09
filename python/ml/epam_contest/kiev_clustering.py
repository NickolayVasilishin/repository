# -*- coding: utf-8 -*-
"""
Created on Wed Aug 31 15:58:10 2016

@author: Nikolay_Vasilishin

Try http://scikit-learn.org/stable/modules/generated/sklearn.cluster.AgglomerativeClustering.html#sklearn.cluster.AgglomerativeClustering
Ward

В этом скрипте я разбил на более мелкие кластеры точки продаж в районе Киева.
"""
import scipy as np
from gmplot import GoogleMapPlotter
import pandas as pd
from sklearn.cluster import DBSCAN

#Для цветового различия кластеров 
def convert_to_rgb(val, minval=1, maxval=3, colors = [(0, 0, 255), (0, 255, 0), (255, 0, 0)]):
    max_index = len(colors)-1
    v = float(val-minval) / float(maxval-minval) * max_index
    i1, i2 = int(v), min(int(v)+1, max_index)
    (r1, g1, b1), (r2, g2, b2) = colors[i1], colors[i2]
    f = v - i1
    return int(r1 + f*(r2-r1)), int(g1 + f*(g2-g1)), int(b1 + f*(b2-b1))

#В этом файле должны быть все точки продаж, разбитые на кластеры, но сама кластеризация не до конца допилена
loc = pd.read_csv("loc_slustered.csv")


grouped_clusters = pd.DataFrame(loc.groupby('cluster').size())
grouped_clusters = grouped_clusters.rename(columns={grouped_clusters.columns[0]:'count'})
grouped_clusters.reset_index(level=0, inplace=True)


single_level_clusters = grouped_clusters.query('count == 1')['cluster'].as_matrix()
norm_level_clusters = grouped_clusters.query('1 < count <= 10')['cluster'].as_matrix()
huge_level_clusters = grouped_clusters.query('count > 10')['cluster'].as_matrix()

#Здесь видно, что размеры кластеров очень варьируются, в том числе много "одноточечных кластеров"
print('Single-point clusters: %d, "2<x<=10"-point clusters: %d, ">10"-point clusters: %d' % (len(single_level_clusters), len(norm_level_clusters), len(huge_level_clusters)))

#s_loc = loc[loc.cluster.isin(single_level_clusters)]
#n_loc = loc[loc.cluster.isin(norm_level_clusters)]
#h_loc = loc[loc.cluster.isin(huge_level_clusters)]
#e_loc = loc[loc.cluster.isin(grouped_clusters.query('2 <= count')['cluster'].as_matrix())].query('(50 <= AddressGeographicalY <= 50.8) & (30.1 <= AddressGeographicalX <= 30.9)')
#0.00141  1702  0.261955    29

#В принципе, все точки Киева были собраны в кластер, но туда попала часть точек из пригорода, поэтому я решил 
#ограничить точки координатами, а на момент комментирования кода номер того кластера забыл
kiev_loc = loc.query('(50.35 <= AddressGeographicalY <= 50.6) & (30.3 <= AddressGeographicalX <= 30.7)')

#Поскольку все точки принадлежат одному кластеру
del kiev_loc['cluster']

#Кластеризация проводится в 2 этапа: сначала кластеризуем все точки, затем кластеризуем 
#"единичные" кластеры. Коэффициент 'e' подобран таким образом, чтобы минимизировать количество "одиночных" кластеров
def cluster(data, columns=['AddressGeographicalX', 'AddressGeographicalY'], e=0.00141, m_samples=1, cluster_last_index = 0, plot = False):
    X = data[columns].as_matrix()
    from sklearn.cluster import DBSCAN
    db = DBSCAN(eps=e, min_samples=m_samples).fit(X)
    core_samples_mask = np.zeros_like(db.labels_, dtype=bool)
    core_samples_mask[db.core_sample_indices_] = True
    labels = db.labels_
    # Number of clusters in labels, ignoring noise if present.
    n_clusters_ = len(set(labels)) - (1 if -1 in labels else 0)
    data['cluster'] = pd.Series(db.labels_, index = data.index)
    data['cluster'] = data['cluster'] + cluster_last_index
    last_index = data['cluster'].max()
    if plot:    
        import matplotlib.pyplot as plt
        # Black removed and is used for noise instead.
        plt.figure(figsize=(10,10))
        unique_labels = set(labels)
        colors = plt.cm.Spectral(np.linspace(0, 1, len(unique_labels)))
        for k, col in zip(unique_labels, colors):
            if  k == -1:
                # Black used for noise.
                col = 'k'
        
            class_member_mask = (labels == k)
        
            xy = X[class_member_mask & core_samples_mask]
            plt.plot(xy[:, 0], xy[:, 1], 'o', markerfacecolor=col,
                     markeredgecolor='k', markersize=6)
        
            xy = X[class_member_mask & ~core_samples_mask]
            plt.plot(xy[:, 0], xy[:, 1], 'o', markerfacecolor=col,
                     markeredgecolor='k', markersize=2)
        
        plt.title('Estimated number of clusters: %d, %d' % (n_clusters_, len(labels[labels == -1])))
        plt.show()
    print('Singled: %d, max: %d' % (data.groupby(by='cluster').count().query('Customer_Code == 1').count()[0], data.groupby(by='cluster').count().max()[0]))
    return data, last_index

#Кластеризуем дважды, при этом запоминаем номер последнего кластера, чтобы кластера не пересекались
kiev_loc, index = cluster(kiev_loc)
kiev_loc_singles = kiev_loc[kiev_loc.cluster.isin(kiev_loc.groupby(by='cluster').count().query('Customer_Code == 1').reset_index()['cluster'])]
kiev_loc_singles, index = cluster(kiev_loc_singles, e=0.00441, cluster_last_index=index)

kiev_loc.update(kiev_loc_singles)
kiev_loc.cluster = kiev_loc.cluster.astype(int)

print('Result: Singled: %d, max: %d' % (kiev_loc.groupby(by='cluster').count().query('Customer_Code == 1').count()[0], kiev_loc.groupby(by='cluster').count().max()[0]))
#points are near each to other?
kiev_loc.groupby(by='cluster').agg(np.std).plot()


ua_map = GoogleMapPlotter.from_geocode("Ukraine, Kiev", 12)
for (code, x, y, cluster) in kiev_loc.as_matrix():
    ua_map.scatter([y], [x], c='#%02x%02x%02x' % convert_to_rgb(cluster, minval=kiev_loc.cluster.min(), maxval=kiev_loc.cluster.max()), size=50, marker=False, alpha=0.5)
    
ua_map.draw("clustered.html")


#50.41916499 30.51916459