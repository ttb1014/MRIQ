package com.vervyle.quiz

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import com.vervyle.data.repository.AssetRepository
import com.vervyle.data.repository.StructuredMriDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import org.xmlpull.v1.XmlPullParserFactory
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import javax.inject.Inject

const val TAG = "viewModelTag"

@HiltViewModel
class QuizViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val repository: AssetRepository
) : ViewModel() {

//    private val factory = XmlPullParserFactory.newInstance().apply {
//        isNamespaceAware = true
//    }
//    private val parser = factory.newPullParser()

    val uiState: StateFlow<QuizScreenUiState> =
        repository.getAssets("dataset_brain_new").map { asset ->
            QuizScreenUiState.Loaded(
                axialStructuredBitmaps = asset.axial,
                coronalStructuredBitmaps = asset.coronal,
                sagittalStructuredBitmaps = asset.sagittal,
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = QuizScreenUiState.Loading,
        )

//    private fun loadBitmap(
//        name: String,
//        basePath: File = context.externalCacheDir ?: context.cacheDir,
//    ): Bitmap {
//        var bitmap: Bitmap? = null
//        var fis: FileInputStream? = null
//        try {
//            fis = FileInputStream(File(basePath, name))
//        } catch (e: FileNotFoundException) {
//            Log.d(TAG, "file not found")
//            e.printStackTrace()
//        } catch (e: SecurityException) {
//            Log.d(TAG, "does not have permission to read")
//            e.printStackTrace()
//        }
//        if (name.endsWith(".xml")) {
//            parser.setInput(fis, null)
//            val vectorDrawable = VectorDrawableCompat.createFromXml(context.resources, parser)
//            vectorDrawable?.run {
//                bitmap = Bitmap.createBitmap(
//                    vectorDrawable.intrinsicWidth,
//                    vectorDrawable.intrinsicHeight,
//                    Bitmap.Config.ARGB_8888
//                )
//            }
//            fis?.close()
//            return bitmap ?: throw Exception("")
//        }
//        bitmap = BitmapFactory.decodeStream(fis)
//        fis?.close()
//        return bitmap!!
//    }
}