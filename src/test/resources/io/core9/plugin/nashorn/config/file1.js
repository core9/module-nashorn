/**
{
	"include" : [
	             "src/test/resources/io/core9/plugin/nashorn/config/file2.js",
	             "src/test/resources/io/core9/plugin/nashorn/config/file3.js"
	             ],
	"var" : [ 
	{
		"name" : "tmp",
		"invoke" : "test",
		"args" : ["test"]
	}, {
		"name" : "tmp3",
		"invoke" : "test",
		"args" : ["tmp"]
	} ] 
}
**/


var test = "test";