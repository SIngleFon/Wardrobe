// МОДАЛЬНОЕ ОКНО АВТОРИЗАЦИИ - РЕГИСТРАЦИИ
// ---------------------------------------
jQuery(document).ready(function($) {
	var formModal = $('.cd-user-modal'),
	  formLogin = formModal.find('#cd-login'),
	  formSignup = formModal.find('#cd-signup'),
	  formForgotPassword = formModal.find('#cd-reset-password'),
	  formModalTab = $('.cd-user-modal-container .cd-switcher li a'), // Изменено
	  tabLogin = formModalTab.filter(':eq(0)'), // Изменено
	  tabSignup = formModalTab.filter(':eq(1)'), // Изменено
	  forgotPasswordLink = formLogin.find('.cd-form-bottom-message a'),
	  backToLoginLink = formForgotPassword.find('.cd-form-bottom-message a'),
	  mainNav = $('.main-nav');

  // Открытие модального окна при клике на "Войти"
  $('.trigger').on('click', function(event) {
	event.preventDefault();
	formModal.addClass('is-visible');
	login_selected(); // Добавлено, чтобы открыть вкладку "Sign In"
  });

	//open modal
	mainNav.on('click', function(event) {
	  $(event.target).is(mainNav) && mainNav.children('ul').toggleClass('is-visible');
	});

	//close modal
	formModal.on('click', function(event) {
	  if ($(event.target).is(formModal) || $(event.target).is('.cd-close-form')) {
		formModal.removeClass('is-visible');
	  }
	});

	//close modal when clicking the esc keyboard button
	$(document).keyup(function(event) {
	  if (event.which == '27') {
		formModal.removeClass('is-visible');
	  }
	});

	//switch from a tab to another
	formModalTab.on('click', function(event) {
	  event.preventDefault();
	  event.stopPropagation(); // Добавлено для предотвращения всплытия события
	  ($(event.target).is(tabLogin)) ? login_selected() : signup_selected();
	});

	//hide or show password
	$('.hide-password').on('click', function() {
	  var togglePass = $(this),
		passwordField = togglePass.prev('input');

	  ('password' == passwordField.attr('type')) ? passwordField.attr('type', 'text'): passwordField.attr('type', 'password');
	  ('Hide' == togglePass.text()) ? togglePass.text('Show'): togglePass.text('Hide');
	  //focus and move cursor to the end of input field
	  passwordField.putCursorAtEnd();
	});

	//show forgot-password form
	forgotPasswordLink.on('click', function(event) {
	  event.preventDefault();
	  forgot_password_selected();
	});

	//back to login from the forgot-password form
	backToLoginLink.on('click', function(event) {
	  event.preventDefault();
	  login_selected();
	});

	function login_selected() {
		formLogin.addClass('is-selected').siblings('#cd-signup, #cd-reset-password').removeClass('is-selected');
		tabLogin.addClass('selected active');
		tabSignup.removeClass('selected active');
	}

	function signup_selected() {
		formSignup.addClass('is-selected').siblings('#cd-login, #cd-reset-password').removeClass('is-selected');
		tabSignup.addClass('selected active');
		tabLogin.removeClass('selected active');
	}

	function forgot_password_selected() {
		formForgotPassword.addClass('is-selected').siblings('#cd-login, #cd-signup').removeClass('is-selected');
		tabLogin.removeClass('active');
		tabSignup.removeClass('active');
	}
	$('.trigger').on('click', function(event) {
		event.preventDefault();
		formModal.addClass('is-visible');
		login_selected(); // Добавлено, чтобы открыть вкладку "Sign In"
	});





});






//---------------------------------------------------------------------------------------------------
$(document).ready(function() {
    // Обработчик для формы "Войти"
  $('#formLogin').on('submit', function(event) {
     event.preventDefault();
         // Получение данных из формы
         var username = $('#signin-username').val();
         var password = $('#signin-password').val();

         // Отправка запроса на сервер для аутентификации
         $.ajax({
             url: '/login', // URL контроллера для обработки логина
             type: 'POST',
             data: { username: username, password: password },
             success: function(response, status, xhr) {
                 // Обработка успешного ответа
                 console.log('Successful login:', response);
                 // Сохраняем состояние аутентификации в localStorage

                 if (response === 'success') {
                                 localStorage.setItem('isLoggedIn', true);
                                                  // Скрываем кнопку "Войти", показываем кнопку "Кабинет"
                                                  localStorage.setItem('username', username)
                                                  $('#loginBtn').hide();
                                                  $('#profileBtn').show();
                                                  // Перенаправление пользователя на главную страницу или другую страницу при успешной аутентификации
                                                  window.location.href = '/'; // Или другой URL
                             } else {
                                 // Отображаем сообщение об ошибке
                                 $('#login-error-message').show();
                             }
             },
             error: function(xhr, status, error) {
                 // Обработка ошибки

                 // Вывод сообщения об ошибке пользователю
             }
      });
  });


  if (localStorage.getItem('isLoggedIn')) {
              $('#loginBtn').hide();
              $('#profileBtn').show();
          }
});



//---------------------------------------------------------------------------------------------------


