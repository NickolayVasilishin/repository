import os

fileset = set()
repeated_files = []
project = r"D:\nv\work\rep\appserver-3.9.0\appserver\workspace"

def scan(directory):
    for subdir, dirs, files in os.walk(directory):
        map(scan, filter((lambda x: "target" not in x), dirs))
        for file in files:
            if file.endswith("png") or file.endswith("jar") or file.endswith("gif") or file.endswith("css") or file.endswith("html"): 
                continue
            if file in fileset:
                if file not in repeated_files:
                    repeated_files.append(file)
            else:
                fileset.add(file)



        
if __name__ == '__main__':

    scan(project)
    for file in sorted(repeated_files):
        print(file)
