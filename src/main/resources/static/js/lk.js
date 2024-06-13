document.addEventListener('DOMContentLoaded', function() {
    var username = localStorage.getItem('username');
    var currentWardrobeId = null;

    const wardrobeLinks = document.querySelectorAll('.wardrobe-link');
    // Функция для отправки запроса на получение данных о шкафах
    function fetchWardrobes() {
            if (window.location.pathname.includes(`/${username}/lk`)) {
                fetch(`/api/${username}/wardrobes`)
                    .then(response => response.json())
                    .then(data => {
                        const wardrobesContainer = document.getElementById('wardrobes');

                        data.forEach(wardrobe => {
                            const wardrobeItem = document.createElement('li');
                            wardrobeItem.className = 'wardrobe-card';

                            const wardrobeLink = document.createElement('a');
                            wardrobeLink.href = `/${username}/lk/${wardrobe.id}`;
                            wardrobeLink.className = 'wardrobe-link';

                            const wardrobeImg = document.createElement('img');
                            wardrobeImg.className = 'wardrobe__image';

                            // Извлекаем имя файла из URL-адреса imageUrl
                            const filename = wardrobe.imageUrl.split('/').pop();

                            // Отправляем GET-запрос на ImageController для получения изображения
                            fetch(`/api/images/${filename}`)
                                .then(response => {
                                    if (response.ok) {
                                        return response.blob();
                                    }
                                    throw new Error('Network response was not ok.');
                                })
                                .then(blob => {
                                    const objectURL = URL.createObjectURL(blob);
                                    wardrobeImg.src = objectURL;
                                })
                                .catch(error => {
                                    console.error('Ошибка при получении изображения:', error);
                                    // В случае ошибки загрузки изображения, используем резервное изображение
                                    wardrobeImg.src = 'https://business-click.it/images/portfolio/cappelledelcommiatofirenze.png';
                                });

                            const editButton = document.createElement('button');
                            editButton.className = 'edit-button';
                            editButton.innerHTML = '<img src="https://img.icons8.com/ios-filled/50/000000/edit.png" alt="Edit">';
                            editButton.dataset.id = wardrobe.id;
                            editButton.dataset.name = wardrobe.name;

                            editButton.addEventListener('click', function(event) {
                                event.stopPropagation();
                                event.preventDefault();
                                currentWardrobeId = this.dataset.id;
                                document.getElementById('editModal').style.display = 'block';

                                // Получаем ссылку на изображение из карточки
                                const wardrobeImgSrc = wardrobeImg.src;

                                // Устанавливаем значение src для изображения в модальном окне редактирования
                                document.querySelector('#editModal .wardrobeImage').src = wardrobeImgSrc;

                                // Загружаем данные о шкафе
                                fetch(`/api/${username}/wardrobe/${wardrobe.id}`)
                                    .then(response => response.json())
                                    .then(wardrobe => {
                                        const wardrobeNameInput = document.getElementById('wardrobeName');
                                        if (wardrobeNameInput) {
                                            wardrobeNameInput.value = wardrobe.name;
                                        } else {
                                            console.error('Элемент с id wardrobeName не найден');
                                        }
                                    })
                                    .catch(error => console.error('Ошибка при загрузке данных о шкафе:', error));
                            });

                            const wardrobeTitle = document.createElement('div');
                            wardrobeTitle.className = 'wardrobe__title';
                            wardrobeTitle.textContent = wardrobe.name;

                            wardrobeLink.appendChild(wardrobeImg);
                            wardrobeLink.appendChild(wardrobeTitle);

                            wardrobeItem.appendChild(wardrobeLink);
                            wardrobeItem.appendChild(editButton);

                            wardrobesContainer.appendChild(wardrobeItem);
                        });

                    })
                    .catch(error => console.error('Ошибка при загрузке данных:', error));
            }
        }


    fetchWardrobes();

document.getElementById('editForm').addEventListener('submit', function(event) {
    event.preventDefault();
    const wardrobeNameInput = document.getElementById('wardrobeName');
    const newWardrobeName = wardrobeNameInput.value;
    const newImage = document.getElementById('editImageInput').files[0];

    if (currentWardrobeId && newWardrobeName) {
        const formData = new FormData();
        formData.append('wardrobe', new Blob([JSON.stringify({ name: newWardrobeName })], { type: 'application/json' }));

        // Проверяем, было ли выбрано новое изображение
        if (newImage) {
            formData.append('image', newImage);
        } else {
            // Если новое изображение не выбрано, используем ссылку на текущее изображение
            formData.append('imageUrl', currentImageSrc);
        }

        updateWardrobeData(currentWardrobeId, formData);
        document.getElementById('editModal').style.display = 'none';
    } else {
        console.error('Не удалось получить идентификатор шкафа или новое имя');
    }
});
    function deleteWardrobeData(wardrobeId) {
            fetch(`/api/${username}/wardrobe/${wardrobeId}`, {
                method: 'DELETE',
                headers: {
                    'Content-Type': 'application/json'
                }
            })
            .then(response => {
                if (response.ok) {
                    console.log('Шкаф успешно удален');
                    window.location.reload();
                } else {
                    console.error('Ошибка при удалении шкафа:', response.status);
                }
            })
            .catch(error => console.error('Ошибка при отправке запроса на удаление шкафа:', error));
        }

        document.getElementById('deleteButton').addEventListener('click', function(event) {
            if (currentWardrobeId) {
                deleteWardrobeData(currentWardrobeId);
            } else {
                console.error('Не удалось получить идентификатор шкафа для удаления');
            }
        });
    // Функция для отправки данных о шкафе на сервер
    function updateWardrobeData(wardrobeId, formData) {
                    fetch(`/api/${username}/wardrobe/${wardrobeId}`, {
                        method: 'PUT',
                        body: formData
                    })
                    .then(response => {
                        if (response.ok) {
                            console.log('Шкаф успешно обновлен');
                            fetchWardrobes();
                            window.location.reload();
                        } else {
                            console.error('Ошибка при обновлении шкафа:', response.status);
                        }
                    })
                    .catch(error => console.error('Ошибка при отправке запроса на обновление шкафа:', error));
                }

    // Обработчик для кнопки закрытия модального окна
    document.querySelector('.close').addEventListener('click', function() {
        document.getElementById('editModal').style.display = 'none';
    });

    // Обработчик для закрытия модального окна при клике вне него
    window.addEventListener('click', function(event) {
        const modal = document.getElementById('editModal');
        if (event.target === modal) {
            modal.style.display = 'none';
        }
    });

    // Обработчик для кнопки создания нового шкафа
    document.getElementById('createWardrobeButton').addEventListener('click', function() {
        var createModal = document.getElementById('createModal');
        createModal.style.display = 'block';
    });

    // Обработчик для кнопки закрытия модального окна создания нового шкафа
    document.querySelector('#createModal .close').addEventListener('click', function() {
        document.getElementById('createModal').style.display = 'none';
    });

    // Обработчик для закрытия модального окна при клике вне него
    window.addEventListener('click', function(event) {
        const createModal = document.getElementById('createModal');
        if (event.target === createModal) {
            createModal.style.display = 'none';
        }
    });

    // Обработчик отправки формы создания нового шкафа
    document.getElementById('createForm').addEventListener('submit', function(event) {
        event.preventDefault();

        var formData = new FormData();
        var newWardrobeName = document.getElementById('newWardrobeName').value;
        var createImageInput = document.getElementById('createImageInput');
        var imageFile = createImageInput.files[0];

        formData.append('wardrobe', new Blob([JSON.stringify({ name: newWardrobeName })], { type: 'application/json' }));
        formData.append('image', imageFile);

        fetch(`/api/${username}/wardrobes`, {
            method: 'POST',
            body: formData
        })
        .then(response => {
            if (response.ok) {
                console.log('Шкаф успешно создан');
                document.getElementById('createModal').style.display = 'none';
                window.location.reload();
            } else {
                console.error('Ошибка при создании шкафа:', response.status);
            }
        })
        .catch(error => console.error('Ошибка при отправке запроса на создание шкафа:', error));
    });



// Функция для обработки выбора изображения
    function handleImageChange(event, inputId, imageId) {
        var inputFile = document.getElementById(inputId);
        var wardrobeImage = document.querySelector(imageId);

        inputFile.addEventListener('change', function(event) {
            var file = event.target.files[0];
            if (file) {
                var reader = new FileReader();
                reader.onload = function(e) {
                    wardrobeImage.src = e.target.result;
                }
                reader.readAsDataURL(file);
            }
        });

        inputFile.click();
    }

    // Обработчики для кнопок изменения изображения
    document.querySelector('#editModal .changeImageButton').addEventListener('click', function() {
        handleImageChange(event, 'editImageInput', '#editModal .wardrobeImage');
    });

    document.querySelector('#createModal .changeImageButton').addEventListener('click', function() {
        handleImageChange(event, 'createImageInput', '#createModal .wardrobeImage');
    });
});
