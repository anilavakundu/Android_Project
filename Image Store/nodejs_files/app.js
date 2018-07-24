var express=require("express");
var bodyparser=require('body-parser');
var fileupload=require("express-fileupload");
var fs=require("fs");
var app=express();

var urlencodedParser = bodyparser.urlencoded({ extended: false });
app.use(fileupload());
app.use(express.static('Images'));


const host='192.168.1.33';
const port=3000;

app.post("/post",function(req,res){
	var file=__dirname+"/Images/"+req.files.foo.name;
	
	fs.writeFile(file,req.files.foo.data,function(err){
		
		if(err){
			
			console.log(err);
		
			}else{
			
				console.log("Done!");
		     
				}
		});
	res.sendStatus(200);
	});
app.get("/get_image/:id",function(req,res){
	var path=__dirname+"/Images/"+req.params.id;
	res.sendFile(path);
	
});
app.get("/images/files",function(req,res){
	console.log("Connected");
	var path=__dirname+"/Images/";
	var files=[];
	fs.readdir(path, function(err, items) {
 		for (var i=0; i<items.length; i++) {
      			  res.write(items[i]+'\n');
    			}	
		  res.end();	
		 });
	});
var server=app.listen(port,host,function(){
	console.log("Running at http://"+host+":"+port);
	});


