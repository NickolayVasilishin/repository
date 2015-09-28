def p(a):
    return (a[0]+a[1]+a[2])/2

def S(a):
    per = p(a)
    return(per*(per-a[0])*(per-a[1])*(per-a[2]))**0.5

def triangle():
    return S((int(input()), int(input()), int(input())))
def rectangle():
    return int(input()) * int(input())
def circle():
    return 3.14*(int(input())**2)

def solve(t):
    funcs = {"треугольник":triangle, "прямоугольник":rectangle, "круг":circle}
    return funcs[t]()

print(solve(input()))
