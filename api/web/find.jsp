<%@ page import="com.theah64.pigeon.database.tables.Users" %>
<%@ page import="com.theah64.pigeon.models.User" %>
<%@ page import="java.util.Arrays" %>
<!DOCTYPE html>
<html lang="en">
<%
    final String[] parts = request.getRequestURI().split("find/");
    System.out.println("URI:" + request.getRequestURI());
    System.out.println("Parts: " + Arrays.toString(parts));
    String imei = parts.length >= 2 ? parts[1] : "";
    System.out.println("IMEI: " + imei);
    final User user = Users.getInstance().get(Users.COLUMN_IMEI, imei);
    if (user == null) {
        response.sendRedirect("/pigeon/error.jsp?title=Invalid user&message=Invalid imei passed " + imei);
        return;
    }
%>
<head>
    <meta charset="UTF-8">
    <title><%=user.getName()%> -Pigeon</title>
    <%@include file="common_headers.jsp" %>
    <script>
        var map;

        $(document).ready(function () {

            var socketUrl = "<%=(Connection.isDebugMode()
            ? "ws://localhost:8080/pigeon/v1/pigeon_socket/listener/"
            : "ws://theapache64.xyz:8080/pigeon/v1/pigeon_socket/listener/") + user.getId()%>";

            var webSocket = new WebSocket(socketUrl);
            var isDeviceResponded = false;
            var lastLat;
            var lastLon;
            var isFirstLocation = true;

            var poly;
            var marker;
            var gLatLon;
            var isMagnet = false;

            $("span#mapMagnet").click(function () {
                if (isMagnet) {
                    isMagnet = false;
                    $(this).addClass("inactive").removeClass("active");
                } else {
                    isMagnet = true;
                    $(this).addClass("active").removeClass("inactive");
                }
            });

            updateStatus("Opening socket...");

            webSocket.onopen = function (evnt) {

                updateStatus("Waiting for device response...");
                setStatusGood();

                setTimeout(function () {
                    if (!isDeviceResponded) {
                        setStatusBad();
                        updateStatus("Device OFFLINE");
                    }
                }, 10000);

            };

            webSocket.onmessage = function (evnt) {

                isDeviceResponded = true;
                var joResp = JSON.parse(evnt.data);
                console.log(joResp);

                var message = joResp.message;
                updateStatus(message);
                setStatusGood();

                if (joResp.error) {
                    setStatusBad();
                } else {

                    var joData = joResp.data;
                    if (joData) {
                        if (joData.type == 'location') {

                            if ($("p#status1").hasClass("blink")) {
                                $("p#status1").removeClass("blink");
                            }

                            if (!$("h3#speed span").is(":visible")) {
                                $("h3#speed span").fadeIn(500);
                            }

                            if (!$("span#mapMarker").is(":visible")) {
                                $("span#mapMarker").fadeIn(500);
                            }

                            if (!$("span#mapMagnet").is(":visible")) {
                                $("span#mapMagnet").fadeIn(500);
                            }


                            lastLat = joData.lat;
                            lastLon = joData.lon;

                            $("h6#status2").text(lastLat + "," + lastLon);
                            $("strong#strongSpeed").text(joData.speed);

                            if (marker) {
                                //Clearing old marker
                                marker.setMap(null);
                            }

                            //Moving map
                            gLatLon = new google.maps.LatLng(lastLat, lastLon);
                            marker = new google.maps.Marker({
                                position: gLatLon
                            });

                            if (isFirstLocation) {

                                isFirstLocation = false;

                                poly = new google.maps.Polyline({
                                    strokeColor: '#000000',
                                    strokeOpacity: 1.0,
                                    strokeWeight: 3
                                });

                                poly.setMap(map);

                                map.setZoom(12);
                                map.setCenter(marker.getPosition());
                                map.panTo(gLatLon);
                            }

                            if (isMagnet) {
                                map.panTo(gLatLon);
                            }

                            marker.setMap(map);
                            poly.getPath().push(gLatLon);

                            google.maps.event.addListener(marker, 'click', function () {
                                map.setZoom(15);
                                map.setCenter(marker.getPosition());
                            });

                        } else if (joData.type == 'satellite') {
                            $("h6#status2").text("");
                            $("strong#strongSpeed").text("");
                            $("h3#speed span").fadeOut(500);
                            $("p#status1").addClass("blink");
                            $("span#mapMarker").fadeOut(500);
                        } else {
                            if ($("p#status1").hasClass("blink")) {
                                $("p#status1").removeClass("blink");
                            }
                        }
                    }

                }

            };

            webSocket.onclose = function (evnt) {
                updateStatus("ERROR: " + evnt.reason);
                console.log(evnt);
                setStatusBad();
            };

            webSocket.onerror = function (evnt) {
                updateStatus("ERROR: " + evnt.data);
                setStatusBad();
            };

            $("span#mapMarker").click(function () {
                var url = "http://maps.google.com/maps?z=12&t=m&q=loc:" + lastLat + "+ " + lastLon;
                window.open(url, '_blank');
            });

            function updateStatus(message) {
                $("p#status1").text(message);
            }

            function setStatusGood() {
                $("div#statusSignal").removeClass("statusBad").addClass("statusGood");
            }

            function setStatusBad() {
                $("div#statusSignal").addClass("statusBad").removeClass("statusGood");
            }


        });
    </script>

</head>
<body>

<div class="container-fluid full">
    <!--Map-->
    <div class="row full">
        <div class="col-md-12 full">
            <div class="full" id="map">
            </div>
        </div>
    </div>

    <!--Control panel-->
    <div id="mainContainer" class="row m0p0">
        <div class="col-md-8 content-centered">
            <div id="main">
                <div class="row">
                    <!--RED/GREEN-->
                    <div class="col-md-2">
                        <div id="statusSignal">

                        </div>
                    </div>
                    <!--STATUS-SPEED-STATUS-->
                    <div class="col-md-8" style="text-align: center">
                        <p id="status1"></p>
                        <h3 id="speed"><strong id="strongSpeed"></strong><span
                                style="display:none;font-size: 16px;  color: #949494;">kmph</span></h3>
                        <h6 id="status2"></h6>
                    </div>
                    <!--VIEW IN MAP BUTTON-->
                    <div class="col-md-2" style="text-align: center">
                        <span id="mapMarker" style="display: none;" class="glyphicon glyphicon-map-marker"></span>
                        <span id="mapMagnet" style="display: none;" class="glyphicon glyphicon-magnet inactive"></span>
                    </div>
                </div>
            </div>
        </div>
    </div>

</div>


<script>

    function myMap() {
        var mapCanvas = document.getElementById("map");
        var mapOptions = {
            center: new google.maps.LatLng(26.9259923, 79.2484255),
            zoom: 3
        };

        map = new google.maps.Map(mapCanvas, mapOptions);
    }
</script>
<script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyBiwaKupCMBsk9HAsqhNlOzUXKMEcZxLxg&callback=myMap"></script>
</body>
</html>