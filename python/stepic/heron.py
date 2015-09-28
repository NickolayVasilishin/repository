def p(a):
    return (a[0]+a[1]+a[2])/2

def S(a):
    per = p(a)
    return(per*(per-a[0])*(per-a[1])*(per-a[2]))**0.5

if __name__ == "__main__":
    print(S(int(input()), int(input()), int(input())))
