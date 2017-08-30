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
                        
                        callback(true);
                        
                        $location.path('/profile');
                    } else {
                        // callback za neuspesan login
                        callback(false);
                    }
                }, function errorCallback(response) {
                    console.log("Error")

                });
        }

        function logout() {
            // uklonimo korisnika iz lokalnog skladišta
            delete $localStorage.currentUser;
            $http.defaults.headers.common.Authorization = '';
            //$state.go('login');
        }

        function getCurrentUser() {
            return $localStorage.currentUser;
        }
    }
})();