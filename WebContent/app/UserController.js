/**
 * User controller
 */

snippet.controller('registerCtrl', function($scope, $http) {
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
            $location.path('/login');
        }).then(function(response){
        	alert(response);
        });
	};
});

snippet.controller('loginCtrl', function($scope, AuthenticationService, $http, $location){
    $scope.user={};
    
    $scope.login=function () {
        AuthenticationService.login($scope.user.username, $scope.user.password, loginCbck, $location);
    };
    function loginCbck(success) {
        if (success) {
            alert("success");
        }
        else{
        	alert("error");
        }
    };
});

snippet.controller('profileCtrl', function($scope, AuthenticationService, $http, $location){

    $scope.profile = AuthenticationService.getCurrentUser();
    // Password reset
    $scope.reset = function () {

        var promise = $http.post("/rest/user/reset", $scope.account);
        promise.then(function (response) {
            $location.path("/profile");
        });

    };
});