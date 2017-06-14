<!DOCTYPE html>
<html>
<head>
    <title>WebSocket Chat</title>
    <link href="/webjars/bootstrap/css/bootstrap.min.css" rel="stylesheet">
    <script src="/webjars/jquery/jquery.min.js"></script>
    <script src="/webjars/sockjs-client/sockjs.min.js"></script>
    <script src="/webjars/stomp-websocket/stomp.min.js"></script>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
    <link href="/css/main.css" rel="stylesheet">
    <link href="/css/w3.css" rel="stylesheet">
    <script src="/js/ws.js"></script>

</head>
<body class="w3-light-grey">

<!-- Sidebar/menu -->
<nav class="w3-sidebar w3-bar-block w3-light-grey w3-animate-left w3-top w3-text-grey w3-large" style="z-index:3;width:250px;font-weight:bold;display:none;left:0;" id="mySidebar">
    <a href="javascript:void()" onclick="w3_close()" class="w3-bar-item w3-button w3-center w3-padding-32">CLOSE</a>
    <a href="#" onclick="w3_close()" class="w3-bar-item w3-button w3-center w3-padding-16">PORTFOLIO</a>
    <a href="#about" onclick="w3_close()" class="w3-bar-item w3-button w3-center w3-padding-16">ABOUT ME</a>
    <a href="#contact" onclick="w3_close()" class="w3-bar-item w3-button w3-center w3-padding-16">CONTACT</a>
</nav>


<nav class="w3-gray w3-animate-right w3-small" style="padding-top:43px;right:0;z-index:2;position:fixed!important;z-index:1;overflow:auto;width:140px" >
    <a href="javascript:void(0)" onclick="routeSelectionToggle()"
       class="w3-button w3-text-dark-grey w3-medium w3-display-topmiddle" style="padding:0 0 0 0px;">
        Route Selection<br>
        <i class="fa fa-angle-double-down" id = "route-selection-expand"></i>
        <i class="fa fa-angle-double-up" id = "route-selection-collapse"></i>
    </a>

    <div class="w3-bar-block w3-left" >

        <form id="route-selection-panel">
            <#--<span class="route-item">-->
                <#--<input type="checkbox" name="vehicle" value="random" class="" > random-->
            <#--</span>-->

            <#--<span class="w3-bar-item w3-button w3-text-grey">-->
                <#--<input type="checkbox" name="vehicle" value="uniform-speed" class="route-selection" > uniform-speed-->
            <#--</span>-->

            <#list defaultRouteNames as name>
                <span class="route-item">
                    <input type="checkbox" name="vehicle" value=${name} checked > ${name}
                </span>
            </#list>

        </form>
    </div>
</nav>

<!-- Top menu on small screens -->
<header class="w3-container w3-top w3-white w3-large w3-padding-small">
    <a href="javascript:void(0)" class="w3-left w3-button w3-white" onclick="w3_open()">☰</a>
    <div class="w3-center w3-padding">Baltimore Public Transition Tracker</div>
</header>

<!-- Overlay effect when opening sidebar on small screens -->
<div class="w3-overlay w3-animate-opacity" onclick="w3_close()" style="cursor:pointer" title="close side menu" id="myOverlay"></div>

<!-- !PAGE CONTENT! -->
<div class="w3-main w3-content" style="max-width:1600px;margin-top:50px">
    <div id="map"></div>
    <div id="messages"></div>
</div>

<footer class="w3-container w3-padding-16 w3-center w3-opacity w3-light-grey w3-xlarge">
    <i class="fa fa-facebook-official w3-hover-opacity"></i>
    <i class="fa fa-instagram w3-hover-opacity"></i>
    <i class="fa fa-snapchat w3-hover-opacity"></i>
    <i class="fa fa-pinterest-p w3-hover-opacity"></i>
    <i class="fa fa-twitter w3-hover-opacity"></i>
    <i class="fa fa-linkedin w3-hover-opacity"></i>
    <i class="fa fa-github w3-hover-opacity"></i>
    <p class="w3-medium">Powered by <a href="https://www.w3schools.com/w3css/default.asp" target="_blank">w3.css</a></p>
</footer>

<#--<div id="main-content" class="container">-->
<#--<div class="row">-->
<#--<div class="col-md-12 space-bottom10">-->
<#--<form class="form-inline">-->
<#--<div class="form-group">-->
<#--<input type="checkbox" name="vehicle" value="random" class="route-selection" > random-->
<#--&lt;#&ndash;<input type="checkbox" name="vehicle" value="Car" class="route-selection" checked> Car&ndash;&gt;-->
<#--text-->
<#--</div>-->
<#--</form>-->
<#--</div>-->

<#--</div>-->

<#--</div>-->





<script async="true" defer="true"
        src="https://maps.googleapis.com/maps/api/js?key=AIzaSyCoRf6Z8U9X6jIhpZV_K6_u5IHVg637qSA&callback=initMap">
</script>

<script>


    var image = '/img/travel_bus.png';
    var positions = [{lat: 39.315770, lng: -76.610532}, {lat: 39.325770, lng: -76.610532}, {lat: 39.335770, lng: -76.610532}];
    var markers = {};

    function showMessage(mesg) {
        $('#messages').append(mesg);
    }

    function initMap(){
        console.log("init map");
        var map = new google.maps.Map(document.getElementById('map'), {
            zoom: 13,
            center: {lat: 39.315770, lng: -76.610532}
        });
//        var id = "1";
//        markers[id] = createMarker(map, id);
        <#list defaultRouteNames as id>
            markers["${id}"] = createMarker(map, "${id}");
        </#list>

    }




    $(function() {
        'use strict';
        var ws = new WebSocket("ws://localhost:8081/route");

        // var ws = Stomp.over(socket);
        var count = 1;
        ws.onmessage = function (event) {
            console.log("onmessage");
            if (event){
                var message = JSON.parse(event.data);
                updateMarker(message);
                if (count < 50) {
                    console.log('received message : ' + event.data);
                    count++;
                }
            }
        };

        ws.onclose = function (event) {
            console.log("ws closed");
            deleteMarkers(markers);
        }


        $('.route-selection').bind("click", function() {
            console.log("checkbox clicked");
            sendRouteName(this);
        });

    });
</script>

<script>

    // Script to open and close sidebar
    function w3_open() {
        document.getElementById("mySidebar").style.display = "block";
        document.getElementById("myOverlay").style.display = "block";
    }

    function w3_close() {
        document.getElementById("mySidebar").style.display = "none";
        document.getElementById("myOverlay").style.display = "none";
    }

    var expandIcon = $("#route-selection-expand");
    var collapseIcon = $("#route-selection-collapse");
    var routeSelectionPanel = $("#route-selection-panel");
    var routeSelectionExpanded = true;
    function routeSelectionExpand() {
        expandIcon.hide();
        collapseIcon.show();
        routeSelectionPanel.show();
        routeSelectionExpanded = true;
    }
    function routeSelectionCollapse() {
        expandIcon.show();
        collapseIcon.hide();
        routeSelectionPanel.hide();
        routeSelectionExpanded = false;
    }
    function routeSelectionToggle() {
        if (routeSelectionExpanded)
            routeSelectionCollapse();
        else
            routeSelectionExpand();
    }
    routeSelectionExpand();

</script>

</body>


</body>
</html>
