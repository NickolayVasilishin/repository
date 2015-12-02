import re

pattern = re.compile(r'<(\/?\w{1,})', re.IGNORECASE)

def validate(filename):
    problem = "no problems"
    tags = {}
    linecount = -1
    with open(filename, encoding="utf-8") as file:
        for line in file:
            linecount += 1
            for tag in re.findall(pattern, line):
                tagIsClosing = False
                if tag[0] == r"/":
                    tagIsClosing = True
                    tag = tag[1:]
                if tag not in tags:
                    tags[tag] = 1
                elif tagIsClosing:
                    tags[tag] -= 1
                    if tags[tag] == 1:
                        problem = linecount 
                else:
                    tags[tag] += 1
    for tag in tags:
        if tags[tag] != 0:
            pass
    return tags, problem, linecount

if __name__ == "__main__":
    print(validate(r"B:\programmersmount\jmeter\n.vasilishin\reports\2015-11-23\jmeter\tables\result1.xml"))
    print(validate(r"B:\programmersmount\jmeter\n.vasilishin\reports\2015-11-23\jmeter\tables\result2.xml"))
    print(validate(r"B:\programmersmount\jmeter\n.vasilishin\reports\2015-11-23\jmeter\tables\result3.xml"))
    print(validate(r"B:\programmersmount\jmeter\n.vasilishin\reports\2015-11-23\jmeter\tables\result5.xml"))
