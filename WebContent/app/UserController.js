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
	
	$scope.getAllRegUsers = function() {
		$http.get('rest/users/all')
			.then(function (response) {
	            $scope.users = response.data;
	        });
	};
	
	$scope.getAllRegUsers();
	
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
});
















