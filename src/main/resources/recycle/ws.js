function createMarker(map, id){
    var icon = {
        path : busPath,
        fillColor: '#FF0000',
        fillOpacity: .6,
        // anchor: new google.maps.Point(12,-290),
        strokeWeight: 0,
        scale: .1,
        // anchor: new google.maps.Point(200,250),
        anchor: {x: 200, y:250},
//            scaledSize: new google.maps.Size(5,5),
        rotation: rotation
    };

    var marker = new google.maps.Marker({
        position: positions[0],
        map: map,
        icon: icon,
        label: {
            text: 'A',
            color: 'red',
        }
    });

    var marker2 = new google.maps.Marker({
        position: positions[0],
        map: map,
        icon:{
            path: "M-1,0a1,1 0 1,0 2,0a1,1 0 1,0 -2,0z",
            fillColor: '#0000FF',
        },
        label: "B"
    });
    // markers.push(beachMarker);
    markers[id] = marker;
    return marker;
}