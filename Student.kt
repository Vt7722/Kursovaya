import kotlinx.serialization.Serializable

@Serializable
data class Student (
    val name: String = "",
    val surname: String = "",
)
{
    constructor(dataStudent:String):this(
        dataStudent.substringBefore(" ").capitalize(),
        dataStudent.substringAfter(" ").capitalize()
    )
    val dataStudent:String
        get() = "$name $surname"
}

