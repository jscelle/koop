import java.text.SimpleDateFormat

fun main(args: Array<String>) {
    val sdf = SimpleDateFormat("dd.MM.yyyy")

    val stack = MutableSortedStack.MutableSortedStackImpl()

    val readerManager = ReaderManager.ReaderManagerImpl(stack)
    val readerPrinting = ReaderPrinting.ReaderPrintingImpl(stack)

    val reader1 = Reader("Иванов", "Иван", "Иванович", "Книга 1", sdf.parse("01.01.2000"), sdf.parse("01.02.2000"))
    val reader2 = Reader("Петров", "Петр", "Петрович", "Книга 2", sdf.parse("01.01.2000"), sdf.parse("10.01.2000"))
    val reader3 = Reader("Сидоров", "Сидор", "Сидорович", "Книга 3", sdf.parse("01.01.2000"), sdf.parse("02.01.2000"))

    readerManager.addReader(reader1)
    readerManager.addReader(reader2)
    readerManager.addReader(reader3)

    readerPrinting.printAllReaders()

    val newReader3 = Reader("Сидоров", "Сидор", "Сидорович", "Книга 3", sdf.parse("01.01.2000"), sdf.parse("20.01.2023"))

    readerManager.updateReaderData(reader3, newReader3)

    readerPrinting.printAllReaders()

    val lastNameToSearch = "Сидоров"
    readerPrinting.printReaderInfoByLastName(lastNameToSearch)

    val path = "test.txt"
    val fileManager = FileManager.FileManagerImpl()
    fileManager.create(path, stack)

    print(fileManager.read(path).get())
}