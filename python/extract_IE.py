import urllib.request
import re

h4Pattern = re.compile(r'<h4>(.*)</h4>', re.IGNORECASE)
userAgentPattern = re.compile(r'<p class="g-c-s">(.*)</p>', re.IGNORECASE)

resuilt = []
browser = ''
string = ''
with urllib.request.urlopen("http://www.zytrax.com/tech/web/msie-history.html") as page:
    for line in page:
        try:
            line = line.decode("UTF-8")
        except UnicodeDecodeError:
            'There is one broken line'
            line = (line[:73]+line[74:]).decode("UTF-8")
        match = h4Pattern.match(line)
        if match:
            browser = match.group(1)[3:]
            match = None
        match = userAgentPattern.match(line)
        if match:
            string = match.group(1)
            match = None
            resuilt.append((browser, string))
        

file = open("IE-user-agents1.xml", 'w')
for browser, agent in resuilt:
    file.write("<" + browser + "/>\n\t" + agent + "\n")
file.close()
