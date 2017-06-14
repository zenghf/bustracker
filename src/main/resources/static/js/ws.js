var busPath = "M98.644,275.387c-11.369,0-20.618,9.249-20.618,20.618c0,11.369,9.25,20.618,20.618,20.618s20.618-9.249,20.618-20.618   C119.262,284.636,110.012,275.387,98.644,275.387z M98.644,301.623c-3.098,0-5.618-2.521-5.618-5.618s2.521-5.618,5.618-5.618   s5.618,2.52,5.618,5.618S101.741,301.623,98.644,301.623z M311.876,275.387c-11.369,0-20.618,9.249-20.618,20.618c0,11.369,9.25,20.618,20.618,20.618s20.618-9.249,20.618-20.618   C332.495,284.636,323.245,275.387,311.876,275.387z M311.876,301.623c-3.098,0-5.618-2.521-5.618-5.618s2.521-5.618,5.618-5.618   s5.618,2.52,5.618,5.618S314.974,301.623,311.876,301.623z M401,259.826v-70.374v-76.644c0-23.871-19.421-43.292-43.292-43.292H76.773c-37.083,0-67.253,30.17-67.253,67.253v123.056   C3.744,264.164,0,271.071,0,278.836v0.92c0,13.096,10.654,23.75,23.75,23.75h30.533c3.583,21.253,22.105,37.497,44.361,37.497   s40.778-16.244,44.361-37.497h124.511c3.583,21.253,22.105,37.497,44.361,37.497s40.778-16.244,44.361-37.497h30.533   c13.096,0,23.75-10.654,23.75-23.75v-0.92C410.52,271.071,406.776,264.164,401,259.826z M386,181.952h-20.583   c-1.053,0-1.91-0.856-1.91-1.91v-57.824c0-1.053,0.856-1.91,1.91-1.91H386V181.952z M15,279.756v-0.92c0-4.83,3.925-8.76,8.75-8.76   h38.151c-3.817,5.393-6.477,11.655-7.619,18.43H23.75C18.925,288.506,15,284.581,15,279.756z M98.644,326.003   c-16.541,0-29.998-13.458-29.998-29.999s13.457-29.998,29.998-29.998s29.998,13.457,29.998,29.998S115.185,326.003,98.644,326.003z    M267.515,288.506h-124.51c-3.582-21.254-22.105-37.5-44.361-37.5c-6.657,0-12.974,1.464-18.665,4.07H24.52V136.77   c0-28.813,23.441-52.253,52.253-52.253h280.935c13.003,0,23.979,8.821,27.275,20.792h-19.567c-9.324,0-16.91,7.585-16.91,16.91   v57.824c0,9.324,7.585,16.91,16.91,16.91H386v58.125h-55.459c-5.69-2.605-12.008-4.07-18.665-4.07   C289.62,251.007,271.097,267.253,267.515,288.506z M311.876,326.003c-16.541,0-29.998-13.458-29.998-29.999   s13.457-29.998,29.998-29.998s29.998,13.457,29.998,29.998S328.417,326.003,311.876,326.003z M395.52,279.756   c0,4.825-3.925,8.75-8.75,8.75h-30.532c-1.142-6.775-3.802-13.037-7.619-18.43h38.151c4.825,0,8.75,3.93,8.75,8.76V279.756z M326.377,180.042v-57.824c0-9.324-7.585-16.91-16.91-16.91H78.542c-19.952,0-36.184,16.232-36.184,36.184v38.55   c0,9.324,7.585,16.91,16.91,16.91h250.2C318.792,196.952,326.377,189.366,326.377,180.042z M191.868,120.308h58.961v61.644h-58.961   V120.308z M176.868,181.952h-58.96v-61.644h58.96V181.952z M57.358,180.042v-38.55c0-11.681,9.503-21.184,21.184-21.184h24.366   v61.644h-43.64C58.214,181.952,57.358,181.095,57.358,180.042z M311.377,180.042c0,1.053-0.856,1.91-1.91,1.91h-43.639v-61.644   h43.639c1.053,0,1.91,0.856,1.91,1.91V180.042z M369.537,243.413c4.142,0,7.5-3.358,7.5-7.5v-6.667c0-4.142-3.358-7.5-7.5-7.5s-7.5,3.358-7.5,7.5v6.667   C362.037,240.055,365.395,243.413,369.537,243.413z";

function createMarker(map, id, icon, label){
    if (icon === undefined) {
        icon = {
            path: busPath,
            fillColor: '#FF0000',
            fillOpacity: .9,
            strokeWeight: 0,
            scale: .05,
            anchor: {x: 200, y: 250},
            rotation: 0
        };
    }
    if (label === undefined) {
        if (typeof id !== "string"){
            label = id;
            return;
        }
        label = id[0].toUpperCase();
        for (var i = 1; i < id.length; i++){
            if (!isNaN(id[i]))
                label += id[i];
        }
    }
    var marker = new google.maps.Marker({
        position: positions[0],
        map: map,
        icon: icon,
        label: {
            text: label,
            color: 'red',
        }
    });

    return marker;
}


function updateMarker(data) {
    var id = data["id"];
    var marker = markers[id];
    var coordinate = data["coordinate"];
    var prevPosition = marker.getPosition();
    var dLat = coordinate['lat'] - prevPosition.lat();
    var dLng = coordinate['lng'] - prevPosition.lng();
    var dx = dLng * Math.cos(coordinate['lat'] * Math.PI / 180.0);
    var dy = dLat;
    var rotation = -Math.atan2(dy, dx) * 180 / Math.PI;
    var icon = marker.getIcon();
    icon.rotation = rotation;
    marker.setIcon(icon);
    var latlng = new google.maps.LatLng(coordinate['lat'], coordinate['lng']);
    marker.setPosition(latlng);
}

function sendRouteName(ws, checkbox) {
    // wait until ws is open
    var timeInterval = 100; // in millisecond
    if (ws.readyState != ws.OPEN){
        setTimeout(function () {
            sendRouteName(checkbox);
        }, timeInterval);
    }
    ws.send(JSON.stringify({'route': checkbox.value, 'checked': checkbox.checked}));
}

function deleteMarkers(markers){
    for (var id in markers)
        markers[id].setMap(null);
    markers = {};
}

// $(function() {
//     'use strict';
//
//     var client;
//
//     function showMessage(mesg) {
//         $('#messages').append(mesg);
//     }
//
//     var ws = new WebSocket("ws://localhost:8081/route");
//
//     // var ws = Stomp.over(socket);
//     var count = 1;
//     ws.onmessage = function (data) {
//         // console.log('received message : ' + data.data);
//         if (data){
//             // console.log('received message : ' + data.data);
//             if (count < 50) {
//                 // var message = JSON.parse(data.data);
//                 showMessage(data.data);
//                 count++;
//             }
//         }
//     };
//
//     function sendRouteName(checkbox) {
//         // wait until ws is open
//         var timeInterval = 100; // in millisecond
//         while (ws.readyState != ws.OPEN){
//             setTimeout(function () {
//                 sendRouteName(checkbox);
//             }, timeInterval);
//         }
//         ws.send(JSON.stringify({'route': checkbox.value, 'checked': checkbox.checked}));
//     }
//     $('.route-selection').bind("click", function() {
//         console.log("checkbox clicked");
//         sendRouteName(this);
//     });
//
// });
