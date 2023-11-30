
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

/// В файл записать список читателей библиотеки в
/// формате: фамилия, имя, отчество, название книги, дата
/// выдачи книги и дата возврата. Разработать объект - список, в
/// котором есть методы работы с читателями библиотеки:
/// Создание общего списка читателей в виде стека, где на
/// вершине находится читатель, у которого самая большая задолженность (по дате возврата книг), создание списка -
/// стека задолжников, исправление данных уже созданного
/// списка и ввод новых читателей, вывод всех списков на экран.
/// При этом на экран должны выводиться списки только по
/// фамилиям читателей, а по запросу - уже вся информация о
/// читателе.
///

data class Reader(
    val lastName: String,
    val firstName: String,
    val patronymic: String,
    val bookTitle: String,
    val checkoutDate: Date,
    val returnDate: Date
)

interface ReaderManager {

    fun addReader(reader: Reader)
    fun updateReaderData(oldReader: Reader, newReader: Reader)

    class ReaderManagerImpl(private val stack: MutableSortedStack): ReaderManager {

        override fun addReader(reader: Reader) {
            stack.push(reader)
        }

        override fun updateReaderData(oldReader: Reader, newReader: Reader) {
            stack.delete(oldReader)
            stack.push(newReader)
        }
    }
}

interface ReaderPrinting {
    fun printAllReaders()
    fun printReaderInfoByLastName(lastName: String)

    class ReaderPrintingImpl(private val stack: MutableSortedStack): ReaderPrinting {

        override fun printAllReaders() {
            println(stack.get().map { it.lastName })
        }

        override fun printReaderInfoByLastName(lastName: String) {
            val readersByLastName = stack.get().filter { it.lastName == lastName }
            if (readersByLastName.isEmpty()) {
                println("Читатель с фамилией $lastName не найден.")
            } else {
                println("Информация о читателях с фамилией $lastName:")
                for (reader in readersByLastName) {
                    println("Фамилия: ${reader.lastName}")
                    println("Имя: ${reader.firstName}")
                    println("Отчество: ${reader.patronymic}")
                    println("Название книги: ${reader.bookTitle}")
                    println("Дата выдачи: ${SimpleDateFormat("dd.MM.yyyy").format(reader.checkoutDate)}")
                    println("Дата возврата: ${SimpleDateFormat("dd.MM.yyyy").format(reader.returnDate)}")
                }
            }
        }
    }
}

interface MutableSortedStack {
    fun push(reader: Reader)
    fun delete(reader: Reader)
    fun get(): Stack<Reader>

    class MutableSortedStackImpl: MutableSortedStack {
        private val stack = Stack<Reader>()

        override fun push(reader: Reader) {
            stack.push(reader)
            sortReaderStack()
        }

        override fun delete(reader: Reader) {
            stack.remove(reader)
        }

        private fun sortReaderStack() {
            val customComparator = Comparator<Reader> { reader1, reader2 ->
                reader1.returnDate.compareTo(reader2.returnDate)
            }
            stack.sortWith(customComparator)
        }

        override fun get(): Stack<Reader> = this.stack
    }
}

interface FileManager {
    fun create(path: String, readers: MutableSortedStack)
    fun read(path: String): MutableSortedStack

    class FileManagerImpl: FileManager {

        override fun create(path: String, readers: MutableSortedStack) {

            val file = File(path)

            val csvData = StringBuilder()
            val sdf = SimpleDateFormat("dd.MM.yyyy")

            for (reader in readers.get()) {
                csvData.append("${reader.lastName}, ${reader.firstName}, ${reader.patronymic}, ${reader.bookTitle}, ${sdf.format(reader.checkoutDate)}, ${sdf.format(reader.returnDate)}\n")
            }

            file.writeText(csvData.toString())
        }

        override fun read(path: String): MutableSortedStack {
            val file = File(path)
            if (!file.exists()) {
                return MutableSortedStack.MutableSortedStackImpl()
            }

            val csvData = file.readText()
            val lines = csvData.split('\n')
            val stack = MutableSortedStack.MutableSortedStackImpl()
            val sdf = SimpleDateFormat("dd.MM.yyyy")

            for (line in lines) {
                val parts = line.split(",").map { it.trim() }
                if (parts.size == 6) {
                    val reader = Reader(
                        parts[0], parts[1], parts[2], parts[3],
                        sdf.parse(parts[4]), sdf.parse(parts[5])
                    )
                    stack.push(reader)
                }
            }

            return stack
        }
    }
}

