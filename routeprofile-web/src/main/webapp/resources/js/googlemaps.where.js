/**
 * 
 */
navigator.geolocation.getCurrentPosition(function(position) {
    //lon      
    var lon = position.coords.longitude;
    //lat
    var lat = position.coords.latitude;
    myRemoteCommand([{name:'latitude', value:lat}, {name:'longitude', value:lon},]);
}, function(defaultLocation) {
    //lon      
    var lon = -8.39730;
    //lat
    var lat = 43.34463;
    myRemoteCommand([{name:'latitude', value:lat}, {name:'longitude', value:lon},]);	
});