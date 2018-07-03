$(document).ready(function() {

	console.log("hello1");

	$("#btnUpload").click(function() {

		console.log("opload clicked");
		var atachments = {
				name : "file1.txt",
				content : "abcdefgh"
		}
		var data = {
			fname : "Jhon",
			lname : "Smith",
			attachements : [{ name : "file1.txt", content: "abcdef" } , { name : "file2.txt", content: "ghxyz" }]
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