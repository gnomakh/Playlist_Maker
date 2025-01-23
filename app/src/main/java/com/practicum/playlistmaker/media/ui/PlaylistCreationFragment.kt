package com.practicum.playlistmaker.media.ui

import android.net.Uri
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.bundle.Bundle
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlaylistMakerBinding
import com.practicum.playlistmaker.media.ui.ViewModel.PlaylistCreationViewModel
import com.practicum.playlistmaker.root.dpToPx
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistCreationFragment : Fragment() {
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
            onBackAttempt()
        }

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    onBackAttempt()
                }
            })

        var picUri: Uri? = null

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

                    picUri = uri
                } else {
                    // binding.uploadImagePicked.isVisible = false Убрать выбранное ранее изображение, если второй раз не выбрано ничего
//                    Toast.makeText(requireActivity(), "Изображение не выбрано", Toast.LENGTH_SHORT)
//                        .show()
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
            viewModel.createPlaylist(
                binding.playlistTitleEdittext.text.toString(),
                binding.playlistDescriptionEdittext.text.toString(),
                picUri
            )
            Toast.makeText(
                requireActivity(),
                "Плейлист ${binding.playlistTitleEdittext.text.toString()} создан",
                Toast.LENGTH_SHORT
            ).show()
            findNavController().navigateUp()
        }
    }

    private fun onBackAttempt() {
        if (!binding.playlistTitleEdittext.text.isNullOrBlank() or
            !binding.playlistDescriptionEdittext.text.isNullOrBlank() or
            binding.uploadImagePicked.isVisible
        )
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(getString(R.string.dialog_title))
                .setMessage(getString(R.string.dialog_message))
                .setNeutralButton(getString(R.string.dialog_neutral)) { dialog, which ->

                }
                .setPositiveButton(getString(R.string.dialog_positive)) { dialog, which ->
                    findNavController().navigateUp()
                }.show()
        else {
            findNavController().navigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
    }
}