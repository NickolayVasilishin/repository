# -*- coding: utf-8 -*-
"""
Created on Thu Sep  1 18:16:46 2016

@author: Nikolay_Vasilishin
http://rp5.ru/%D0%90%D1%80%D1%85%D0%B8%D0%B2_%D0%BF%D0%BE%D0%B3%D0%BE%D0%B4%D1%8B_%D0%B2_%D0%9A%D0%B8%D0%B5%D0%B2%D0%B5,_%D0%96%D1%83%D0%BB%D1%8F%D0%BD%D0%B0%D1%85_(%D0%B0%D1%8D%D1%80%D0%BE%D0%BF%D0%BE%D1%80%D1%82)
В данном скрипте я подготовил данные о температуре в период времени продаж.
"""

import pandas as pd
def simplify_file(wfile):
    lines = []
    with open(wfile, encoding='utf-8') as file:
        file.readline()
        for line in file:
            lines.append(list(map(lambda x: x.replace('"', ''), line.split(';')[:2])))
    return lines

lines = simplify_file('33345.01.01.2014.02.02.2015.1.0.0.ru.utf8.00000000.csv')

weather = pd.DataFrame(lines, columns = ['datetime', 't'])
weather.datetime = pd.to_datetime(weather.datetime, format='%d.%m.%Y %H:%M')
weather.t = weather.t.astype(float)
weather.plot()
weather['date'] = weather.datetime.dt.date
weather['time'] = weather.datetime.dt.time
weather = weather[(weather.time.apply(lambda x: x.hour) >=9)&(weather.time.apply(lambda x: x.hour) <=21)]
weather.plot()
del weather['time']
del weather['datetime']
weather = weather.groupby(by='date').mean()
weather.plot()
weather.to_csv('weather_kiev.csv')
