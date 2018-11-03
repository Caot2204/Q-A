var io = require("socket.io")(5000);

// https://socket.io/docs/rooms-and-namespaces/

io.on("connection", function(socket) {
    
    socket.on("comenzarPartida", function(codigoPartida, primerPregunta){
        io.to(codigoPartida).broadcast.emit("comenzarPartida", primerPregunta, socket.id);
    });
    
    socket.on("unirAPartida", function(codigoPartida){
        socket.join(codigoPartida);        
    });
    
    socket.on("enviarSiguientePregunta", function(codigoPartida, pregunta){
        io.to(codigoPartida).broadcast.emit("desplegarPregunta", pregunta);
    });
    
    socket.on("enviarRespuestasDePregunta", function(codigoPartida, respuestas){
        io.to(codigoPartida).broadcast.emit("desplegarRespuestas", respuestas);
    });
    
    socket.on("enviarGraficaRespuestas", function(codigoPartida, graficaRespuestas){
        io.to(codigoPartida).broadcast.emit("desplegarGraficaRespuestas", graficaRespuestas);
    });
    
    socket.on("enviarMarcador", function(codigoPartida, marcador){
        io.to(codigoPartida).broadcast.emit("desplegarMarcador", marcador);        
    });
    
    socket.on("responderPregunta", function(idMonitor, respuesta, puntajeJugador){
        socket.broadcast.to(idMonitor).emit("recibirRespuesta", respuesta, puntajeJugador);        
    });
    
    socket.on("enviarMensajeChat", function(codigoPartida, mensaje){
        io.to(codigoPartida).emit("recibirMensajeChat", mensaje);
    });
            
});

