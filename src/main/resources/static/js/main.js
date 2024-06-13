

window.addEventListener("scroll", function() {
    const scrollPos = window.scrollY
    const headerEl = document.getElementById("header")
    if(scrollPos > 100){
        headerEl.classList.add("container_mini")
    }else{
        headerEl.classList.remove("container_mini")

    }
})

	// //REMOVE THIS - it's just to show error messages
	// formLogin.find('input[type="submit"]').on('click', function(event) {
	//   event.preventDefault();
	//   formLogin.find('input[type="email"]').toggleClass('has-error').next('span').toggleClass('is-visible');
	// });
	// formSignup.find('input[type="submit"]').on('click', function(event) {
	//   event.preventDefault();
	//   formSignup.find('input[type="email"]').toggleClass('has-error').next('span').toggleClass('is-visible');
	// });
  
  
	//IE9 placeholder fallback
	//credits http://www.hagenburger.net/BLOG/HTML5-Input-Placeholder-Fix-With-jQuery.html
if (!Modernizr.input.placeholder) {
	  $('[placeholder]').focus(function() {
		var input = $(this);
		if (input.val() == input.attr('placeholder')) {
		  input.val('');
		}
	  }).blur(function() {
		var input = $(this);
		if (input.val() == '' || input.val() == input.attr('placeholder')) {
			input.val(input.attr('placeholder'));
		  }
		});
}




$(document).ready(function() {
    // Проверяем состояние аутентификации при загрузке страницы


    var hideMenuTimeout;

    function showProfileMenu() {
        clearTimeout(hideMenuTimeout);
        $('.profile-menu').stop(true, true).fadeIn();
        // Скрываем меню через 6 секунд
        hideMenuTimeout = setTimeout(hideProfileMenu, 6000);
    }

    function hideProfileMenu() {
        $('.profile-menu').fadeOut();
    }

    // Обработчик для кнопки "Кабинет"
    $('#profileBtn .profile-btn').on('click', function(event) {
        event.stopPropagation(); // Останавливаем всплытие события
        showProfileMenu();
    });

    // Обработчик для кликов вне меню
    $(document).on('click', function(event) {
        if (!$(event.target).closest('.profile-menu, .profile-btn').length) {
            hideProfileMenu();
        }
    });

    $('.profile-link').on('click', function(event) {
        event.preventDefault();
        // Получаем имя пользователя из localStorage
        var username = localStorage.getItem('username');
        if (username) {
            // Переходим на страницу личного кабинета для этого пользователя
            window.location.href = '/' + username + '/lk';
        }
    });

    $('.logout-link').on('click', function(event) {
        event.preventDefault();
        localStorage.removeItem('isLoggedIn');
        localStorage.removeItem('username');
        $('#profileBtn').hide();
        $('#loginBtn').show();
        window.location.href = "/index"
    });
});


document.addEventListener('DOMContentLoaded', function() {
  var message = document.getElementById('registration-message');
    if (message) {
      setTimeout(function() {
        message.style.display = 'none';
      }, 4000); // Скрывать сообщение через 4 секунды
    }
});

document.addEventListener('DOMContentLoaded', function() {
  var message = document.getElementById('login-message');
  if (message) {
    setTimeout(function() {
      message.style.display = 'none';
    }, 4000); // Скрывать сообщение через 4 секунды
  }
});

// Получаем ссылку на модальное окно и кнопку "Контакты"
var contactModal = document.getElementById("contactModal");
var contactBtn = document.getElementById("contactBtn");

// Получаем кнопку закрытия модального окна
var closeBtn = document.getElementsByClassName("close")[0];

// Открываем модальное окно при клике на кнопку "Контакты"
contactBtn.onclick = function() {
  contactModal.style.display = "block";
}

// Закрываем модальное окно при клике на кнопку закрытия
closeBtn.onclick = function() {
  contactModal.style.display = "none";
}

// Закрываем модальное окно при клике за его пределами
window.onclick = function(event) {
  if (event.target == contactModal) {
    contactModal.style.display = "none";
  }
}