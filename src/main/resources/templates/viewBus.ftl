<!DOCTYPE html>
<html>
  <head>
    <meta name="viewport" content="initial-scale=1.0, user-scalable=no"/>
    <meta charset="utf-8"/>
    <title>Simple icons</title>
    <style>
      /* Always set the map height explicitly to define the size of the div
       * element that contains the map. */
      #map {
        height: 100%;
      }
      /* Optional: Makes the sample page fill the window. */
      html, body {
        height: 100%;
        margin: 0;
        padding: 0;
      }
    </style>
    <script type="text/javascript">
      var positions = [{lat: 39.315770, lng: -76.610532}, {lat: 39.325770, lng: -76.610532}, {lat: 39.335770, lng: -76.610532}];
    </script>
    <script src="/js/jquery-3.2.1.min.js" type="text/javascript"></script>
  </head>
  <body>
    <div id="map"></div>
    <script>

      // This example adds a marker to indicate the position of Bondi Beach in Sydney,
      // Australia.
      function initMap() {
        var map = new google.maps.Map(document.getElementById('map'), {
          zoom: 13,
          center: {lat: 39.315770, lng: -76.610532}
        });
        var marker = setMarker(map);
        setInterval(function() {relocateMarker(marker);}, 3000);
      }
      var image = '/img/travel_bus.png';
      function setMarker(map){
        var marker = new google.maps.Marker({
          position: positions[0],
          map: map,
          icon: image
        });
        // markers.push(beachMarker);
        return marker;
      }
      function relocateMarker(marker) {
          $.ajax({
              type: 'GET',
              url: '/baltimore',
              cache: false,
              success: function (result) {
                  // console.log(result);
                  var latlng = new google.maps.LatLng(result['lat'], result['lng']);
                  marker.setPosition(latlng);
                  // console.log(latlng);
              }
          });
      }
    </script>
    <script async="true" defer="true"
    src="https://maps.googleapis.com/maps/api/js?key=AIzaSyCoRf6Z8U9X6jIhpZV_K6_u5IHVg637qSA&callback=initMap">
    </script>
  </body>
</html>