<!DOCTYPE html>
<html>
<head>
    <title>WebSocket Chat</title>
    <link href="/webjars/bootstrap/css/bootstrap.min.css" rel="stylesheet">
    <script src="/webjars/jquery/jquery.min.js"></script>
    <script src="/webjars/sockjs-client/sockjs.min.js"></script>
    <script src="/webjars/stomp-websocket/stomp.min.js"></script>
    <link href="/css/main.css" rel="stylesheet">
    <#--<script src="/js/ws.js"></script>-->
    <style>
        /* Always set the map height explicitly to define the size of the div
         * element that contains the map. */
        #map {
            height: 500px;
        }
        /* Optional: Makes the sample page fill the window. */
        html, body {
            height: 100%;
            margin: 0;
            padding: 0;
        }
    </style>
    <script>
        function initMap(){
            console.log("init map");
            var map = new google.maps.Map(document.getElementById('map'), {
                zoom: 13,
                center: {lat: 39.315770, lng: -76.610532}
            });
            setMarker(map, "random");
        }

        var image = '/img/travel_bus.png';
        var positions = [{lat: 39.315770, lng: -76.610532}, {lat: 39.325770, lng: -76.610532}, {lat: 39.335770, lng: -76.610532}];
        var markers = {};

        function showMessage(mesg) {
            $('#messages').append(mesg);
        }

        function setMarker(map, id){
            if (id in markers)
                return markers[id];
            var marker = new google.maps.Marker({
                position: positions[0],
                map: map,
                icon: image
            });
            // markers.push(beachMarker);
            markers[id] = marker;
            return marker;
        }
        
        function updateMarker(data) {
            // console.log("updateMarker: " + data);
            var id = data["id"];
            var marker = markers[id];
            var coordinate = data["coordinate"];
            var latlng = new google.maps.LatLng(coordinate['lat'], coordinate['lng']);
            marker.setPosition(latlng);
        }

        $(function() {
            'use strict';
            var ws = new WebSocket("ws://localhost:8081/route");

            // var ws = Stomp.over(socket);
            var count = 1;
            ws.onmessage = function (data) {
                // console.log('received message : ' + data.data);
                if (data){
                    console.log('received message : ' + data.data);
                    if (count < 50) {
                        var message = JSON.parse(data.data);
                        updateMarker(message);
                        count++;
                    }
                }
            };

            ws.onclose = function (p1) {
                console.log("ws closed");
            }

            function sendRouteName(checkbox) {
                // wait until ws is open
                var timeInterval = 100; // in millisecond
                if (ws.readyState != ws.OPEN){
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


//            function relocateMarker(marker) {
//                $.ajax({
//                    type: 'GET',
//                    url: '/baltimore',
//                    cache: false,
//                    success: function (result) {
//                        // console.log(result);
//                        var latlng = new google.maps.LatLng(result['lat'], result['lng']);
//                        marker.setPosition(latlng);
//                        // console.log(latlng);
//                    }
//                });
//            }

        });
    </script>
</head>
<body>
<div id="main-content" class="container">
    <div class="row">
        <div class="col-md-12 space-bottom10">
            <form class="form-inline">
                <div class="form-group">
                    <input type="checkbox" name="vehicle" value="random" class="route-selection" > random
                    <#--<input type="checkbox" name="vehicle" value="Car" class="route-selection" checked> Car-->
                </div>
            </form>
        </div>

    </div>
    <#--<div class="row">-->
        <#--<div class="col-md-12">-->
            <#--<table id="conversation" class="table table-striped">-->
                <#--<thead>-->
                <#--<tr>-->
                    <#--<th width="10%">From</th>-->
                    <#--<th width="15%">Topic</th>-->
                    <#--<th width="60%">Message</th>-->
                    <#--<th width="10%">Time</th>-->
                <#--</tr>-->
                <#--</thead>-->
                <#--<tbody id="messages">-->
                <#--</tbody>-->
            <#--</table>-->
        <#--</div>-->
    <#--</div>-->

    <div id="map"></div>
    <div id="messages"></div>

</div>

<script async="true" defer="true"
        src="https://maps.googleapis.com/maps/api/js?key=AIzaSyCoRf6Z8U9X6jIhpZV_K6_u5IHVg637qSA&callback=initMap">
</script>

<script>

    // This example adds a marker to indicate the position of Bondi Beach in Sydney,
    // Australia.

</script>

</body>


</body>
</html>
