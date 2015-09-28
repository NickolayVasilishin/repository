import string

normal, shifted = string.ascii_lowercase, string.ascii_lowercase[2:] + string.ascii_lowercase[:2]
print(input("Enter shifted text\n").translate("".maketrans(shifted, normal)))
