/**
 * User controller
 */

snippet.controller('registerCtrl', function($scope, AuthenticationService, $http) {
	alert("current User: " + JSON.stringify(AuthenticationService.getCurrentUser()));
	
	// Write user to file
	$scope.uploadFile = function(files) {
        $scope.user.file = files[0];
    };
    
	$scope.register = function(){
		var fd = new FormData();
		
        fd.append("file", $scope.user.file);
        fd.append("username", $scope.user.username);
        fd.append("password", $scope.user.password);
        fd.append("firstName", $scope.user.firstName);
        fd.append("lastName", $scope.user.lastName);
        fd.append("email", $scope.user.email);
        fd.append("phone", $scope.user.phoneNumber);
        fd.append("location", $scope.user.location);

        $http.post('rest/users/add', fd, {
            withCredentials: true,
            headers: {'Content-Type': undefined },
            transformRequest: angular.identity
        }).then(function(success) {
        	toast(success.data);
            $location.path('/login');
        }).then(function(response){
        	alert(response.data);
        });
	};
});

// Display login page and authenticate the user
snippet.controller('loginCtrl', function($scope, AuthenticationService, $http, $location){
    $scope.user={};
    
    $scope.login=function () {
        AuthenticationService.login($scope.user.username, $scope.user.password, loginCbck, $location);
    };
    function loginCbck(success) {
        if (success) {
            alert("Welcome");
        }
        else{
        	alert("Ups! Something went wrong.");
        }
    };
});

// Display users profile page
snippet.controller('profileCtrl', function($window, $scope, AuthenticationService, $http, $location){
	var vm = this;
	
	// Get all languages from file
	$scope.getAllLanguages = function(){
		$http.get('rest/users/languages/all')
			.then(function(response){
				$scope.languages = response.data;
			});
	}
	
	$scope.getAllLanguages();
	
	// Get all snippets from file for the current user
	$scope.getAllSnippets = function(){
		$http.get('rest/users/snippets/user/all')
			.then(function(response){
				$scope.snippets = response.data;
			});
	}
	
	// Snippets details
	$scope.detailsSnippet = function(snippet){
		$window.sessionStorage.snippet_id = snippet.id;
		$location.path('/details/');
	}
	
	// Delete selected snippet
	$scope.deleteSnippet = function(snippet){
		$http.get('rest/users/snippets/delete/' + snippet.id)
			.then(function (response){
				$scope.getAllSnippets();
			});
	}
	
	$scope.getAllSnippets();
});

// Display administrator page with all options
snippet.controller('adminCtrl', function($scope, AuthenticationService, $http, $location){
	var vm = this;
	$scope.users = []
	$scope.languages = []
	
	// Get all registered users
	$scope.getAllRegUsers = function() {
		$http.get('rest/users/all')
			.then(function (response) {
	            $scope.users = response.data;
	        });
	};
	
	// Get all languages from file
	$scope.getAllLanguages = function(){
		$http.get('rest/users/languages/all')
			.then(function(response){
				$scope.languages = response.data;
			});
	}
	
	$scope.getAllRegUsers();
	$scope.getAllLanguages();
	
	// Block user
	$scope.blockUser = function(user){
		var promise = $http.post("rest/users/block/" + user.username);
        promise.then(function (response) {
        	$scope.getAllRegUsers();
        });
	};
	
	// Unblock user
	$scope.unblockUser = function(user){
		var promise = $http.post("rest/users/unblock/" + user.username);
        promise.then(function (response) {
        	$scope.getAllRegUsers();
        });
	};
	
	// Add new Language
	$scope.addLanguage = function(language){
		var promise = $http.post("rest/users/languages/add/" + language.name);
		promise.then(function (response){
			alert("language add: " + JSON.stringify(response.data));
			$scope.getAllLanguages();
		});
	}
});

//Display page for adding snippets and all snippets from database
snippet.controller('snippetCtrl', function($window, $scope, AuthenticationService, $http, $location){
	var vm = this;
	
	$scope.snippets = []
	$scope.languages = {}
	// Get the role of the logged in user
	// If user is admin he can delete snippets from the list with all snippets
	$scope.userRole = {}
	
	// Status to check if user is blocked 
	$scope.status;
	
	if(AuthenticationService.getCurrentUser() === undefined){
		$scope.userRole = "Guest";
		$scope.status = "Active";
	}else{
		var user = AuthenticationService.getCurrentUser();
		$scope.userRole = user.role;
		$scope.status = user.status;
	}
	
	// Get all languages from file
	$scope.getAllLanguages = function(){
		$http.get('rest/users/languages/all')
			.then(function(response){
				$scope.languages = response.data;
			});
	}
	
	$scope.getAllLanguages();
	
	// Get all languages from file
	$scope.getAllSnippets = function(){
		$http.get('rest/users/snippets/all')
			.then(function(response){
				$scope.snippets = response.data;
			});
	}
	
	
	$scope.getAllSnippets();
	
	$scope.snippet = {};
	
	$scope.language = {};
	
	// Add new Snippet
	$scope.addSnippet = function(snippet){
		$scope.snippet.language = $scope.language.name;
		var promise = $http.post("rest/users/snippets/add/", $scope.snippet);
		promise.then(function (response){
			if(!(response.data.status === "OK")){
				alert(response.data.status);
			}
			$scope.getAllSnippets();
		});
	}
	
	// Snippets details
	$scope.detailsSnippet = function(snippet){
		$window.sessionStorage.snippet_id = snippet.id;
		$location.path('/details/');
	}
	
	// Delete selected snippet
	$scope.deleteSnippet = function(snippet){
		$http.get('rest/users/snippets/delete/' + snippet.id)
			.then(function (response){
				$scope.getAllSnippets();
			});
	}
	
	// Disable comments
	$scope.blockComments = function(snippet){
//		alert("Snippet to be blocked " + snippet);
		var promise = $http.post("rest/users/snippets/comments/block/", snippet);
        promise.then(function (response) {
        	alert(response.data);
        	$scope.getAllSnippets();
        })
        .catch(function(error){
        	alert(JSON.stringify(error));
        });
	};
	
	// Enable comments
	$scope.unblockComments = function(snippet){
		var promise = $http.post("rest/users/snippets/comments/unblock/", snippet);
        promise.then(function (response) {
        	$scope.getAllSnippets();
        });
	};
});

//Display snippet details page
snippet.controller('snippetDetailsCtrl', function($window, $scope, AuthenticationService, $http){
	var vm = this;
	
	$scope.s = [];
	
	// Snippets comments
	$scope.comments = [];
	
	// Role of the user that is on this page
	$scope.role;
	// Status to check if user is blocked 
	$scope.status = "";
	// Username of the user that is online
	$scope.userUsername;
	
	// Get the selected snippet from database and display it's details
	$scope.getSnippet = function(){
		$http.get('rest/users/snippets/details/' + $window.sessionStorage.snippet_id)
	        .then(function (response) {
	            $scope.s = response.data;
	            $scope.comments = $scope.s.comments;
	        });
		// Get the current role of the user that is on this page
		if(AuthenticationService.getCurrentUser() === undefined){
			$scope.role = "Guest";
			$scope.status = "Active";
		}else{
			$scope.role = AuthenticationService.getCurrentUser().role;
			var user = AuthenticationService.getCurrentUser();
			$scope.status = user.status;
			$scope.userUsername = user.username;
		}
	};
	
	$scope.getSnippet();
	
	// Add new Comment to snippet
	$scope.addComment = function(snippet, comment){
		// Set comments user to Guest if user is not logged in
		// Or to users username if he is logged in
		var username = "";
		if(AuthenticationService.getCurrentUser() === undefined){
			username = "Guest";
		}else{
			username = AuthenticationService.getCurrentUser().username;
		}
		comment.user = username;
		
		// Make a post call to service to save the comment
		var promise = $http.post("rest/users/snippets/" + snippet.id + "/comment/add/", comment);
		promise.then(function (response){
			$scope.s = response.data;
			$scope.comments = $scope.s.comments;
		});
	}
	
	// Delete any comment if user is admin
	// If not then user can delete only his comments
	$scope.deleteComment = function(comment, snippet){
		var promise = $http.post("rest/users/snippets/" + snippet.id + "/comment/delete/", comment);
		promise.then(function (response){
			$scope.s = response.data;
			$scope.comments = $scope.s.comments;
		})
	}
	
	// Like comment
	$scope.likeComment = function(comment, snippet){
		var promise = $http.post("rest/users/snippets/" + snippet.id + "/" + $scope.userUsername +"/comments/like/", comment);
		promise.then(function (response){
			if(response.data.status === "You already gave your grade this comment"){
				alert(response.data.status);
			}else{
				$scope.s = response.data.status;
				$scope.comments = $scope.s.comments;
			}
		})
	}
	
	// Dislike comment
	$scope.dislikeComment = function(comment, snippet){
		var promise = $http.post("rest/users/snippets/" + snippet.id + "/" + $scope.userUsername +"/comments/dislike/", comment);
		promise.then(function (response){
			if(response.data.status === "You already gave your grade this comment"){
				alert(response.data.status);
			}
			else{
				$scope.s = response.data.status;
				$scope.comments = $scope.s.comments;
			}
		})
	}
});















