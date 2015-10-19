import json
from pprint import pprint

with open('steam-games.json') as data_file:    
    data = json.load(data_file)

pprint(list(filter((lambda x: len(x.get('name'))>81), data.get('applist').get('apps'))))
