# -*- coding: utf-8 -*-
"""
Created on Tue Aug 30 13:31:31 2016

@author: Nikolay_Vasilishin
Данный скрипт должен был наносить heatmap на карту Украины, иллюстрирующую интенсивность продаж.
Однако gmplot рисует очень плохую heatmap, поэтому решил отразить "горячесть" точки ее цветом.
К сожалению, не успел нормировать значения, поэтому не синими отрисовались лишь пара точек в Киеве, у которых огромные продажи.
Также для быстродействия я использовал часть данных о продажах. 

"""


import scipy as np
import matplotlib.pyplot as plt
import pandas as pd
from sklearn.preprocessing import StandardScaler
import pickle
from gmplot import GoogleMapPlotter

sales_columns = ['TouchPoint_Code',
 'Product_Code',
 'Product',
 'SubBrand_Family',
 'Brand_Family',
 'Week',
 'Volume_Sales']

def convert_to_rgb(val, minval=1, maxval=3, colors = [(0, 0, 255), (0, 255, 0), (255, 0, 0)]):
    max_index = len(colors)-1
    v = float(val-minval) / float(maxval-minval) * max_index
    i1, i2 = int(v), min(int(v)+1, max_index)
    (r1, g1, b1), (r2, g2, b2) = colors[i1], colors[i2]
    f = v - i1
    return int(r1 + f*(r2-r1)), int(g1 + f*(g2-g1)), int(b1 + f*(b2-b1))

def normalize(data, new_min=0, new_max=1):
    old_range = (data.max() - data.min())
    new_range = new_max - new_min
    
    data = (((data - data.min()) * new_range) / old_range) + new_min
    return data

def ua_map(datax, datay, sales):
    ua_map = GoogleMapPlotter.from_geocode("Ukraine", 7)
    for (x, y, s) in zip(datax, datay, sales):
        ua_map.scatter([x], [y], c='#%02x%02x%02x' % convert_to_rgb(s+1, maxval=10), size=1000+10*s, marker=False, alpha=0.5)    
    #ua_map.heatmap(datax, datay, radius=40)
    ua_map.draw("mymap.html")
    

#data = pd.read_csv(r"C:\Users\Nikolay_Vasilishin\Downloads\contest\data\raw_from_customer\sales\sample.csv")
with open(r"C:\Users\Nikolay_Vasilishin\Downloads\contest\data\raw_from_customer\sales\data.dump", "rb") as file:
    data = pickle.load(file)
data = data.rename(columns=dict(zip(data.columns, sales_columns)))

touchpoints = pd.read_excel(r"C:\Users\Nikolay_Vasilishin\Downloads\contest\data\raw_from_customer\POS\Ret UNI 2015 02 27 with new columns.xlsx", skiprows=1)
touchpoints = touchpoints[['Customer_Code', 'AddressGeographicalX', 'AddressGeographicalY']]

touchpoints = touchpoints.rename(columns={touchpoints.columns[0]:'TouchPoint_Code'})

gdata = data[['TouchPoint_Code', 'Volume_Sales']].sample(n=10000)
gdata = gdata.groupby(by='TouchPoint_Code').sum()
gdata.reset_index(level=0, inplace=True)

#data = data.merge(touchpoints, on='TouchPoint_Code', how='left')

gdata = touchpoints.merge(gdata, on='TouchPoint_Code')
gdata.Volume_Sales = normalize(gdata.Volume_Sales, 1, 10)

ua_map(gdata.AddressGeographicalY.as_matrix(), gdata.AddressGeographicalX.as_matrix(), gdata.Volume_Sales.as_matrix())


