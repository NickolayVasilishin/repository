params = list(map(int, [input(), input(), input()]))
print(((params[0]+params[2])//60+params[1])%24)
print((params[0]+params[2])%60)
