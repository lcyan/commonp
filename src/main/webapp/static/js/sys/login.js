require(['jquery', 'validation_ext', 'validation_i18n', 'bootstrap'], function($) {
	$(function(){
		var $loginForm = $('#loginForm');
		
		$loginForm.validate({
			rules:{
				username:{required:true},
				password:{required:true}
			},
			messages:{
				username:{required:'用户名不能为空.'},
				password:{required:'用户密码不能为空.'}
			}
		});
	});
});