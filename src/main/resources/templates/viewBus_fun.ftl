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
    <script src="/js/hzmap.js" type="text/javascript"></script>
  </head>
  <body>
    <div id="map"></div>
    <script>
       settings = {'initPosition': {lat: 39.315770, lng: -76.610532}, 'image':'/img/green_dot.png',
                   'url': '/route/homeToBWI', 'interval': 100};
      function initMap() {
        var map = new google.maps.Map(document.getElementById('map'), {
          zoom: 13,
          center: {lat: 39.315770, lng: -76.610532}
        });
        bus(map, settings);
      }
    </script>
    <script async="true" defer="true"
    src="https://maps.googleapis.com/maps/api/js?key=AIzaSyCoRf6Z8U9X6jIhpZV_K6_u5IHVg637qSA&callback=initMap">
    </script>
  </body>
</html>