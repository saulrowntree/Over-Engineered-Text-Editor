function makeeditorrequest(URLType, URL, FallbackURL) {
    try {
        attemptRequest(URLType, URL, FallbackURL);
    }catch (error){
        tryFallback(URLType, FallbackURL)
    }
}

function attemptRequest(URLType, URL, FallbackURL){
    let xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function () {
        if (this.readyState === 4) {
            var j = JSON.parse(this.response);
            document.getElementById('output').value = j.answer;
            if (this.status !== 200) {
                tryFallback(URLType, FallbackURL)
            }
            togglebuttons('on')
        }
    };

    let url = URL + "/?type= blah" + "&text="
        + encodeURI(document.getElementById('content').value);
    xhttp.open("GET", url, true);
    xhttp.send();
    togglebuttons('off')
    document.getElementById("output").value = 'Loading...';
}

function tryFallback(URLType, URL){
    let xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function () {
        if (this.readyState === 4) {
            var j = JSON.parse(this.response);
            document.getElementById('output').value = j.answer;
            if (this.status !== 200) {
                alert(j.string);
            }
            togglebuttons('on')
        }
    };

    let url = URL + "/?type=" + URLType + "&text="
        + encodeURI(document.getElementById('content').value);
    xhttp.open("GET", url, true);
    xhttp.send();
    togglebuttons('off')
    document.getElementById("output").value = 'Loading...';
}

function savecontent(URL) {
    let xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function () {
        if (this.readyState === 4) {
            var j = JSON.parse(this.response);
            document.getElementById('output').value = j.answer;
            if (this.status !== 200) {
                alert(j.string);
            }
            togglebuttons('on')
        }
    };

    let url = URL + "/save?text=" + encodeURI(document.getElementById('content').value);
    xhttp.open("POST", url, true);
    xhttp.send();
    togglebuttons('off')
    document.getElementById("output").value = 'Loading...';
}

function loadcontent(URL) {
    let xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function () {
        if (this.readyState === 4) {
            var j = JSON.parse(this.response);
            document.getElementById('content').value = j.string;
            if (this.status !== 200) {
                alert(j.string);
            }
            togglebuttons('on')
            document.getElementById("output").value = j.answer;
        }
    };

    let url = URL + "/load?UId=" + encodeURI(document.getElementById('content').value);
    xhttp.open("GET", url, true);
    xhttp.send();
    togglebuttons('off')
    document.getElementById("output").value = 'Loading...';
}

function togglebuttons(onOff) {
    for (let button of document.getElementsByClassName('operation')) {
        button.disabled = onOff === 'off';
    }
}