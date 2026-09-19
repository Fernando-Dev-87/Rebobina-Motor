package com.example

import com.example.data.datasource.RebobinadorFilesRepository
import com.example.domain.model.FileCategory
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class RebobinadorFilesRepositoryTest {

    @Test
    fun testFilesRepositoryNotEmpty() {
        val files = RebobinadorFilesRepository.files
        assertTrue("Files repository should contain files from O Rebobinador", files.isNotEmpty())
        assertTrue("Should have at least 5 files", files.size >= 5)
    }

    @Test
    fun testFacebookFilesUrlFormat() {
        val url = RebobinadorFilesRepository.FACEBOOK_FILES_URL
        assertEquals("https://www.facebook.com/groups/395592083967391/files/files", url)
    }

    @Test
    fun testApostila01809Present() {
        val apostila = RebobinadorFilesRepository.files.find { it.id == "apostila_018_09" }
        assertNotNull("Apostila 018/09 must be in repository", apostila)
        assertEquals(FileCategory.APOSTILAS, apostila?.category)
        assertTrue(apostila?.technicalContent?.contains("PASSO POLAR") == true)
        assertTrue(apostila?.technicalContent?.contains("FLUXO MAGNÉTICO") == true)
        assertTrue(apostila?.tags?.contains("Iltonn") == true)
    }

    @Test
    fun testEsquema12PontasPresent() {
        val esquema12 = RebobinadorFilesRepository.files.find { it.id == "esquema_12_pontas" }
        assertNotNull("Esquema 12 Pontas must be in repository", esquema12)
        assertEquals(FileCategory.ESQUEMAS_TRIFASICOS, esquema12?.category)
        assertTrue(esquema12?.technicalContent?.contains("DUPLO TRIÂNGULO") == true)
        assertTrue(esquema12?.technicalContent?.contains("220V") == true)
        assertTrue(esquema12?.technicalContent?.contains("380V") == true)
        assertTrue(esquema12?.technicalContent?.contains("440V") == true)
        assertTrue(esquema12?.technicalContent?.contains("760V") == true)
    }

    @Test
    fun testTabelaAwgPresent() {
        val tabela = RebobinadorFilesRepository.files.find { it.id == "tabela_awg_esmaltado" }
        assertNotNull("Tabela AWG must be in repository", tabela)
        assertEquals(FileCategory.TABELAS_TECNICAS, tabela?.category)
        assertTrue(tabela?.technicalContent?.contains("AWG") == true)
        assertTrue(tabela?.technicalContent?.contains("Seção mm²") == true)
    }

    @Test
    fun testAllFilesHaveValidData() {
        for (file in RebobinadorFilesRepository.files) {
            assertFalse("File ID must not be blank", file.id.isBlank())
            assertFalse("File title must not be blank", file.title.isBlank())
            assertFalse("File name must not be blank", file.fileName.isBlank())
            assertFalse("Author must not be blank", file.author.isBlank())
            assertFalse("Technical content must not be blank", file.technicalContent.isBlank())
            assertTrue("Download URL must point to Facebook files", file.downloadUrl.contains("facebook.com/groups/395592083967391/files/files"))
        }
    }
}
