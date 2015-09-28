import urllib.request
import pickle

banner = pickle.load(urllib.request.urlopen("http://www.pythonchallenge.com/pc/def/banner.p"))

for item in banner:
    for octotorp, times in item:
        print(octotorp * times, end="")
