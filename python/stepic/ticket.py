num = input()
if sum(map(int, list(num[:3]))) == sum(map(int, list(num[3:]))):
    print("Счастливый")
else:
    print("Обычный")
