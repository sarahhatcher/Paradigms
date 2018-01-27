import random

VALIDM = [1,2,3,4,5,6,7,8]
VALIDT = ['A','B','C','D','E','F','G','H']
MAX = 8
boundM = []
boundT = []

def getMach(bound):
    mach = random.randint(0, MAX-1)

    if bound and (boundM.len() < MAX):
        while (mach in boundM):
            mach = random.randint(0, MAX-1)

    return mach

def getTask(bound):
    task = random.randint(0, MAX-1)

    if bound and (boundT.len() < MAX):
        while(task in boundT):
            task = random.randint(0, MAX-1)

    return VALIDT[task]

# Sets up name

name = input("Type in the name. No name causes error: ")

# Ask for Invalid Machine/Task generation

imtg = input("Make Invalid Machine/Task Error possible? (y to yes): ")

if imtg == 'y':
    VALIDM += [9, 10, 11]
    VALIDT += ['X','Y','Z']
    MAX = MAX + 3

# Sets up FPA

print("Requesting more than 8 FPA will result in FPA.")
fpaC = input("How many Forced Partial Assignment do you want? (default 0): ")
fpaE = input("Allow causing FPA error (y to yes): ")

fPA = []

if fpaC == "":
    fpaC = 0
else:
    fpaC = int(fpaC)

for i in range(fpaC):
    if fpaE == 'y':
        fPA += [(getMach(True), getTask(True))]
    else:
        fPA += [(getMach(False), getTask(False))]
        

# Sets up FM

fmC = input("How many Forbidden Machine do you want? (default 0): ")

fM = []

if fmC == "":
    fmC = 0
else:
    fmC = int(fmC)

for i in range(fmC):
    fM += [(getMach(False), getTask(False))]

# Sets up TNT

tntC = input("How many Too Near Task do you want? (default 0): ")

tNT = []

if tntC == "":
    tntC = 0
else:
    tntC = int(tntC)

for i in range(tntC):
    tNT += [(getTask(False), getTask(False))]

# Sets up the MP (May create random invalid MP.)
print("Build MP, may randomly create invalid MP with negative penalty.")
x = input("Task width of MP (Default 8): ")
y = input("Machine height of MP (Default 8): ")

if (x == ""):
    x = 8
else:
    x = int(x)
if (y == ""):
    y = 8
else:
    y = int(y)

mP = []

for i in range(y):
    subMP = []
    for j in range(x):
        subMP.append(random.randint(-1,99))
    mP.append(subMP)

# Sets up the TNP (May create random invalid MP.)
print("Build TNP, may randomly create invalid TNP with negative penalty.")
tnpC = input("How many Too Near Penalty do you want? (default 0): ")

tNP = []

if tnpC == "":
    tnpC = 0
else:
    tnpC = int(tnpC)

for i in range(tnpC):
    tNP += [(getTask(False), getTask(False), random.randint(-1,99))]

# Starts printing output.
print("Copy following text printed to the file---------------------------")

# Prints Name.

print("Name:")
print(name)

# Prints FPA.

print("\nforced partial assignment:")
for temp in fPA:
    a,b = (str(temp[0]), str(temp[1]))
    print("(" + a + "," + b + ")")

# Prints FM

print("\nforbidden machine:")
for temp in fM:
    a,b = (str(temp[0]), str(temp[1]))
    print("(" + a + "," + b + ")")

# Prints TNT

print("\ntoo-near tasks:")
for temp in tNT:
    a,b = (str(temp[0]), str(temp[1]))
    print("(" + a + "," + b + ")")

# Prints MP.

print("\nmachine penalties:")
for i in range(y):
    for j in range(x):
        print(mP[i][j], end=' ')
    print("")

# Prints TNP

print("\ntoo-near penalities:")
for temp in tNP:
    a,b,c = (str(temp[0]), str(temp[1]), str(temp[2]))
    print("(" + a + "," + b + "," + c + ")")