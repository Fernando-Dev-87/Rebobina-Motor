package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Rebobina motor", appName)
  }

  @Test
  fun `verify rebobinador files repository integration`() {
    val files = com.example.data.datasource.RebobinadorFilesRepository.files
    assertEquals(true, files.isNotEmpty())
    assertEquals("https://www.facebook.com/groups/395592083967391/files/files", com.example.data.datasource.RebobinadorFilesRepository.FACEBOOK_FILES_URL)
  }
}
