import kotlinx.serialization.Serializable

@Serializable
data class Group(
    val number: String,
    val podgroup1: List<Student> = emptyList(),
    val podgroup2: List<Student> = emptyList()
)

