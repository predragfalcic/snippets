/**
 * User controller for registering and login user
 */

snippet.controller('registerCtrl', function($scope, $http) {
	// Write user to file
	$scope.register = function(user){
		$http({
			  method: 'POST',
			  url: 'rest/users/add',
			  data : angular.toJson(user)
			}).then(function successCallback(response) {
				alert(response.data);
				redirectTo: 'login';
			  }, function errorCallback(response) {
			    alert(response.data)
			  });
	};
});