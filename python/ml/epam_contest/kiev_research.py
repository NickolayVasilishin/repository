# -*- coding: utf-8 -*-
"""
Created on Fri Sep  2 13:07:56 2016

@author: Nikolay_Vasilishin

Основной идеей было кластеризовать точки продаж на маленькие группы (5-15 точек)
и привлечь внешние факторы (погода), найти группы с похожими условиями и попытаться определить влияние отдельных факторов,
таких как акции, влияние погоды, наличие поблизости метро (не успел, хотел реализовать с помощью 
OpenStreetMap) и других объектов, взаимное влияние групп друг на друга.
Также визуализация на карте помогла бы определить наиболее прибыльные районы, возможно, помочь перераспределить инвестиции 
между близлежащими точками.

Также несложно было бы добавить интерактивности и отображать на карте влияние изменяемых параметров на продажи точек, а также
изменения продаж с течением времени.

"""

import pandas as pd
import scipy as np
from sklearn.metrics import mean_squared_error, r2_score
weather = pd.read_csv('weather_kiev.csv', 
                      parse_dates=['date'], 
                      infer_datetime_format=True, 
                      index_col=0).sort_index()
kiev_loc = pd.read_csv('kiev_loc.csv')
sales = pd.read_csv(r'..\data\processed-merged_sales_and_ads\PMU_Weekly_Sales_2014_2015_subbrand_merged.csv',
                    parse_dates=['Week'],
                    infer_datetime_format=True,
                    index_col=3)

#Здесь начинается процесс мержа уже подготовленных данных с данными о погоде, отношению к бренду
sales = sales[sales.TouchPoint_Code.isin(kiev_loc.Customer_Code.as_matrix())].sort_index()
print(sales.corr())
del sales['PMI_Portfolio_Hostess']
del sales['Fam_SA']
print(sales.shape)
sales = sales.merge(weather, how='left', left_index=True, right_index=True)
print(sales.shape)
brands = pd.read_excel(r'..\data\raw_from_customer\brands\Brand_equity 2012 - 2014.xlsx', index_col=2)
brands = brands.rename(columns={'Subbrand_family':'SubBrand_Family'})

brands['Brand_Family'] = brands['Brand_Family'].apply(lambda x: x.lower().capitalize()) 
sales['Brand_Family'] = sales['Brand_Family'].apply(lambda x: x.lower().capitalize()) 

sales.SubBrand_Family = sales.SubBrand_Family.apply(lambda name: name.replace(' ', '_').replace(r'/', '_').replace(r'&', '_').replace(r'+', '_').lower())
brands.SubBrand_Family = brands.SubBrand_Family.apply(lambda x: x.lower())

#brands_before = brands[:brands.index.searchsorted(sales.index[0])]
#brands_after = brands[brands.index.searchsorted(sales.index[0]):]

#extend indices for brands dataframe and interpolate values    
#Данные по отношению к брендам пришлось переиндексировать и интерполировать значения
#на неосвещенные промежутки времени 
brands_new = pd.DataFrame()
print('Extending indices')
for brand in brands.SubBrand_Family.unique():
    idx = brands[brands.SubBrand_Family == brand].index.append(sales.index.unique())
    b = brands[brands.SubBrand_Family == brand].reindex(idx)
    b.Brand_Family = b.Brand_Family.fillna(b.Brand_Family[0], axis=0)
    b.SubBrand_Family = b.SubBrand_Family.fillna(b.SubBrand_Family[0], axis=0)
    b = b.sort_index()
    b = b.interpolate()
    frame = [brands_new, b]
    brands_new = pd.concat(frame)    
    

brands_new = brands_new = brands_new.reset_index().drop_duplicates(subset=['index', 'Brand_Family', 'SubBrand_Family', 'Affinity','Brand Character', 'Functional Performance']).set_index('index')

brands_new = brands_new.fillna(brands_new.mean())
#Эти графики отражают отношение к бренду по всему необходимому времени
#for label in brands_new.SubBrand_Family.unique():
#    brands_new[brands_new.SubBrand_Family == label][['Affinity', 'Brand Character', 'Functional Performance']].plot(title=label)

sales1 = sales.reset_index()
brands_new1 = brands_new.reset_index()
sales_merged = sales1.merge(brands_new1, how='left', on=['index', 'Brand_Family', 'SubBrand_Family'])
sales_merged = sales_merged.set_index(['index'])
print('Merged')
#Брендам, к которым отношение не было зарегестрировано, я присвоил "среднее" отношение. Может быть, подпортил этим датасет.
sales_merged.Affinity = sales_merged.Affinity.fillna(sales_merged.Affinity.mean())
sales_merged['Brand Character']= sales_merged['Brand Character'].fillna(sales_merged['Brand Character'].mean())
sales_merged['Functional Performance'] = sales_merged['Functional Performance'].fillna(sales_merged['Functional Performance'].mean())


#Здесь уже времени не хватало, попробовал построить хоть какую-нибудь модель, но она получилась совсем отвратительной.
from sklearn.cross_validation import KFold
from sklearn.linear_model import ElasticNetCV
met = ElasticNetCV()

features = sales_merged[['PMI_Portfolio_AVB_Boost', 'PMI_Portfolio_PFP_Boost',
       'PMI_Portfolio_PPRP', 'PMI_Portfolio_SA', 'SubFam_Hostess',
       'SubFam_PFP_Boost', 'SubFam_RAP', 'SubFam_SA', 'Fam_AVB_Boost',
       'Fam_Hostess', 'Fam_PFP_Boost', 'Fam_RAP', 't', 'Affinity',
       'Brand Character', 'Functional Performance']].as_matrix()
target = sales_merged['Volume_Sales'].as_matrix()

met = ElasticNetCV(n_jobs=-1, l1_ratio=[.01, .05, .25, .5, .75, .95, .99])

kf = KFold(len(target), n_folds=5)
pred = np.zeros_like(target)
for train, test in kf:
    met.fit(features[train], target[train])
    pred[test] = met.predict(features[test])

print('[EN CV] RMSE on testing (5 fold), {:.2}'.format(np.sqrt(mean_squared_error(target, pred))))
print('[EN CV] R2 on testing (5 fold), {:.2}'.format(r2_score(target, pred)))
print('')

    
    
    
    
    

    
    
    
    
    