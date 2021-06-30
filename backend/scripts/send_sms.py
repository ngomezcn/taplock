# Download the helper library from https://www.twilio.com/docs/python/install
import os
from twilio.rest import Client

# Find your Account SID and Auth Token at twilio.com/console
# and set the environment variables. See http://twil.io/secure
account_sid = 'AC278dc5f07e5365b761bdfa330234775c'
auth_token = '8ec6177d4f1f29fd0fb047d7cdafd080'
client = Client(account_sid, auth_token)

message = client.messages.create(
                     body="hola ke ase tu wendyyyyyyy",
                     from_='+15709084110',
                     to='+34611150934'
                 )

print(message.sid)
