$(function() {
    'use strict';

    var client;

    function showMessage(mesg) {
        $('#messages').append(mesg);
    }

    var ws = new WebSocket("ws://localhost:8081/route");

    // var ws = Stomp.over(socket);
    var count = 1;
    ws.onmessage = function (data) {
        // console.log('received message : ' + data.data);
        if (data){
            // console.log('received message : ' + data.data);
            if (count < 50) {
                // var message = JSON.parse(data.data);
                showMessage(data.data);
                count++;
            }
        }
    };

    function sendRouteName(checkbox) {
        // wait until ws is open
        var timeInterval = 100; // in millisecond
        while (ws.readyState != ws.OPEN){
            setTimeout(function () {
                sendRouteName(checkbox);
            }, timeInterval);
        }
        ws.send(JSON.stringify({'route': checkbox.value, 'checked': checkbox.checked}));
    }
    $('.route-selection').bind("click", function() {
        console.log("checkbox clicked");
        sendRouteName(this);
    });

});
