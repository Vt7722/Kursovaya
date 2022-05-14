import org.litote.kmongo.*

val groupDb = mongoDatabase.getCollection<Group>().apply { drop() }
fun main() {
    var exit = true
    while (exit) {
        println("Список групп и студентов\n" +
                "Выберите действие\n" +
                "1. Просмотреть список групп\n" +
                "2. Добавить группу\n" +
                "3. Добавить студента в группу\n" +
                "4. Изменить информацию о студенте\n" +
                "5. Перевести студента\n" +
                "6. Удалить студента\n" +
                "7. Удалить группу\n" +
                "8. Импорт из формата CSV\n" +
                "9. Экспорт в формат CSV\n" +
                "Для выхода введите \"e\"")
        println("\nВаш ответ: ")
        when (readLine()) {
            "1" -> {
                println("СПИСОК ГРУПП")
                prettyPrintCursor(groupDb.find().sort("{'number':1}"))
            }
            "2" -> {
                println("ДОБАВЛЕНИЕ ГРУППЫ")
                addGroup()
                prettyPrintCursor(groupDb.find().sort("{'number':1}"))
            }
            "3" -> {
                println("ДОБАВЛЕНИЕ СТУДЕНТА В ГРУППУ")
                addStudentInGroup(inpGrp(), inpSt())
                prettyPrintCursor(groupDb.find().sort("{'number':1}"))
            }
            "4" -> {
                println("ИЗМЕНЕНИЕ ИНФОРМАЦИИ О СТУДЕНТЕ")
                renameStudent()
                prettyPrintCursor(groupDb.find().sort("{'number':1}"))
            }
            "5" -> {
                println("ПЕРЕВОД СТУДЕНТА")
                transfer()
                prettyPrintCursor(groupDb.find().sort("{'number':1}"))
            }
            "6" -> {
                println("УДАЛЕНИЕ СТУДЕНТА ИЗ ГРУППЫ")
                deleteStudentInGroup(inpGrp(), inpSt())
                prettyPrintCursor(groupDb.find().sort("{'number':1}"))
            }
            "7" -> {
                println("УДАЛЕНИЕ ГРУППЫ")
                deleteGroup()
                prettyPrintCursor(groupDb.find().sort("{'number':1}"))
            }
            "8" -> {
                println("ИМПОРТ СПИСКА ГРУПП")
                import()
                prettyPrintCursor(groupDb.find().sort("{'number':1}"))
            }
            "9" -> {
                println("ЭКСПОРТ СПИСКА ГРУПП")
                export()
            }
            "e" -> {
                println("ВЫХОД")
                exit = false
            }
        }
    }
}