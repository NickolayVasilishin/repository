# -*- coding: utf-8 -*-
"""
Created on Thu Nov 26 21:06:57 2015

@author: n.vasilishin
"""

import zipfile
import io
import sys
import os
import difflib

def check_fat_jar(jarfile, parent = None):
    if parent:
        zfiledata = io.BytesIO(parent.read(jarfile))
        zf = zipfile.ZipFile(zfiledata)
    else:
        zf = zipfile.ZipFile(jarfile)
    lst = zf.infolist()
    for zi in lst:
        if zi.filename.split(".")[-1] == "jar":
            sys.stderr.write("FAT JAR: %s\n" % zi.filename)
            sys.stderr.flush()
            return True
    return False
        
#Format: [(jarname, path, size, CRC), ...]
def list_jewar(jewar_file, parent = None):
    jars = []
    parent_or_none = jewar_file.split("\\")[-1] + "/"
    #just open ear
    if not parent:
        zf = zipfile.ZipFile(jewar_file, 'r')
    #if we found war inside ear, open it and build paths with name of this war
    else:
        parent_or_none = parent.filename.split("\\")[-1] + "/" + jewar_file + "/"
        zfiledata = io.BytesIO(parent.read(jewar_file))
        zf = zipfile.ZipFile(zfiledata)
    try:
        #get list of files and their properties
        lst = zf.infolist()
        for zi in lst:
            fn = zi.filename
            fn = fn.split("/")[-1]
            #collect files and properties in [(file, path, size, CRC),...] list
            if fn.split(".")[-1] == "jar":
                if check_fat_jar(zi.filename, zf):
                    jars.extend(list_jewar(zi.filename, zf))
                jars.append((fn, parent_or_none + ("/".join(zi.filename.split("/")[:-1]) + "/"), zi.file_size, zi.CRC))
            if fn.split(".")[-1] == "war":
                jars.extend(list_jewar(zi.filename, zf))
    finally:
        zf.close()
    return jars
    
#Format: [(jarname, path, size, CRC), ...]
def list_modules(modules_dir):
    jars = []
    with zipfile.ZipFile(os.path.join(modules_dir, "modules"), 'w') as ear_zip:
        for subdir, dirs, files in os.walk(modules_dir):
            for file in files:
                if file.split(".")[-1] == "jar":
                    ear_zip.write(os.path.join(subdir, file), arcname=subdir[len(modules_dir):]+file)
    #TODO delete modules
    return list_jewar(os.path.join(modules_dir, "modules"))
        
#prints list of jars in user-friendly format        
def print_jewar(jewar, item_n = None, err = False, premessage = None, as_table = False, to_file = None):    
    line_format = "{1:<80}{0:<80}{2:<40}{3:<40}"
    if as_table:
        line_format = "|{1}|{0}|{2}|{3}|"    
    #print list of jars via stderr
    if err:
        prnt = sys.stderr.write
        line_format += "\n"
    elif to_file:
        to_file = open(to_file, 'w')
        prnt = to_file.write
    else:
        prnt = print
    if premessage:
        prnt(premessage)
    if not item_n:
        if as_table:
            prnt(("\n\n||{0}||{1}||{2}||{3}||\n").format("Path", "Name", "Size", "CRC"))
        else:
            prnt(("\n\n" + line_format + "\n\n").format("Path", "Name", "Size", "CRC"))
    
    eq_items = []
    for item in jewar:
        if not eq_items:
            eq_items.append(item)
        else:
            if has_any_equal(item, eq_items[-1]):
                eq_items.append(item)
            else:
                for i in eq_items:
                    if not item_n and item_n != 0:
                        prnt(line_format.format(*i))
                    elif type(item_n) == int:
                            prnt(i[item_n])
                    elif type(item_n) == list:
                        for n in item_n:
                            prnt(i[n], end="\t")
                        prnt()
                eq_items = [item]
                if not as_table:
                    prnt("\n")
                else:
                    prnt(line_format.format(*(["----"]*4)))
    if to_file:
        to_file.close()

#used in print_jewar to visually seperate equal jars
def has_any_equal(item1, item2):
    for pos in range(4):
        if item1[pos] == item2[pos]:
            return True
    return False
       
#look for jars with equal CRC
def has_duplicate_CRC(ear):
    CRCs = [each[3] for each in ear]
    similar_jars = []
    if len(CRCs) != len(set(CRCs)):
        duplicate_CRCs = list(set([x for x in CRCs if CRCs.count(x) > 1]))
        for each in ear:
            for crc in duplicate_CRCs:
                if each[3] == crc:
                    similar_jars.append(each)
    return similar_jars

def has_similar_names(ear):
    names = list(set([item[0] for item in ear]))
    #names = [item[0] for item in ear]
    #pos = 0
    s_names = []
    for each in names:
        #names.remove(each)
        s_names.append(difflib.get_close_matches(each, names, cutoff=0.90))
        if len(s_names[-1]) != 1:
            names.remove(each)
    s_names = [i for i in s_names if len(i)>1]
    return s_names
       
def check_similar(ear, print_as_table = False):
    dups = has_duplicate_CRC(ear)
    if dups:
        print_jewar(sorted(dups, key = lambda dups: dups[3]), err=True, premessage = "Found %d equal by CRC jars." % len(dups), as_table = print_as_table)
    else:
        print("No duplicate CRCs were found")
   
         

       
if __name__=="__main__":
    ear_path = r"D:\nv\work\rep\appserver-3.11.0\workspace\server-app\argus-enterprise\target\argus-enterprise-3.11.0.ear"
    modules_path = r"D:\nv\work\rep\appserver-3.11.0\workspace\server-app\argus-enterprise\target\modul"
    interactive = False
    if len(sys.argv) == 4:
        if "-i" in sys.argv:
            interactive = True
            sys.argv.remove("-i")
    if len(sys.argv) == 3:
        ear_path = sys.argv[1]
        modules_path = sys.argv[2]
        
    print("Collecting jars from %s." % ear_path.split("\\")[-1])
    ear = list_jewar(ear_path)
    print("Collecting jars from modules.")
    modules = list_modules(modules_path)
    print("Merging jar lists.")
    ear.extend(modules)
    print_jewar(sorted(ear, key = lambda ear: ear[1]), to_file="D:\ear.txt")
    check_similar(ear)
    sn = has_similar_names(ear)
    sn_checked = []
    if interactive:
        for name in sn:
            print("\t\t".join(name))
            if "y" in input("eq?"):
                sn_checked.append(name)
                
    #with open("D:\out.txt", "w") as out:
     #   for each in sorted(sn_checked, key=lambda x: x[0]):
      #      out.write("[" + "\t".join(each) + "]\n")
    
    
    
    #TODO REFACTOR
    #TODO FULL PATHS OF NAME-SIMILAR DEPENDENCIES
    #TODO OPTIMIZE NAME SEARCH
#    with open("D:\similar_deps.txt", 'r') as file:
#        l = "{1:<80}{0:<80}{2:<40}{3:<40}"
#        for line in file:
#            line = line[1:-2]
#            dep = line.split("\t")
#            for each in ear:
#                full_deps = [None]*2
#                if dep[0] in each[0]:
#                    full_deps[0] = each
#                elif dep[1] in each[0]:
#                    full_deps[1] = each
#                if full_deps[0] and full_deps[1]:
#                    print(l.format(*full_deps[0]))
#                    print(l.format(*full_deps[1]))
#            
                
    
    print("Done.")