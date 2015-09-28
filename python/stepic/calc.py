#+, -, /, *, mod, pow, div, 

def add(a,b):
    return a + b
def sub(a,b):
    return a - b
def multiply(a,b):
    return a * b
def divide(a,b):
    if b != 0:
        return a / b
    else:
        return "Деление на 0!"
def mod(a,b):
    if b != 0:
        return a % b
    else:
        return "Деление на 0!"
def powr(a,b):
    return a**b
def div(a,b):
    if b != 0:
        return a // b
    else:
        return "Деление на 0!"


def eval(exp):
    funcs = {'+':add, '-':sub, '/':divide, '*':multiply, 'mod':mod, 'pow':powr, 'div':div}
    return(funcs[exp[2]](exp[0],exp[1]))

exp = (float(input()), float(input()), input())
print(eval(exp))
