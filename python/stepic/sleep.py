overSleep = "Пересып"
lackOfSleep = "Недосып"
normalSleep = "Это нормально"

minimum, maximum, sleeped = range(3)
time = list(map(int, [input(), input(), input()]))

if time[sleeped] < time[minimum]:
    print(lackOfSleep)
elif time[sleeped] > time[maximum]:
    print(overSleep)
else:
    print(normalSleep)
