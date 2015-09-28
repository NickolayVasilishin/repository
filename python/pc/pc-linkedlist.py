import urllib.request
import re
url = r"http://www.pythonchallenge.com/pc/def/linkedlist.php?nothing="
num = "12345"
while(1):
    try:
        with urllib.request.urlopen(url + num) as page:
            text = page.readall().decode("UTF-8")
            print(text)
            if text == "Yes. Divide by two and keep going.":
                num = str(int(num)//2)
            else:
                num = re.search("nothing is (\d+)", text).group(1)
    except AttributeError:
        print("Follow the " + re.search("\w+\.html", text).group())
        break
