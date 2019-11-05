taskApp.factory('taskService', function ($resource, $http, $q) {

    var service = {};

    var urls = {
        getTasks: "api/tasks",
        addTask: "api/addtask",
        deleteTask: "api/deletetask",
        updateTask: "api/updatetask",
        getSettings: "api/settings"
    };

    service.getTasks = function (startIndex) {

        var offset = startIndex?startIndex:0;
        var url = urls.getTasks;
        var defer = $q.defer();

        var response = $http({
            method: "get",
            url: url,
            params: {offset: offset},
            cache: false
        });

        response.success(function (responce) {
            defer.resolve(responce);
        }).error(function (responce) {
            defer.reject(responce);
        });

        return defer.promise;
    };

    service.addTask = function (task) {

        var url = urls.addTask;
        var defer = $q.defer();

        var response = $http({
            url: url,
            cache: false,
            method: "post",
            data: task
/*            data: {
                name: task.name,
                dueDate: task.dueDate,
                status: task.status
            }*/
        });

        response.success(function (responce) {
            defer.resolve(responce);
        }).error(function (responce) {
            defer.reject(responce);
        });

        return defer.promise;
    };


    service.updateTask = function (task) {

        var url = urls.updateTask;
        var defer = $q.defer();

        var response = $http({
            url: url,
            cache: false,
            method: "put",
            data: task
/*            data: {
                id: task.id,
                dueDate: task.dueDate,
                name: task.name,
                status: task.status
            }*/
        });

        response.success(function (responce) {
            defer.resolve(responce);
        }).error(function (responce) {
            defer.reject(responce);
        });

        return defer.promise;
    };
    service.getSettings = function () {
        var url = urls.getSettings;
        var defer = $q.defer();

        var response = $http({
            method: "get",
            url: url,
            cache: false
        });

        response.success(function (responce) {
            defer.resolve(responce);
        }).error(function (responce) {
            defer.reject(responce);
        });

        return defer.promise;
    };


    service.deleteTask = function (id) {
        var input =[   ];
        var url = urls.deleteTask + "/" + id;
        var defer = $q.defer();


        var response = $http({
            url: url,
            cache: false,
            method: "delete"

        });

        response.success(function (responce) {
            defer.resolve(responce);
        }).error(function (responce) {
            defer.reject(responce);
        });
        return defer.promise;
    };
    return service;
});