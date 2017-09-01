/**
 * Create routes for controllers
 */

// Create modules
var snippet = angular.module('snippet', ['ngRoute', 'authentication']);

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
	.when('/profile', {
		templateUrl: 'directives/profile.html'
	})
	.when('/admin',{
		templateUrl: 'directives/adminProfile.html'
	})
	.otherwise({
		redirectTo: '/'
	});
});

snippet.run(run);

function run($rootScope, $http, $location, $localStorage, AuthenticationService) {
    // postavljanje tokena nakon refresh
    if ($localStorage.currentUser) {
        $http.defaults.headers.common.Authorization = $localStorage.currentUser.token;
    }

    $rootScope.logout = function () {
        AuthenticationService.logout();
    }

    $rootScope.getCurrentUserRole = function () {
        if (!AuthenticationService.getCurrentUser()){
            return undefined;
        }
        else{
            return AuthenticationService.getCurrentUser().role;
        }
    }
    $rootScope.isLoggedIn = function () {
        if (AuthenticationService.getCurrentUser()){
            return true;
        }
        else{
            return false;
        }
    }

    $rootScope.getCurrentUser = function () {
        if (!AuthenticationService.getCurrentUser()){
            return undefined;
        }
        else{
            return AuthenticationService.getCurrentUser().username;

        }
    }

    $rootScope.getCurrentUserImage = function () {
        if (!AuthenticationService.getCurrentUser()){
            return undefined;
        }
        else{
            return AuthenticationService.getCurrentUser().image;

        }
    }
}