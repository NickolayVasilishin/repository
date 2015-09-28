from zipfile import *
import re

archive = ZipFile(r"C:\Users\n.vasilishin\Downloads\channel.zip")
ext = ".txt"
num = "90052"
comments = []
try:
        while(1):
                with archive.open(num + ext) as file:
                        line = file.read(100)
                        print(line)
                        line = line.decode("UTF-8")
                        num = re.search("nothing is (\d+)", line).group(1)
                        comments.append(archive.getinfo(num + ext).comment.decode("UTF-8"))
except AttributeError:
        print("".join(comments))
