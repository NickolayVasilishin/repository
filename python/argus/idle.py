with open(r"B:\programmersmount\jmeter\n.vasilishin\reports\2015-11-10\host\jc-cpu.csv") as file:
    lines = 0
    idle = 0
    file.readline()
    for line in file:
        lines += 1
        idle += 100 - int(line.split(",")[1])
    print(idle/lines, lines)
