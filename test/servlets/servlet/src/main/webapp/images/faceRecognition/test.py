while True:
    a = input()
    if a.isdigit():
        print(int(a) + 100)
    else:
        print(a + "100")