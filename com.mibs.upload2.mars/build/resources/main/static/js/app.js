$(document).ready(function() {

	console.log("hello1");

	$("#btnUpload").click(function() {

		console.log("opload clicked");
		var data = {
			fname : "Jhon",
			lname : "Smith",
			attachements : ["apple", "banano"]
		};
		$.ajax({
			type : "POST",
			url : "testUpload",
			data : JSON.stringify(data),
			contentType : 'application/json',
			dataType : "html",
			//headers : headers,
			success : function(e) {

			   console.log( e );
			},
			error : function(e) {
			  console.log( e );
			}
		});
	});

});