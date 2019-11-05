'use strict';

taskApp.controller('mainController', function ($scope, $timeout, taskService, $http, $q) {

    var Status = {
        Error: "Error",
        Success: "Success"
    }

    var TaskStatus = {
        Open: "Open",
        Completed: "Completed"
    }

    $scope.startIndex = 0;
    $scope.showNumber = 15;
    $scope.fileMessage = '';
    $scope.tasks = [];

    $scope.task = {
        name: '',
        start: '',
        status: TaskStatus.Open
    }
    $scope.task2 = {
        name: '',
        start: ''
    }
    $scope.editableTask = null;

    $scope.isUploadDisabled = function () {
        return false;
    }


    $scope.isAddTaskDisabled = function () {

        if (!$scope.settings) {
            return true;
        }

        try {
            if (!new RegExp($scope.settings.datePattern).test($scope.task.dueDate.trim())
                || !new RegExp($scope.settings.namePattern).test($scope.task.name.trim())
            ) {
                return true;
            }

        } catch (e) {
            return true;
        }

        return false;
    }


    $scope.isUpdateTaskDisabled = function () {

        if (!$scope.settings) {
            return true;
        }

        try {
            if ($scope.task2.name == $scope.editableTask.name
                && $scope.task2.description == $scope.editableTask.description
                && $scope.task2.dueDate == $scope.editableTask.dueDate) {

                return true;

            }

            if (!new RegExp($scope.settings.datePattern).test($scope.task2.dueDate.trim())
                || !new RegExp($scope.settings.namePattern).test($scope.task2.name.trim())
            ) {
                return true;
            }

        } catch (e) {
            return true;
        }

        return false;
    }


    $scope.addTaskData = function () {

        console.log("adding task = " + $scope.task.name);

        clearMessages();

        var task = $scope.task;
        setWaitState(true);
        taskService.addTask($scope.task).then(function (result) {

            console.log("add task success");
            var res = result;

            if (res.status != Status.Success) {
                $scope.errorMessage = "Error adding task";
                if (res.message) {
                    $scope.errorMessage += ": " + res.message;
                }
            } else {
                $scope.statusMessage = "Task successfully added!";
            }

            $scope.reloadTasks().then(function (res) {
                setWaitState(false);
            });

            $scope.clearAddTaskInput();

        }, function (err) {

            setWaitState(false);
            console.log("error adsing task");
            $scope.errorMessage = "Error adding task";
            if (err.message) {
                $scope.errorMessage += ": " + err.message;
            }
        })
    }

    $scope.updateTask = function () {

        console.log("updating task = " + $scope.task2.id);

        clearMessages();

        setWaitState(true);
        taskService.updateTask($scope.task2).then(function (result) {

            console.log("change task success");
            var res = result;

            if (res.status != Status.Success) {
                $scope.errorMessage = "Error changing task";
                if (res.message) {
                    $scope.errorMessage += ": " + res.message;
                }
            } else {
                $scope.statusMessage = "Task successfully changed!";
            }

            $scope.reloadTasks().then(function (res) {
                setWaitState(false);
            });

            $scope.clearChangeTaskInput();

        }, function (err) {

            setWaitState(false);
            console.log("error adsing task");

            $scope.errorMessage = "Error changing task";
            if (err.message) {
                $scope.errorMessage += ": " + err.message;
            }
        })


    }

    $scope.getSettings = function () {

        console.log("getting settings");
        taskService.getSettings().then(function (result) {

            var settings = result;

            $scope.settings = {
                datetPattern: '',
                namePattern: ''
            };

            $scope.settings.datePattern = settings.datePattern;
            $scope.settings.namePattern = settings.namePattern;

        });
    };

    $scope.reloadTasks = function () {

        console.log("reloading tasks");
        var defer = $q.defer();
        $scope.tasks = [];

        taskService.getTasks().then(function (result) {

            var records = result;

            if (!records) {
                console.log("No task records found");
            }

            $scope.tasks = $scope.tasks.concat(records);

            if (!$scope.tasks || $scope.tasks.length == 0) {
                $scope.statusMessage = "No tasks found";
            }

            defer.resolve(result);

        });
        return defer.promise;
    }

    var clearMessages = function () {
        $scope.errorMessage = null;
        $scope.statusMessage = null;
    };

    $scope.isShowRecord = function (index) {
        return index >= $scope.startIndex && index < $scope.startIndex + $scope.showNumber;
    }

    $scope.onNavigateRight = function () {

        if (!$scope.tasks) {
            return;
        }
        if ($scope.startIndex + $scope.showNumber >= $scope.tasks.length) {
            return;
        }
        $scope.startIndex += $scope.showNumber;

        if ($scope.startIndex + $scope.showNumber >= $scope.tasks.length) {

            setWaitState(true);

            $scope.reloadTasks().then(function (res) {

                setWaitState(false);

                $scope.loadMoreData($scope.tasks.length).then(function (result) {

                    if (result) {
                        console.log("Loaded more tasks: " + result.length);
                    }

                })
            })

        }
    }

    $scope.clearAddTaskInput = function () {
        $scope.task.description = '';
        $scope.task.name = '';
        $scope.task.dueDate = '';
    }

    $scope.clearChangeTaskInput = function () {

        $scope.task2.description = '';
        $scope.task2.name = '';
        $scope.task2.dueDate = '';
        $scope.task2.id = '';

    }
    $scope.onNavigateLeft = function () {

        if (!$scope.tasks) {
            return;
        }

        if ($scope.startIndex == 0) {
            return;
        }

        $scope.startIndex = Math.max(0, $scope.startIndex - $scope.showNumber);
    };

    $scope.isNavigateLeft = function () {
        if (!$scope.tasks) {
            return false;
        }

        return $scope.startIndex > 0;
    }

    $scope.isNavigateRight = function () {
        if (!$scope.tasks) {
            return false;
        }
        return $scope.startIndex + $scope.showNumber < $scope.tasks.length;
        //return true;
    }

    $scope.getFinishIndex = function () {

        if (!$scope.tasks) return 0;
        if ($scope.startIndex + $scope.showNumber > $scope.tasks.length) {
            return $scope.tasks.length;
        }
        return $scope.startIndex + $scope.showNumber;

    }


    $scope.ondeleteTask = function (id) {

        console.log("deleting task = " + id);
        doConfirm("Delete task  " + id + " ?", function yes() {

            taskService.deleteTask(id).then(function (result) {

                if (Status.Success == result.status) {
                    console.log("task " + id + " removed!");
                    $scope.statusMessage = "Task successfully deleted!";
                } else {
                    $scope.errorMessage = "Error deleting task"
                    if (result.message) {
                        $scope.errorMessage += ": " + result.message;
                    }
                }
                $scope.reloadTasks().then(function (res) {
                    setWaitState(false);
                });


            }, function (err) {
                console.log("error deleting task");
                $scope.errorMessage = "Error deleting task"
                if (err.message) {
                    $scope.errorMessage += ": " + err.message;
                }
            })
        });
    }


    $scope.onEditTask = function (task) {

        console.log("editing task = " + task.id);
        $scope.editableTask = task;

        $scope.task2.id = task.id;
        $scope.task2.name = task.name;
        $scope.task2.description = task.description;
        $scope.task2.status = task.status;
        $scope.task2.dueDate = $scope.editableTask.dueDate = formatDate(new Date(task.dueDate));
    }


    $scope.loadMoreData = function (startIndex) {

        console.log("loading more tasks");
        var defer = $q.defer();
        taskService.getTasks(startIndex).then(function (result) {

            var records = result;

            if (!records) {
                console.log("No task records found");
            }

            $scope.tasks = $scope.tasks.concat(records);

            if (records && records.length > 0) {
                console.log("Found " + records.length + " tasks");


            } else {
                console.log("No more task records found");
            }


            defer.resolve(result);
        });
        return defer.promise;
    }

    var setWaitState = function (state) {

        if (state)
            $("#loading").css("display", "block");
        else
            $("#loading").css("display", "none");
    }


    var formatDate = function (date) {

        var mm = date.getMonth() + 1;
        var dd = date.getDate();
        var yyyy = date.getFullYear();
        if (dd < 10) {
            dd = '0' + dd;
        }
        if (mm < 10) {
            mm = '0' + mm;
        }
        var dt = yyyy + '-' + mm + '-' + dd;

        return dt;
    }


    function doConfirm(msg, yesFn, noFn) {
        var confirmBox = $("#confirmBox");
        confirmBox.find(".message").html(msg);
        confirmBox.find(".yes,.no").unbind().click(function () {
            confirmBox.hide();
        });
        confirmBox.find(".yes").click(yesFn);
        confirmBox.find(".no").click(noFn);
        confirmBox.show();
    }

    $scope.isCompleteAllowed = function (task) {

        return task != null && task.status != "Completed";

    }


    $scope.onCompleteTask = function (task) {

        doConfirm("Complete task  " + task.id + " ?", function yes() {
                task.status = TaskStatus.Completed;

                setWaitState(true);
                taskService.updateTask(task).then(function (result) {

                    console.log("Completing task success");
                    var res = result;

                    if (res.status != Status.Success) {
                        $scope.errorMessage = "Error completing task " + task.id;
                        if (res.message) {
                            $scope.errorMessage += ": " + res.message;
                        }
                    } else {
                        $scope.statusMessage = "Task successfully completed!";
                    }

                    $scope.reloadTasks().then(function (res) {
                        setWaitState(false);
                    });


                }, function (err) {

                    setWaitState(false);
                    console.log("error adsing task");

                    $scope.errorMessage = "Error completing task " + task.id;
                    if (err.message) {
                        $scope.errorMessage += ": " + err.message;
                    }
                })


            }
        );

    }

    var setLoading = function () {

        $("#loading").css("display", "block");
    }

    var stopLoading = function () {
        $("#loading").css("display", "none");
    }


    $scope.getSettings();
    $scope.reloadTasks();

})



