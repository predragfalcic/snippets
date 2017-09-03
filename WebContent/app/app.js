/**
 * Create routes for controllers
 */

// Create modules
var snippet = angular.module('snippet', ['ngRoute', 'authentication']);

snippet.config(function($routeProvider) {
	$routeProvider.when('/', {
		templateUrl: 'directives/login.html'
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
	.when('/snippets', {
		templateUrl: 'directives/snippets.html'
	})
	.when('/details', {
		templateUrl: 'directives/SnippetDetails.html'
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

// Custom filter for comparing dates
snippet.filter('snippetsByDate', function(){
	return function(items, fromDate, toDate){
		var filtered = [];
        //here you will have your desired input
        var from_date = Date.parse(fromDate);
        var to_date = Date.parse(toDate);
        if (from_date == null || toDate == null)
            filtered = items;
        else
            filtered = items.filter(function (item) {
                return item.created >= from_date && item.created <= to_date;
            });
        return filtered;
	};
});

//Custom filter for comment likes
snippet.filter('orderObjectBy', function(){
    return function(input, attribute) {
    	
        if (!angular.isObject(input)) return input;

        var array = [];
        for(var objectKey in input) {
            array.push(input[objectKey]);
        }

        array.sort(function(a, b){
            a = parseInt(a.grade[attribute]);
            b = parseInt(b.grade[attribute]);
            return b - a;
        });
        return array;
    }
});





