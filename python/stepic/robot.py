word = "программист"
#num = int(input())
ending = {0:"ов", 1:"", 2:"а", 3:"а", 4:"а", 5:"ов", 6:"ов", 7:"ов", 8:"ов", 9:"ов"}
for num in range(0, 1001):
    if 10 <= num%100 < 20:
        print(num, word + ending[0])
    else:
        print(num, word + ending[num%10])
