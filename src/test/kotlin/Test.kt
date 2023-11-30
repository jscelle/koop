import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.File
import java.util.*

class ReaderManagerImplTest {

    private lateinit var readerManager: ReaderManager
    private lateinit var stack: MutableSortedStack

    @BeforeEach
    fun setUp() {
        stack = MutableSortedStack.MutableSortedStackImpl()
        readerManager = ReaderManager.ReaderManagerImpl(stack)
    }

    @Test
    fun testAddReader() {
        val reader1 = Reader("Ivanov", "Ivan", "Ivanovich", "Java in Action", Date(), Date())
        readerManager.addReader(reader1)

        Assertions.assertEquals(1, stack.get().size)
        Assertions.assertEquals(reader1, stack.get().peek())

        val reader2 = Reader("Petrov", "Petr", "Petrovich", "Effective Java", Date(), Date())
        readerManager.addReader(reader2)

        Assertions.assertEquals(2, stack.get().size)
        Assertions.assertEquals(reader2, stack.get().peek())
    }

    @Test
    fun testUpdateReaderData() {
        val oldReader = Reader("Petrov", "Petr", "Petrovich", "Effective Java", Date(), Date())
        stack.push(oldReader)

        val newReader = Reader("Petrov", "Petr", "Petrovich", "Clean Code", Date(), Date())
        readerManager.updateReaderData(oldReader, newReader)

        Assertions.assertEquals(1, stack.get().size)
        Assertions.assertEquals(newReader, stack.get().peek())

        // Adding another reader and updating it
        val reader1 = Reader("Ivanov", "Ivan", "Ivanovich", "Java in Action", Date(), Date())
        readerManager.addReader(reader1)

        val updatedReader1 = Reader("Ivanov", "Ivan", "Ivanovich", "Kotlin in Action", Date(), Date())
        readerManager.updateReaderData(reader1, updatedReader1)

        Assertions.assertEquals(2, stack.get().size)
        Assertions.assertEquals(updatedReader1, stack.get().peek())
    }

    @Test
    fun testAddAndUpdateMultipleReaders() {
        // Adding multiple readers and updating one of them
        val reader1 = Reader("Ivanov", "Ivan", "Ivanovich", "Java in Action", Date(), Date())
        val reader2 = Reader("Petrov", "Petr", "Petrovich", "Effective Java", Date(), Date())
        val reader3 = Reader("Sidorov", "Sidor", "Sidorovich", "Clean Code", Date(), Date())

        readerManager.addReader(reader1)
        readerManager.addReader(reader2)
        readerManager.addReader(reader3)

        Assertions.assertEquals(3, stack.get().size)

        val updatedReader2 = Reader("Petrov", "Petr", "Petrovich", "Kotlin in Action", Date(), Date())
        readerManager.updateReaderData(reader2, updatedReader2)

        Assertions.assertEquals(3, stack.get().size)
        Assertions.assertEquals(updatedReader2, stack.get().peek())
    }
}

class FileManagerImplTest {

    private lateinit var fileManager: FileManager
    private lateinit var stack: MutableSortedStack
    private val filePath = "readers.csv"

    @BeforeEach
    fun setUp() {
        stack = MutableSortedStack.MutableSortedStackImpl()
        fileManager = FileManager.FileManagerImpl()
    }

    @Test
    fun testCreate() {
        val reader = Reader("Ivanov", "Ivan", "Ivanovich", "Java in Action", Date(), Date())
        stack.push(reader)

        fileManager.create(filePath, stack)

        val file = File(filePath)
        Assertions.assertTrue(file.exists())
    }
}
