import atexit

from flask import Flask
from flask import request
from flask import Response
import requests as requests
import json

import palindromecount

app = Flask(__name__)

r = requests.post("http://serviceregistry.40226640.qpc.hal.davecutting.uk/add?service=palindromecount", data="")
serviceIDstr = str(r.content)
res = [int(i) for i in serviceIDstr if i.isdigit()]
serviceID = res[0]


def deregister_service():
    r = requests.delete("http://serviceregistry.40226640.qpc.hal.davecutting.uk/delete?service=palindromecount"+str(serviceID), data="deleting")

# Register the function to be called on exit
atexit.register(deregister_service)


@app.route('/')
def palindromes():
    t = request.args.get("text")
    if t is not None:
        response = generate_palindrome_response(t)

    else:
        response = generate_text_error_response()

    response.headers['Content-Type'] = 'application/json'
    response.headers['Access-Control-Allow-Origin'] = '*'
    return response


def generate_palindrome_response(text):
    answer = palindromecount.counter(text)

    r = {
        'error': False,
        'string': "Contains " + str(answer) + " palindromes.",
        'answer': answer
    }
    reply = json.dumps(r)
    response = Response(response=reply, status=200, mimetype="application/json")
    return response


def generate_text_error_response():
    r = {
        'error': True,
        'string': "The 'text' query parameter was not passed correctly.",
        'answer': -1
    }
    reply = json.dumps(r)
    response = Response(response=reply, status=400, mimetype="application/json")
    return response


if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000)
