/**
 * 
 */

// User services
snippet.factory('usersFactory', function($http) {

	factory.register = function(user) {
		return $http.post('/com.web.programiranje.snippets/rest/users/add', {"username":''+user.id, "password":''+user.password, "firstName":''+user.firstName, 
											  "lastName":''+user.lastName, "email":''+user.phoneNumber, "location":''+user.location});
	};
	
	return factory;
	
});