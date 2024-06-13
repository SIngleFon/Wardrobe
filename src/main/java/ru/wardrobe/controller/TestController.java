//package ru.wardrobe.controller;
//
//import lombok.AllArgsConstructor;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RestController;
//import ru.wardrobe.model.*;
//import ru.wardrobe.model.enums.EnumColor;
//import ru.wardrobe.model.enums.EnumComposition;
//import ru.wardrobe.model.enums.EnumSeason;
//import ru.wardrobe.model.items.*;
//import ru.wardrobe.service.AccountService;
//import ru.wardrobe.service.ItemService;
//import ru.wardrobe.service.WardrobeService;
//
//import java.util.*;
///**
// * Контроллер для быстрого создания учетной записи для тестирования приложения (можно игнорировать)
// *
// */
//@RestController
//@AllArgsConstructor
//public class TestController {
//    private final AccountService aService;
//    private final ItemService iService;
//    private final WardrobeService wService;
//    /**
//     * Создает тестовые данные: учетную запись пользователя, шкаф и предметы.
//     * @return Список учетных записей после создания тестовых данных.
//     */
//    @GetMapping("/create")
//    public List<Account> tt(){
//        String username = "singlef";
//
//        // Создаем новую учетную запись
//        Account account = new Account(username, "email", "123123", "USER");
//        Account savedAccount = aService.save(account);
//
//        // Создаем новый шкаф
//        Wardrobe wardrobe = new Wardrobe("Шкаф_1");
//        String imageUrl = "https://business-click.it/images/portfolio/cappelledelcommiatofirenze.png";
//        wardrobe.setImageUrl(imageUrl);
//        wardrobe.setAccount(savedAccount);
//        wService.save(wardrobe);
//
//        // Связываем шкаф с учетной записью
//        savedAccount.getWardrobes().add(wardrobe);
//        aService.save(savedAccount);
//
//        // Возвращаем список всех учетных записей после создания тестовых данных
//        return getTest(username);
//    }
//
//    /**
//     * Получает все учетные записи.
//     * @return Список всех учетных записей.
//     */
//    @GetMapping("/b")
//    public List<Account> bs(){
//        return (List<Account>) aService.getAll();
//    }
//
//    /**
//     * Выполняет тестирование генерации случайных предметов и их сохранение в базу данных.
//     * @param username Имя пользователя, для которого выполняется тест.
//     * @return Список всех учетных записей после выполнения теста.
//     */
//    @GetMapping("/test")
//    public List<Account> getTest(String username) {
//        Account singlef = aService.getUserByUsername(username);
//        if (singlef != null) {
//            List<Item> randomItems = generateRandomItems(singlef.getWardrobes());
//            randomItems.forEach(item -> System.out.println("Создан случайный предмет: " + item));
//            singlef.getWardrobes().forEach(wardrobe -> {
//                System.out.println("Добавляем случайные предметы в шкаф " + wardrobe.getName());
//                List<Item> itemsCopy = new ArrayList<>(randomItems);
//                itemsCopy.forEach(item -> item.setWardrobe(wardrobe));
//                itemsCopy.forEach(item -> iService.save(item));
//            });
//            singlef = aService.save(singlef);
//        }
//        return (List<Account>) aService.getAll();
//    }
//
//    /**
//     * Генерирует случайные предметы для каждого шкафа из списка шкафов.
//     * @param wardrobes Список шкафов, для которых генерируются предметы.
//     * @return Список сгенерированных случайных предметов.
//     */
//    private List<Item> generateRandomItems(List<Wardrobe> wardrobes) {
//        List<Item> randomItems = new ArrayList<>();
//        EnumColor[] colors = EnumColor.values();
//        Random random = new Random();
//
//        wardrobes.forEach(wardrobe -> {
//            int itemCount = random.nextInt(5) + 1;
//
//            for (int i = 0; i < itemCount; i++) {
//                Item item;
//                String type = getRandomType();
//                EnumSeason season = EnumSeason.values()[random.nextInt(EnumSeason.values().length)];
//
//                switch (type) {
//                    case "UPPERWEAR":
//                        item = new Upperwear();
//
//                        break;
//                    case "ACCESSORY":
//                        item = new Accessory();
//
//                        break;
//                    case "BOTTOMWEAR":
//                        item = new Bottomwear();
//
//                        break;
//                    case "FOOTWEAR":
//                        item = new Footwear();
//
//                        break;
//                    case "CLOTHING":
//                        item = new Clothing();
//                        Set<EnumComposition> compositions = new HashSet<>();
//
//                        // Генерируем случайное количество случайных значений EnumComposition
//                        int numberOfCompositions = random.nextInt(EnumComposition.values().length) + 1; // хотя бы один элемент
//                        for (int j = 0; j < numberOfCompositions; j++) {
//                            int randomIndex = random.nextInt(EnumComposition.values().length);
//                            EnumComposition composition = EnumComposition.values()[randomIndex];
//                            compositions.add(composition);
//                        }
//                        ((Clothing) item).setMaterial(compositions);
//                        break;
//                    default:
//                        throw new IllegalStateException("Unexpected value: " + type);
//                }
//                String imageUrl = "https://business-click.it/images/portfolio/cappelledelcommiatofirenze.png";
//                item.setImageUrl(imageUrl);
//                item.setColors(new HashSet<>(Collections.singletonList(colors[random.nextInt(colors.length)])));
//                item.setDescription("Random Description " + i);
//                item.setSeason(season);
//                item = iService.save(item);
//                randomItems.add(item);
//            }
//        });
//
//        return randomItems;
//    }
//
//    /**
//     * Генерирует случайный тип предмета из списка доступных типов.
//     * @return Случайный тип предмета.
//     */
//    private String getRandomType() {
//        String[] types = {"UPPERWEAR", "ACCESSORY", "BOTTOMWEAR", "FOOTWEAR", "CLOTHING"};
//        return types[new Random().nextInt(types.length)];
//    }
//
//    private String getRandomMaterial() {
//        EnumComposition[] materials = EnumComposition.values();
//        return materials[new Random().nextInt(materials.length)].name();
//    }
//}
//
