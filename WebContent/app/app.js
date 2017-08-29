/**
 * Create routes for controllers
 */

// Create modules
var snippet = angular.module('snippet', ['ngRoute']);

snippet.config(function($routeProvider) {
	$routeProvider.when('/',{
		templateUrl: 'directives/registration.html'
	})
	.when('/login', {
		templateUrl: 'directives/login.html'
	})
	.when('/registration', {
		templateUrl: 'directives/registration.html'
	})
	.otherwise({
		redirectTo: '/'
	});
});

snippet.config(function($logProvider){
    $logProvider.debugEnabled(true);
});

