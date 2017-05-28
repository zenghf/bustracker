function bus(map, settings){
    var marker = new google.maps.Marker({
        position: settings['initPosition'],
        map: map,
        icon: settings['image']
    });

    relocateMarker = function() {
        // console.log("----> relocateMarker");
        $.ajax({
            type: 'GET',
            url: settings['url'],
            cache: false,
            success: function (result) {
                // console.log(result);
                var latlng = new google.maps.LatLng(result['lat'], result['lng']);
                marker.setPosition(latlng);
                // console.log(latlng);
            }
        });
        // console.log("<---- relocateMarker");
    };
    // console.log(settings['interval']);
    setInterval(relocateMarker, settings['interval']);

}