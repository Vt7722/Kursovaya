import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import org.apache.commons.csv.CSVPrinter
import org.litote.kmongo.*
import java.io.FileReader
import java.io.FileWriter
import java.nio.charset.Charset
import java.util.*

fun init(): List<Student> {
    val groupDbList = groupDb.find().toList()
    var k = emptyList<Student>()
    groupDbList.map {
        k = it.podgroup1
    }
    return k
}

fun inpSt(): String {
    var chk = true
    println("Введите имя и фамилию студента")
    var str = readLine().toString()
    while (chk) {
        if (str == "") {
            println("Ошибка. Введите имя и фамилию студента")
            str = readLine().toString()
        } else if (' ' !in str.toCharArray()) {
            println("Ошибка.Возможно вы забыли указать имя или фамилию студента.\nВведите имя и фамилию студента")
            str = readLine().toString()
        } else {
            chk = false
        }
    }
    return str
}

fun inpGrp(): String {
    var chk = true
    println("Введите группу")
    var str = readLine().toString().toLowerCase()
    while (chk) {
        if (str == "") {
            println("Ошибка. Введите группу")
            str = readLine().toString()
        }else if (
            (str.toCharArray().size!=3)||
            (str.toCharArray()[0] !in '0'..'9')||
            (str.toCharArray()[1] !in '0'..'9')||
            (str.toCharArray()[2] !in 'а'..'я')) {
            println("Ошибка.Неправильно введен номер группы.\nВведите группу")
            str = readLine().toString()
        } else chk = false
    }
    return str
}

fun countChck(number: String): Boolean {
    val lst = groupDb.find(Group::number eq number).toList()
    val pg2 = lst.map{it.podgroup2.size}.toString()
    val pg1 = lst.map{it.podgroup1.size}.toString()
    return pg1<=pg2
}

//Добавить группу
fun addGroup() {
    groupDb.insertOne(Group(inpGrp()))

}

//Удалить группу
fun deleteGroup() {
    val number = inpGrp()
    groupDb.deleteOne(Group::number eq number)
}

//Добавить студента в группу
fun addStudentInGroup(number: String, student: String) {
    println("Добавление студента в группу")
    if (countChck(number)) {
        groupDb.updateOne(Group::number eq number,
            addToSet(Group::podgroup1, Student(student)), upsert())
    } else {
        groupDb.updateOne(Group::number eq number,
            addToSet(Group::podgroup2, Student(student)), upsert())
    }
}

//Удалить студента из группы
fun deleteStudentInGroup(number:String, student: String) {
    if (init().contains(Student(student))) {
        groupDb.updateOne(Group::number eq number,
            pull(Group::podgroup1, Student(student)))
    } else {
        groupDb.updateOne(Group::number eq number,
            pull(Group::podgroup2, Student(student)))
    }
}
//Изменить информацию о студенте
fun renameStudent() {
    val number = inpGrp()
    val student = inpSt()
    val chk = init().contains(Student(student))
    println("Ввод новых данных")
    val studentNew = inpSt()
    deleteStudentInGroup(number,student)
    if (chk) {
        groupDb.updateOne(Group::number eq number,
            push(Group::podgroup1, Student(studentNew)))
    } else {
        groupDb.updateOne(Group::number eq number,
            push(Group::podgroup2, Student(studentNew)))
    }
}

//Перевести студента
fun transfer() {
    println("Откуда")
    val number = inpGrp()
    val student = inpSt()
    println("Куда")
    val numberNew = inpGrp()
    deleteStudentInGroup(number,student)
    addStudentInGroup(numberNew,student)
}

fun import() {
    val csvParser = CSVParser(FileReader("D:\\IT\\kurs\\src\\main\\resources\\StudZag.csv"), CSVFormat.DEFAULT
        .withFirstRecordAsHeader()
        .withIgnoreHeaderCase()
        .withTrim())

    for (csvRecord in csvParser) {
        Charset.forName("Windows-1251")
        val group = csvRecord.get("Группа").toString()
        val pgroup = csvRecord.get("Подгруппа").toString().toInt()
        val name = csvRecord.get("Имя")
        val surname = csvRecord.get("Фамилия")
        if (pgroup == 1)
            groupDb.updateOne(Group::number eq group,
                addToSet(Group::podgroup1, Student(name, surname)), upsert())
        else
            groupDb.updateOne(Group::number eq group,
                addToSet(Group::podgroup2, Student(name, surname)), upsert())
    }
}

fun export() {
    val csvPrinter = CSVPrinter(FileWriter("D:\\IT\\kurs\\src\\main\\resources\\StudWrite.csv"), CSVFormat.DEFAULT
        .withHeader("Группа", "Подгруппа", "Имя", "Фамилия"))
    val curs = groupDb.find()
    val listStudent = curs.toMutableList()
    for (student in listStudent) {
        student.podgroup1.map {
            val studentData1 = Arrays.asList(
                student.number,
                1,
                it.name, it.surname
            )
            println(studentData1)
            csvPrinter.printRecord(studentData1)
        }
        student.podgroup2.map {
            val studentData2 = Arrays.asList(
                student.number,
                2,
                it.name, it.surname
            )
            println(studentData2)
            csvPrinter.printRecord(studentData2)
        }

    }
    csvPrinter.flush()
    csvPrinter.close()
}