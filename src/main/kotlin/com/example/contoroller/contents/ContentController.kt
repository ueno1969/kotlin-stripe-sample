data class Content(val id: Int, val name: String, val price: Int)

class ContentController() {
    fun list(): List<Content> {
        return listOf(
            Content(1, "赤ペン", 100),
            Content(2, "青ペン", 200),
            Content(3, "黄ペン", 300),
        )
    }
}
