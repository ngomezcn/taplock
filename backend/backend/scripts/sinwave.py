import math
import time
import random

# iTap
seed = 3453459


# List of actual generateds
ocuped_id = [1,2,3,4,40,40]

def auth(id=0,key=0):
    formula = int((seed * math.sin(2 * math.pi * 50 * id/8000)))+id 

    if formula == key:
        return True
    else:
        return False
    

def generate():
    id = random.randint(0, 70000)
    while id in ocuped_id:
        id = random.randint(0, 70000)
    ocuped_id.append(id)
    
    key = int((seed * math.sin(2 * math.pi * 50 * id/8000)))+id
    
    return key,id
    
key,id = generate()
        
print(auth(key=key,id=id))
print("KEY:",key)
print("ID:",id)
