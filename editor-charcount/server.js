'use strict';

const express = require('express');

const PORT = 80;
const HOST = '0.0.0.0';

var charcount = require('./charcount');

const app = express();
app.get('/', (req,res) => {

    var output = {
        'error': false,
        'string': '',
        'answer': 0
    };

    res.setHeader('Content-Type', 'application/json');
    res.setHeader('Access-Control-Allow-Origin', '*')
    try {
        var text = req.query.text;
        var answer = charcount.counter(text);
        output.string = 'Contains '+answer+ ' characters';
        output.answer = answer;
    } catch (e){
        output.string = "text query param was not passed properly.";
        output.answer = -1;
        output.error = true;
        res.status(400)
    }
    res.end(JSON.stringify(output));



});

app.listen(PORT, HOST);
