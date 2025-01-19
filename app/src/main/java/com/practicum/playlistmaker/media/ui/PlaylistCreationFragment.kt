package com.practicum.playlistmaker.media.ui

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.bundle.Bundle
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.databinding.FragmentPlaylistMakerBinding
import com.practicum.playlistmaker.media.domain.models.Playlist
import com.practicum.playlistmaker.media.ui.ViewModel.PlaylistCreationViewModel
import com.practicum.playlistmaker.root.dpToPx
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileOutputStream

class PlaylistCreationFragment: Fragment() {
    private var _binding: FragmentPlaylistMakerBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PlaylistCreationViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlaylistMakerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

        binding.playlistMakerToolbar.setNavigationOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        var picturePicked = ""

        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    binding.uploadImagePicked.isVisible = true

                    Glide.with(requireActivity())
                        .load(uri)
                        .transform(
                            CenterCrop(),
                            RoundedCorners(requireActivity().dpToPx(8.0F)),
                        )
                        .into(binding.uploadImagePicked)

                    picturePicked = saveImageToPrivateStorage(uri)
                    Toast.makeText(requireActivity(), picturePicked, Toast.LENGTH_SHORT).show()
                } else {
                    binding.uploadImagePicked.isVisible = false
                    Toast.makeText(requireActivity(), "Изображение не выбрано", Toast.LENGTH_SHORT).show()
                }
            }

        binding.uploadImageTemplate.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.playlistTitleEdittext.addTextChangedListener(
            beforeTextChanged = { text: CharSequence?, start: Int, count: Int, after: Int -> },
            onTextChanged = { text: CharSequence?, start: Int, before: Int, count: Int ->
                binding.buttonCreatePlaylist.isEnabled = !text.isNullOrEmpty()
            },
            afterTextChanged = { text: Editable? -> }
        )

        binding.buttonCreatePlaylist.setOnClickListener {
            viewModel.addPlaylist(createPlaylist(binding.playlistTitleEdittext.text.toString(), picturePicked, binding.playlistDescriptionEdittext.text.toString()))
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun saveImageToPrivateStorage(uri: Uri) : String {
        //создаём экземпляр класса File, который указывает на нужный каталог
        val filePath = File(requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "pl_covers")
        //создаем каталог, если он не создан
        if (!filePath.exists()){
            filePath.mkdirs()
        }
        val fileName = "image_${System.currentTimeMillis()}.jpg"
        //создаём экземпляр класса File, который указывает на файл внутри каталога
        val file = File(filePath, fileName)
        // создаём входящий поток байтов из выбранной картинки
        val inputStream = requireActivity().contentResolver.openInputStream(uri)
        // создаём исходящий поток байтов в созданный выше файл
        val outputStream = FileOutputStream(file)
        // записываем картинку с помощью BitmapFactory
        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
        return fileName
    }

    private fun createPlaylist(title: String, picture: String, desc: String) : Playlist {
        return Playlist(title, desc, picture, "69 треков")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
    }
}