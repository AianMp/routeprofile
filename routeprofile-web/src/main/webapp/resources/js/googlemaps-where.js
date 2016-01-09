/**
 * 
 */
navigator.geolocation.getCurrentPosition(function(position) {
    //lon      
    var lon = position.coords.longitude;
    //lat
    var lat = position.coords.latitude;
    myRemoteCommand([{name:'latitude', value:lat}, {name:'longitude', value:lon},]);
});