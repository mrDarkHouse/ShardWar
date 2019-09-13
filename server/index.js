var app = require('express')();
var server = require('http').Server(app);
var io = require('socket.io')(server);
var os = require("os");
var networkInterfaces = os.networkInterfaces( );
var players = [];
var rooms = [];



//var Datastore = require('nedb');
//var db = new Datastore({filename : 'users'});
//db.loadDatabase();


server.listen(8080, function(){
	console.log("Server started");
	init();
	//var room = new Room();
	//rooms.push(room);
	
	
})

//io.on('connection', function(socket){
//	console.log("Connected");
//	socket.emit('socketID', { id: socket.id });
//	
//	socket.on('disconnect', function(){
//		console.log("Disconnected");
//	});
//});

io.on('connection', function(socket){
	console.log("Connected");
	//console.log(networkInterfaces);
	getIp();
	socket.emit('socketID', { id: socket.id });
	//socket.broadcast.emit('newPlayer', { id: socket.id});
	//console.log(socket.sockets);
	
	socket.on('disconnect', function(){
		socket.broadcast.emit('playerDisconnected', { id: socket.id });
		//let i = players.indexOf(socket)
		//players.splice(i, 1);
		removePlayerFromRoom(socket);
		for(let i = 0; i < players.length; i++){
			if(players[i].id == socket.id){
				players.splice(i, 1);
			}
		}
		console.log("Player Disconnected" + "(current players: " + players.length + ")");
		logRoomsInfo();
	});
	socket.on('initUser', function(data){
		var newPlayer = new Player(socket.id, data.name, data.avatarID, data.rankedPoints);
		players.push(newPlayer);
		console.log("New player connected " + "(current players: " + players.length + ")");
		addPlayerToRoom(newPlayer);
		logRoomsInfo();
		//socket.broadcast.emit('newPlayer', {id: newPlayer.id, name: newPlayer.name, 
		//	avatarID: newPlayer.avararID, rankedPoints:newPlayer.rankedPoints});
		//if(players.length > 1){
			//socket.emit('newPlayer', {id: players[0].id, name: players[0].name, 
			//avatarID: players[0].avararID, rankedPoints:players[0].rankedPoints});
			//io.sockets.emit("gameStart");
		//}
		
		
	});
	socket.on('endTimeButton', function(){
		let t = getRoomBySocket(socket);
		rooms[t].endTime();
		
	});
	socket.on('voteResult', function(data){
		let t = getRoomBySocket(socket);
		if(rooms[t].player1.id == socket.id){
			io.sockets.sockets[rooms[t].player2.id].emit("enemyVoted", data);
			rooms[t].readyChange(0, data);
		}
		if(rooms[t].player2.id == socket.id){
			io.sockets.sockets[rooms[t].player1.id].emit("enemyVoted", data);
			rooms[t].readyChange(1, data);
		}
	});
	socket.on('askCurrentPlayer', function(data){
		let t = getRoomBySocket(socket);
		let n = rooms[t].currentPlayer;
		let ans;
		if(rooms[t].player1.id == socket.id){
			if(n == 1) ans = 1;
			if(n == 2) ans = 2;
		}
		if(rooms[t].player2.id == socket.id){
			if(n == 1) ans = 2;
			if(n == 2) ans = 1;
		}
		socket.emit("currentPlayer", {player: ans});
	});
	
	
	
});

function getIp(){
	Object.keys(networkInterfaces).forEach(function (ifname) {
		var alias = 0;

		networkInterfaces[ifname].forEach(function (iface) {
			if ('IPv4' !== iface.family || iface.internal !== false) {
			  // skip over internal (i.e. 127.0.0.1) and non-ipv4 addresses
			  return;
			}

			if (alias >= 1) {
			  // this single interface has multiple ipv4 addresses
			  console.log(ifname + ':' + alias, iface.address);
			} else {
			  // this interface has only one ipv4 adress
			  console.log(ifname, iface.address);
			}
			++alias;
		});
	});
}


function getRoomBySocket(socket){
	for(let i = 0; i < rooms.length; i++){
		if((rooms[i].player1 != null && rooms[i].player1.id == socket.id) || 
		   (rooms[i].player2 != null && rooms[i].player2.id == socket.id)){
			return i;
		}
	}
	console.log("error: cant find room by socket " + socket)
}

function removePlayerFromRoom(socket){
	let t = getRoomBySocket(socket);
	if(t == undefined) return;
	
	if(rooms[t].player1 != null && rooms[t].player1.id == socket.id){
		if(rooms[t].full()){
			io.sockets.sockets[rooms[t].player2.id].emit("enemyDisconnected");
			rooms[t].flush();
		}
		rooms[t].player1 = null;
	}
	if(rooms[t].player2 != null && rooms[t].player2.id == socket.id){
		if(rooms[t].full()){
			io.sockets.sockets[rooms[t].player1.id].emit("enemyDisconnected");
			rooms[t].flush();
		}
		rooms[t].player2 = null;
	}
	if(rooms[t].empty()){
		rooms.splice(t, 1);
	}
}

function addPlayerToRoom(player){
	let t = getFreeRoom(player); 
	
	if(rooms[t].player1 == null){
		rooms[t].player1 = player;
	}else if(rooms[t].player2 == null){
		rooms[t].player2 = player;
	}
	if(rooms[t].full()){
		//io.sockets.emit("startVote");
		
		
		io.sockets.sockets[rooms[t].player1.id].emit('startVote', {id: rooms[t].player2.id, name: rooms[t].player2.name, 
			avatarID: rooms[t].player2.avararID, rankedPoints:rooms[t].player2.rankedPoints});
		
		io.sockets.sockets[rooms[t].player2.id].emit('startVote', {id: rooms[t].player1.id, name: rooms[t].player1.name, 
			avatarID: rooms[t].player1.avararID, rankedPoints:rooms[t].player1.rankedPoints});
		//io.sockets.emit('startVote', {id: players[0].id, name: players[0].name, 
			//avatarID: players[0].avararID, rankedPoints:players[0].rankedPoints});
	}
}


function getFreeRoom(player){
	for(let i = 0; i < rooms.length; i++){
		if(!rooms[i].full()){
			if(rooms[i].player1 != null && playerCanBeEnemy(rooms[i].player1, player)){
				return i;
			}
			if(rooms[i].player2 != null && playerCanBeEnemy(rooms[i].player2, player)){
				return i;
			}
		}
	}
	var room = new Room();
	rooms.push(room);	
	return rooms.length - 1;
}
function playerCanBeEnemy(player1, player2){
	return true;
}

function logRoomsInfo(){
	console.log("Current rooms:");
	for(let i = 0; i < rooms.length; i++){
		//if(!rooms[i].empty()){
			let info = "Room " + i;
			if(rooms[i].inGame) info += " (in game)";
			console.log(info);
			var s1;
			if(rooms[i].player1 == null) s1 = "Empty";
			else s1 = rooms[i].player1.name;
			
			var s2;
			if(rooms[i].player2 == null) s2 = "Empty";
			else s2 = rooms[i].player2.name;
			
			console.log("p1: " + s1);
			console.log("p2: " + s2);
		//}
	}
}


function Player(id, name, avararID, rankedPoints){
	this.id = id;
	this.name = name;
	this.avararID = avararID;
	this.rankedPoints = rankedPoints;
}


var turnTime = 45;

function Room(){
	this.player1 = null;
	this.player2 = null;
	this.ready = [];
	this.ready[0] = -1;
	this.ready[1] = -1;
	this.inGame = false;
	this.roundTimer = turnTime;
	this.currentPlayer = -1;
	this.numberChange = -1;
	this.buyedInit = [];
	
	this.iterate = function(delay){
		if(this.inGame){
			this.timerUpdate(delay);
		}
	};
	this.empty = function(){
		if(this.player1 == null && this.player2 == null) return true;
		else return false;
	};
	this.full = function(){
		if(this.player1 != null && this.player2 != null) return true;
		else return false;
	};
	this.flush = function(){
		this.ready[0] = -1;
		this.ready[1] = -1;
	};
	
	
	
	this.readyChange = function(num, val){
		this.ready[num] = val;
		if(this.ready[0] == 1 && this.ready[1] == 1){
			this.startGame();
			io.sockets.sockets[this.player1.id].emit("gameStart");
			io.sockets.sockets[this.player2.id].emit("gameStart");
		}
		if(this.ready[0] == 0){
			if(this.player2 != null) io.sockets.sockets[this.player2.id].emit("enemyDisconnected");
			//io.sockets.connected[this.player1.id].disconnect();
			//this.player1 = null;
			this.flush();
			//console.log("exit 1");
			//console.log(this.player1 + " " + this.player2 + " " + this.ready[0] + " " + this.ready[1])
		}
		if(this.ready[1] == 0){
			if(this.player1 != null) io.sockets.sockets[this.player1.id].emit("enemyDisconnected");
			//io.sockets.connected[this.player2.id].disconnect();
			//this.player2 = null;
			this.flush();
			//console.log("exit 2");
			//console.log(this.player1 + " " + this.player2 + " " + this.ready[0] + " " + this.ready[1])
		}
	};
	this.startGame = function(){
		this.inGame = true;
		this.startTurn();
	}
	
	this.startTurn = function(){
		if(this.buyedInit[0] != this.buyedInit[1]){
            if(this.buyedInit[0] > this.buyedInit[1]) this.currentPlayer = 1;
            else this.currentPlayer = 2;
            this.buyedTurn = currentPlayer;
        }else {
            let n = Math.floor((Math.random() * 2));
            this.currentPlayer = n + 1;
        }
		this.round++;
		this.buyedInit[0] = 0;
        this.buyedInit[1] = 0;
		
	}
	this.changePlayerTurn = function(){
		this.currentPlayer = 3 - this.currentPlayer;
		this.numberChange++;
	}
	
	this.endTime = function(){
		this.roundTimer = turnTime;
		this.nextPlayer();
		
		io.sockets.sockets[this.player1.id].emit("timeEnd");
		io.sockets.sockets[this.player2.id].emit("timeEnd");
	}
	
	this.nextPlayer = function(){
		if (this.numberChange >= 1) {
                this.numberChange = 0;
                this.income();
                this.startTurn();
        } else this.changePlayerTurn();
	}
	this.income = function(){
		
		
	}
	
	this.timerUpdate = function(delay){
		this.roundTimer -= delay/1000;
		io.sockets.sockets[this.player1.id].emit("timeChange", {time: this.roundTimer});
		io.sockets.sockets[this.player2.id].emit("timeChange", {time: this.roundTimer});
		if(this.roundTimer <= 0){
			this.endTime();
		}
		//console.log(this.roundTimer);
	}
	
}



function init(){
	setInterval(updateRooms, 1000);
	
}
function updateRooms(){
	for(let i = 0; i < rooms.length; i++){
		rooms[i].iterate(1000);
	}
}








