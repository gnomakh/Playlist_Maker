package com.practicum.playlistmaker.media.ui.ViewModel

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.media.domain.api.PlaylistsInteractor
import com.practicum.playlistmaker.media.domain.models.Playlist
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

class PlaylistCreationViewModel(
    private val playlistInteractor : PlaylistsInteractor,
    private val application: Application,
) : AndroidViewModel(application) {

    fun addPlaylist(playlist: Playlist) {
        viewModelScope.launch { playlistInteractor.addPlaylist(playlist) }
    }

    fun createPlaylist(title: String, desc: String, picUri: Uri?) {
        val picPath = if(picUri != null) saveImageToPrivateStorage(picUri) else null
        addPlaylist(Playlist(0, title, desc, picPath, 0))
    }

    fun saveImageToPrivateStorage(uri: Uri) : String {
        val filePath = File(getApplication<Application>().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "pl_covers")
        if (!filePath.exists()){
            filePath.mkdirs()
        }
        val fileName = "image_${System.currentTimeMillis()}.jpg"
        val file = File(filePath, fileName)
        val inputStream = getApplication<Application>().contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(file)
        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
        return file.path
    }
}