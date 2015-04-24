import json
import urllib2
import sys

def add_card(card):
    print(json.dumps(card))
    req = urllib2.Request('http://ah-jeez.herokuapp.com/cards')
    req.add_header('Authorization','Token CAAEwuPkFMgQBAAJwVMb3qVBS3QlQb96') 
    req.add_header('Content-Type','application/json')
    response = urllib2.urlopen(req, json.dumps(card))
    print(response.read())
    response.close()

user = {
    "user": {
            "fbId": "5555555555",
            "fbToken": "CAAEwuPkFMgQBAAJwVMb3qVBS3QlQb96",
            "name": "Card Creator 4000",
            "pic": "http://www.mypic.com"
        }
    }

req = urllib2.Request('http://ah-jeez.herokuapp.com/users/login')
req.add_header('Content-Type','application/json')
response = urllib2.urlopen(req, json.dumps(user))
obj = json.loads(response.read())

user_id = obj['user']['id']

card = {
    "card": {
        "text": "Fear itself",
        "type": "white",
        "userId": user_id
    }
}

wfile = open('white_cards.txt','r')

for line in wfile:
    card['card']['text'] = line.strip()
    add_card(card)

wfile.close()

bfile = open('black_cards.txt','r')

card['card']['type'] = "black"
for line in bfile:
    card['card']['text'] = line.strip()
    add_card(card)

bfile.close()
