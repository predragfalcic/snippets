/**
 * Authenticate the user when logging on server
 */

(function () {
    angular
        .module('authentication',['ngStorage','angular-jwt'])
        .factory('AuthenticationService', Service);

    function Service($http, $localStorage, jwtHelper) {
        var service = {};

        service.login = login;
        service.logout = logout;
        service.getCurrentUser = getCurrentUser;

        return service;

        function login(username, password, callback, $location) {
            $http.post('rest/users/login', { "username": username, "password": password, "role": "null" })
                .then(function successCallback(response) {
                    // ukoliko postoji token, prijava je uspesna
                    if (response.data.token) {
                    	
                        // korisnicko ime, token i rola (ako postoji) cuvaju se u lokalnom skladištu
                        var currentUser = { username: username, token: response.data.token }
                        var tokenPayload = jwtHelper.decodeToken(response.data.token);
                        
                        if(tokenPayload.role){
                            currentUser.role = tokenPayload.role;
                        }
                        if(tokenPayload.image){
                            currentUser.image = tokenPayload.image;
                        }
                        
                        // prijavljenog korisnika cuva u lokalnom skladistu
                        $localStorage.currentUser = currentUser;
                        // jwt token dodajemo u to auth header za sve $http zahteve
                        $http.defaults.headers.common.Authorization = response.data.token;	
                        // callback za uspesan login
                        
                        if(tokenPayload.role === "admin"){
                        	$location.path("/admin");
                        }else if(tokenPayload.role === "regUser"){
                        	$location.path("/snippets");
                        }
                        
                        callback(true);
                        
                    } else {
                        // callback za neuspesan login
                        callback(false);
                    }
                }, function errorCallback(response) {
                	alert("usao je ovde");
                    console.log("Error")
                });
        };
        
        function logout() {
            // uklonimo korisnika iz lokalnog skladišta
            delete $localStorage.currentUser;
            $http.defaults.headers.common.Authorization = '';
        };

        function getCurrentUser() {
            return $localStorage.currentUser;
        };
        
        function setHeader(){
        	alert("Setting header for the request.");
        	// If the user is logged in set the header for the request
            if ($localStorage.currentUser) {
                $http.defaults.headers.common.Authorization = $localStorage.currentUser.token;
            }
            return $localStorage.currentUser.token
        };
        
        function isLoggedIn(){
        	// postavljanje tokena nakon refresh
            if ($localStorage.currentUser) {
                return true;
            }else{
            	return false;
            }
        };
    }
})();