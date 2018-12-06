var io = require("socket.io")(5000);
console.log("BorkerPartida escuchando...");

// https://socket.io/docs/rooms-and-namespaces/

io.on("connection", function(socket) {
    
    socket.on("comenzarPartida", function(codigoPartida, primerPregunta, idSocket){
        io.to(codigoPartida).emit("comenzarPartida", primerPregunta, idSocket);
    });
    
    socket.on("unirAPartida", function(codigoPartida){
        socket.join(codigoPartida);
        console.log("se conecto a " + codigoPartida + " el socket " + socket.id);
    });
    
    socket.on("desconectarPartida", function(codigoPartida){
        socket.leave(codigoPartida);
        console.log("se desconecto de " + codigoPartida + " el socket " + socket.id);
    });
    
    socket.on("enviarSiguientePregunta", function(codigoPartida, pregunta){
        io.to(codigoPartida).emit("desplegarPregunta", pregunta);
    });
    
    socket.on("enviarRespuestasDePregunta", function(codigoPartida, preguntaConRespuestas){
        io.to(codigoPartida).emit("desplegarRespuestas", preguntaConRespuestas);
    });
    
    socket.on("enviarMarcador", function(codigoPartida, marcador){
        io.to(codigoPartida).emit("desplegarMarcador", marcador);        
    });
    
    socket.on("enviarMedallero", function(codigoPartida, medallero){
        io.to(codigoPartida).emit("desplegarMedallero", medallero);        
    });
    
    socket.on("responderPregunta", function(idMonitor, respuestaAPregunta){
        io.to(idMonitor).emit("recibirRespuesta", respuestaAPregunta);        
    });
    
    socket.on("enviarMensajeChat", function(codigoPartida, mensaje){
        io.to(codigoPartida).emit("recibirMensajeChat", mensaje);
    });
            
});

