'use strict';

taskApp.constant('constants', {

	version: '${project.version}',
	availableState:{
		workspace:{
			name:'workspace',
			url:'/'
		}
	}
});

taskApp.config(function (constants, $stateProvider, $urlRouterProvider) {

	var availableState = constants.availableState;
	$stateProvider
		.state(availableState.workspace.name, {
			url: availableState.workspace.url,
			views: {
				'': {
					templateUrl: 'views/tasks.html',
					controller: 'mainController'
				}
			}
		});
	$urlRouterProvider.otherwise(availableState.workspace.url);
});

