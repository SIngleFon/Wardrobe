document.addEventListener('DOMContentLoaded', function() {
var username = localStorage.getItem('username');
var currentItemId = null;
    function fetchItems() {
        var wardrobeId = window.location.pathname.split('/').pop();
        const itemsContainer = document.getElementById('items');
        fetch(`/api/${username}/wardrobe/${wardrobeId}`)
            .then(response => response.json())
            .then(data => {
                itemsContainer.innerHTML = ''; // Очищаем контейнер перед добавлением новых элементов

                data.items.forEach(item => {
                    const itemCard = document.createElement('li');
                        itemCard.className = 'item-card';


                        // Добавляем атрибуты данных для фильтрации
                        itemCard.setAttribute('data-type', item.type);
                        itemCard.setAttribute('data-colors', JSON.stringify(item.colors));
                        itemCard.setAttribute('data-season', item.season);


                        const itemLink = document.createElement('a');
                        itemLink.className = 'item-link';
                        itemLink.href = `/${username}/lk/${wardrobeId}/${item.id}`;

                        //Временно нереализовано
                        itemLink.addEventListener('click', function(event) {
                            event.preventDefault(); // Предотвращаем стандартное действие перехода по ссылке
                        });


                        const itemImage = document.createElement('img');
                        itemImage.className = 'item__image';

                        // Извлекаем имя файла из URL-адреса imageUrl
                        const filename = item.imageUrl.split('/').pop();

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
                                itemImage.src = objectURL;
                            })
                            .catch(error => {
                                console.error('Ошибка при получении изображения:', error);
                                // В случае ошибки загрузки изображения, используем резервное изображение
                                itemImage.src = 'https://business-click.it/images/portfolio/cappelledelcommiatofirenze.png';
                            });

                        const itemDetails = document.createElement('div');
                        itemDetails.className = 'item__details';

                        const editButton = document.createElement('button');
                                        editButton.className = 'edit-button';
                                        editButton.innerHTML = '<img src="https://img.icons8.com/ios-filled/50/000000/edit.png" alt="Edit">';
                                        editButton.dataset.id = item.id;
                                        editButton.dataset.type = item.type;

                                        editButton.addEventListener('click', function(event) {
                                            event.stopPropagation();
                                            event.preventDefault();
                                            currentItemId = this.dataset.id;
                                            const itemId = this.dataset.id; // Получаем itemId из dataset кнопки
                                            const itemImgSrc = itemImage.src;
                                            openEditItemModal({
                                                id: itemId, // Передаем itemId
                                                description: item.description, // Получаем описание из item
                                                type: this.dataset.type,
                                                img: itemImgSrc
                                            });
                                        });

                        const description = document.createElement('p');
                        description.className = 'item__description';
                        description.textContent = `Описание: ${item.description}`;

                        let type = ''; // Создаем переменную для типа
                        if (item.type === 'clothing') { // Проверяем, если тип - clothing, меняем на Одежда
                            type = document.createElement('p');
                            type.className = 'item__type';
                            type.textContent = 'Тип: Одежда';
                        }else if (item.type === 'footwear') {
                            type = document.createElement('p');
                            type.className = 'item__type';
                            type.textContent = 'Тип: Обувь';
                        }else if (item.type === 'upperwear') {
                            type = document.createElement('p');
                            type.className = 'item__type';
                            type.textContent = 'Тип: Верхняя одежда';
                        }else if (item.type === 'accessory') {
                             type = document.createElement('p');
                             type.className = 'item__type';
                             type.textContent = 'Тип: Аксессуары';
                        }else if (item.type === 'bottomwear') {
                             type = document.createElement('p');
                             type.className = 'item__type';
                             type.textContent = 'Тип: Джинсы, брюки';
                        }else {
                             type = document.createElement('p');
                             type.className = 'item__type';
                             type.textContent = `Тип: ${item.type}`;
                        }
                        const color = document.createElement('p');
                        color.className = 'item__color';
                        if (item.colors) {
                            color.textContent = `Цвет: ${item.colors.join(', ')}`;
                        }

                        const season = document.createElement('p');
                        season.className = 'item__season';
                        season.textContent = `Сезон: ${item.season}`;

                        let material = ''; // Создаем переменную для материала
                                                if (item.material) { // Проверяем, есть ли материал у предмета
                                                    material = document.createElement('p');
                                                    material.className = 'item__material';
                                                    material.textContent = `Материал: ${item.material}`;
                                                }
                        itemLink.appendChild(itemImage);
                        itemDetails.appendChild(type);
                        if (item.colors) {
                            itemDetails.appendChild(color);
                        }
                        itemDetails.appendChild(season);
                        if (material) { // Добавляем материал только если он есть
                            itemDetails.appendChild(material);
                        }
                        /*itemDetails.appendChild(title);*/
                        itemDetails.appendChild(description);
                        itemCard.appendChild(itemLink);
                        itemCard.appendChild(itemDetails);
                        itemCard.appendChild(editButton);
                        itemsContainer.appendChild(itemCard);
                });
            })
            .catch(error => console.error('Ошибка при загрузке данных:', error));
    }

    fetchItems();

document.getElementById('openCreateItemModalButton').addEventListener('click', function() {
    var createItemModal = document.getElementById('createItemModal');
    createItemModal.style.display = 'block';
});

document.getElementById('newItemType').addEventListener('change', function() {
    // Сначала скрываем все дополнительные поля
    document.getElementById('upperwearFields').style.display = 'none';
    document.getElementById('accessoryFields').style.display = 'none';
    document.getElementById('bottomwearFields').style.display = 'none';
    document.getElementById('footwearFields').style.display = 'none';
    document.getElementById('clothingFields').style.display = 'none';

    // Затем показываем поля, соответствующие выбранному типу
    const type = this.value;
    if (type === 'UPPERWEAR') {
        document.getElementById('upperwearFields').style.display = 'block';
    } else if (type === 'ACCESSORY') {
        document.getElementById('accessoryFields').style.display = 'block';
    } else if (type === 'BOTTOMWEAR') {
        document.getElementById('bottomwearFields').style.display = 'block';
    } else if (type === 'FOOTWEAR') {
        document.getElementById('footwearFields').style.display = 'block';
    } else if (type === 'CLOTHING') {
        document.getElementById('clothingFields').style.display = 'block';
    }
});


document.getElementById('createItemForm').addEventListener('submit', function(event) {
    event.preventDefault();

    const formData = new FormData(); // Создаем объект FormData для отправки данных формы

    // Добавляем текстовые данные в объект FormData
    formData.append('type', document.getElementById('newItemType').value);
    formData.append('description', document.getElementById('newItemDescription').value);
    formData.append('season', document.getElementById('newItemSeason').value);

    // Добавляем выбранные цвета в объект FormData
    const selectedColors = document.getElementById('selectedColors').children;
    Array.from(selectedColors).forEach(colorBlock => {
        formData.append('colors[]', colorBlock.textContent.trim().replace('×', ''));
    });

    // Добавляем изображение в объект FormData
    const imageInput = document.getElementById('createImageInput');
    if (imageInput.files[0]) {
        formData.append('file', imageInput.files[0]);
    } else {
        // Если пользователь не выбрал изображение, добавляем заглушку
        formData.append('file', 'https://business-click.it/images/portfolio/cappelledelcommiatofirenze.png');
    }

    // Пример отправки данных с использованием fetch
    const wardrobeId = window.location.pathname.split('/').pop();
    const url = `/api/${username}/wardrobe/${wardrobeId}/items`;

    fetch(url, {
        method: 'POST',
        body: formData // Передаем объект FormData в теле запроса
    })
    .then(response => {
        if (response.ok) {
            console.log('Элемент успешно создан');
            document.getElementById('createItemModal').style.display = 'none';
            window.location.reload();
        } else {
            console.error('Ошибка при создании элемента:', response.status);
        }
    })
    .catch(error => console.error('Ошибка при отправке запроса на создание элемента:', error));
});

// Обработчик для закрытия модального окна при клике вне него
window.addEventListener('click', function(event) {
    const createItemModal = document.getElementById('createItemModal');
    if (event.target === createItemModal) {
        createItemModal.style.display = 'none';
    }
});


document.getElementById('newItemColor').addEventListener('change', function(event) {
        var selectedColors = document.getElementById('selectedColors');
        var selectedColor = event.target.value;

        // Проверяем, выбран ли уже этот цвет
        var isColorSelected = Array.from(selectedColors.children).some(function(element) {
            return element.textContent.trim() === selectedColor;
        });

        if (!isColorSelected) {
            var colorElement = document.createElement('div');
            colorElement.textContent = selectedColor;

            // Добавляем кнопку для удаления выбранного цвета
            var deleteButton = document.createElement('button');
            deleteButton.textContent = '×';
            deleteButton.onclick = function() {
                selectedColors.removeChild(colorElement);
                // Возвращаем выбранный цвет в список
                var option = document.createElement('option');
                option.value = selectedColor;
                option.textContent = selectedColor;
                event.target.appendChild(option);
            };
            colorElement.appendChild(deleteButton);

            selectedColors.appendChild(colorElement);

            // Удаляем выбранный цвет из списка выбора
            event.target.removeChild(event.target.querySelector('option[value="' + selectedColor + '"]'));
        }
    });

    document.getElementById('newItemMaterial').addEventListener('change', function(event) {
        var selectedMaterials = document.getElementById('selectedMaterials');
        var selectedMaterial = event.target.value;

        // Проверяем, выбран ли уже этот материал
        var isMaterialSelected = Array.from(selectedMaterials.children).some(function(element) {
            return element.textContent.trim() === selectedMaterial;
        });

        if (!isMaterialSelected) {
            var materialElement = document.createElement('div');
            materialElement.textContent = selectedMaterial;

            // Добавляем кнопку для удаления выбранного материала
            var deleteButton = document.createElement('button');
            deleteButton.textContent = '×';
            deleteButton.onclick = function() {
                selectedMaterials.removeChild(materialElement);
                // Возвращаем выбранный материал в список
                var option = document.createElement('option');
                option.value = selectedMaterial;
                option.textContent = selectedMaterial;
                event.target.appendChild(option);
            };
            materialElement.appendChild(deleteButton);

            selectedMaterials.appendChild(materialElement);

            // Удаляем выбранный материал из списка выбора
            event.target.removeChild(event.target.querySelector('option[value="' + selectedMaterial + '"]'));
        }
    });


document.getElementById('saveEditButton').addEventListener('click', function(event) {
    event.preventDefault();

    const itemImage = document.querySelector('#editModal .itemImage');
    const itemId = currentItemId;
    const formData = new FormData();
    formData.append('description', document.getElementById('editItemDescription').value);
    console.log(itemId);
    // Получаем выбранный цвет из select элемента
    const newItemColorSelect = document.querySelector('.newItemColor');
    const selectedColor = newItemColorSelect.options[newItemColorSelect.selectedIndex].value;

    // Если выбран цвет, добавляем его в FormData
    if (selectedColor) {
        formData.append('colors', selectedColor.toUpperCase());
    }

    formData.append('season', document.querySelector('.newItemSeason').value.toUpperCase());

    const imageInput = document.getElementById('editImageInput');
    if (imageInput.files[0]) {
        formData.append('file', imageInput.files[0]);
    }

    fetch(`/api/${username}/item/${itemId}`, {
        method: 'PUT',
        body: formData
    })
    .then(response => {
        if (response.ok) {
            console.log('Элемент успешно обновлен');
            document.getElementById('editModal').style.display = 'none';
            window.location.reload();
        } else {
            console.error('Ошибка при обновлении элемента:', response.status);
        }
    })
    .catch(error => console.error('Ошибка при отправке запроса на обновление элемента:', error));
});

function deleteItemData(itemId) {
            fetch(`/api/${username}/item/${itemId}`, {
                method: 'DELETE',
                headers: {
                    'Content-Type': 'application/json'
                }
            })
            .then(response => {
                if (response.ok) {
                    console.log('Предмет успешно удален');
                    window.location.reload();
                } else {
                    console.error('Ошибка при удалении предмета:', response.status);
                }
            })
            .catch(error => console.error('Ошибка при отправке запроса на удаление предматов:', error));
        }

document.getElementById('deleteButton').addEventListener('click', function(event) {
            if (currentItemId) {
                deleteItemData(currentItemId);
            } else {
                console.error('Не удалось получить идентификатор предмета для удаления');
            }
        });


function openEditItemModal(item) {
    document.querySelectorAll('.editItemFields').forEach(field => {
        field.style.display = 'none';
    });

    // Загружаем данные о предмете
    const url = `/api/${username}/item/${item.id}`;
    fetch(url)
        .then(response => response.json())
        .then(itemData => {
            // Заполняем общие поля
            document.getElementById('editItemDescription').value = itemData.description;
            document.querySelector('.newItemColor').value = itemData.colors ? itemData.colors.join(', ') : '';
            document.querySelector('.newItemSeason').value = itemData.season || '';

            // Показываем поля в зависимости от типа элемента
            if (itemData.type === 'ACCESSORY') {
                document.querySelector('.accessoryFields').style.display = 'block';
            } else if (itemData.type === 'FOOTWEAR') {
                document.querySelector('.footwearFields').style.display = 'block';
            } else if (itemData.type === 'CLOTHING') {
                document.querySelector('.clothingFields').style.display = 'block';
                document.querySelector('.newItemMaterial').value = itemData.material || '';
            }

            // Устанавливаем изображение
            const itemImage = document.querySelector('#editModal .itemImage');
            if (itemImage) {
                itemImage.src = item.img;
            } else {
                console.error('Элемент с классом .itemImage не найден в модальном окне редактирования.');
            }

            // Открываем модальное окно
            document.getElementById('editModal').style.display = 'block';
        })
        .catch(error => console.error('Ошибка при загрузке данных о элементе:', error));
}



document.querySelector('.close').addEventListener('click', function() {
    document.body.style.overflow = 'auto';
    document.getElementById('editModal').style.display = 'none';
});

window.addEventListener('click', function(event) {
    document.body.style.overflow = 'auto';
    const modal = document.getElementById('editModal');
    if (event.target === modal) {
        modal.style.display = 'none';
    }
});

function filterItems() {
    const type = document.getElementById('typeFilter').value.toLowerCase();
    const color = document.getElementById('colorFilter').value.toLowerCase();
    const season = document.getElementById('seasonFilter').value.toLowerCase();

    const items = document.querySelectorAll('.item-card');

    console.log("Тип фильтра:", type);
    console.log("Цвет фильтра:", color);
    console.log("Сезон фильтра:", season);

    items.forEach(item => {
        const itemType = item.getAttribute('data-type').toLowerCase();
        const itemColorsString = item.getAttribute('data-colors');
        const itemColors = JSON.parse(itemColorsString);
        console.log("Цвет элемента:", itemColors);
        const itemSeason = item.getAttribute('data-season').toLowerCase();

        console.log("Тип элемента:", itemType);
        console.log("Цвет элемента:", itemColors); // Добавленный вывод для отладки
        console.log("Сезон элемента:", itemSeason);

        let isVisible = true;

        if (type !== '' && type !== 'по типу') {
            isVisible = isVisible && (itemType === type);
        }

        if (color !== '' && color !== 'по цвету') {
            console.log("Цвет элемента для сравнения:", itemColors); // Добавленный вывод для отладки
            const colorLowerCase = color.toLowerCase(); // Приводим цвет к нижнему регистру
            isVisible = isVisible && itemColors.some(itemColor => itemColor.toLowerCase() === colorLowerCase); // Проверяем, есть ли выбранный цвет в массиве цветов элемента
        }

        if (season !== '' && season !== 'по сезону') {
            isVisible = isVisible && (itemSeason === season);
        }

        if (isVisible) {
            item.style.display = 'inline-block'; // Устанавливаем стиль для отображения элемента
            item.style.listStyleType = 'none'; // Устанавливаем стиль для маркера списка
        } else {
            item.style.display = 'none';
        }
    });
}

document.getElementById('typeFilter').addEventListener('change', filterItems);
document.getElementById('colorFilter').addEventListener('change', filterItems);
document.getElementById('seasonFilter').addEventListener('change', filterItems);

document.getElementById('resetFiltersButton').addEventListener('click', function() {
    // Сбросить выбранные значения фильтров
    document.getElementById('typeFilter').value = 'По типу';
    document.getElementById('colorFilter').value = 'По цвету';
    document.getElementById('seasonFilter').value = 'По сезону';

    // Вызвать функцию filterItems() для сброса отображения элементов
    filterItems();
});

// Функция для обработки выбора изображения
    function handleImageChange(inputId, imageId) {
        var inputFile = document.getElementById(inputId);
        var itemImage = document.querySelector(imageId);

        inputFile.addEventListener('change', function(event) {
            var file = event.target.files[0];
            if (file) {
                var reader = new FileReader();
                reader.onload = function(e) {
                    itemImage.src = e.target.result;
                }
                reader.readAsDataURL(file);
            }
        });

        inputFile.click();
    }

    // Обработчики для кнопок изменения изображения
    document.querySelector('#editModal .changeImageButton').addEventListener('click', function() {
        handleImageChange('editImageInput', '#editModal .itemImage');
    });

    document.querySelector('#createItemModal .changeImageButton').addEventListener('click', function() {
        handleImageChange('createImageInput', '#createItemModal .itemImage');
    });




});