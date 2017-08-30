/**
 * 
 */

// User services
snippet.factory('usersFactory', function($http) {

	factory.profile = function(user) {
		return $http.get('/com.web.programiranje.snippets/rest/users/profile', {"username":''+user.username});
	};
	
	return factory;
	
});