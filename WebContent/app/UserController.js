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
snippet.controller('profileCtrl', function($scope, AuthenticationService, $http, $location){
	var vm = this;
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
});

//Display snippet details page
snippet.controller('snippetDetailsCtrl', function($window, $scope, AuthenticationService, $http){
	var vm = this;
	
	$scope.s = [];
	
	$scope.getSnippet = function(){
		$http.get('rest/users/snippets/details/' + $window.sessionStorage.snippet_id)
	        .then(function (response) {
	            $scope.s = response.data;
	        });
	};
	
	$scope.getSnippet();
	
});















