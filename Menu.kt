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
                "5. Перевести судента\n" +
                "6. Удалить студента\n" +
                "7. Удалить группу\n" +
                "8. Импорт из формата CSV\n" +
                "9. Эксопрт в формат CSV\n" +
                "Для выхода введите \"e\"")
        when (readLine()) {
            "1" -> {
                println("СПИСОК ГРУПП")
                prettyPrintCursor(groupDb.find().sort("{'number':1}"))
            }
            "2" -> {
                println("ДОБАВЛЕНИЕ ГРУПП")
                addGroup()
            }
            "3" -> {
                addStudentInGroup(inpGrp(), inpSt())
            }
            "4" -> {
                renameStudent()
            }
            "5" -> {
                transfer()
            }
            "6" -> {
                deleteStudentInGroup(inpGrp(), inpSt())
            }
            "7" -> {
                deleteGroup()
            }
            "8" -> {
                import()
            }
            "9" -> {
                export()
            }
            "e" -> {
                println("ВЫХОД")
                exit = false
            }
        }
    }
}