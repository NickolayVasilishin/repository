# -*- coding: utf-8 -*-
"""
Created on Fri Sep  9 12:26:06 2016

@author: Nikolay_Vasilishin
"""
import gevent
from gevent import monkey
import os.path
# patches stdlib (including socket and ssl modules) to cooperate with other greenlets
monkey.patch_all()

import lxml.html as html
import re
from urllib.request import urlretrieve
bookpattern = re.compile(r'(?:http:\/\/www\.oreilly\.com\/data\/free\/)(.*)(?:.csp.*)')
 
base = r'http://www.oreilly.com/data/free/archive.html'
download = r'http://www.oreilly.com/data/free/files/%s.pdf'
name=r'E:\education\Books\Big Data_tmp\%s.pdf'
error = []

def get_books_urls(base):
    page = html.parse(base)
    books_by_year = page.getroot().find_class("product-row cover-showcase")
    for section in books_by_year:
        for element in section.getchildren():
            if 'href' in element.attrib and 'title' in element.attrib:
                print(element.attrib['title'])
                try:
                    if not os.path.exists(name % element.attrib['title'].replace('?', '').replace(':', '')):
                        yield (download % re.findall(bookpattern, element.attrib['href'])[0], name % element.attrib['title'].replace('?', '').replace(':', ''))
                except Exception:
                    error.append((element.attrib['href'], element.attrib['title']))          

def download_book(url, title):
    print(title)
    try:
        urlretrieve(url, title)
    except Exception:
        error.append((url, title))

jobs = [gevent.spawn(download_book, url, title) for (url, title) in get_books_urls(base)]
gevent.joinall(jobs)