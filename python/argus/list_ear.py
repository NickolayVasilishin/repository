# -*- coding: utf-8 -*-
"""
Created on Wed Nov 25 19:25:58 2015

@author: n.vasilishin
"""
import zipfile
import difflib
import sys
jars = []
sizes = []

def list_jar(jar_file):
    global jars
    global sizes
    zf = zipfile.ZipFile(jar_file, 'r')
    try:
        lst = zf.infolist()
        for zi in lst:
            fn = zi.filename
            fn = fn.split("/")[-1]
            if fn.split(".")[-1] == "jar":
                jars.append(fn)
                sizes.append(zi.file_size)
    finally:
        zf.close()

def find_equal(files):
    f = files[:]
    for filename in f:
        print(filename)
        if "analy" in filename  :
            print(filename)
        f.remove(filename)
        if difflib.get_close_matches(filename, f, cutoff=0.99):
            sys.stderr.write("Equal dependencies: %s\n" % "".join(difflib.get_close_matches(filename, f, cutoff=0.99)))
            sys.stderr.flush()


def find_similar(files):
    words = {}
    for filename in files:
        files.remove(filename)
        words[filename] = difflib.get_close_matches(filename, files, cutoff=0.90)
        words = {k:v for k,v in words.items() if v}
    for filename in sorted(words):
        wordslist = list(words)
        if filename not in words:
            continue
        wordslist.remove(filename)
        for match in difflib.get_close_matches(filename, wordslist, cutoff=0.80):
            if match not in words[filename]:
                words[filename].append(match)
            for each in words[match]:
                words[filename].append(each)
            words.pop(match)
        words[filename].sort()
    return words

def print_similar(file_dict):
    for file in sorted(file_dict):
        if len(file_dict[file]) != 0:
            print(file, ":", sep="")
            print("\t", end="")
            print("\n\t".join(file_dict[file]), sep="\n")

if __name__=="__main__":
    list_jar(r"D:\nv\work\rep\appserver-3.11.0\workspace\server-app\argus-enterprise\target\argus-enterprise-3.11.0-с.ear")
    #print("\n".join(sorted(jars)), len(jars) == len(set(jars)), "\n".join(map(str,sorted(sizes))), sep='\n')
    #print(len(sizes) == len(set(sizes)))
    #print("\n".join(sorted(jars)))
    find_equal(jars)
    print_similar(find_similar(jars))