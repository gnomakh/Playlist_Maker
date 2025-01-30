package com.practicum.playlistmaker.playlist.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlaylistScreenBinding
import com.practicum.playlistmaker.media.domain.models.Playlist
import com.practicum.playlistmaker.playlist.ui.viewModel.PlaylistViewModel
import com.practicum.playlistmaker.root.dpToPx
import com.practicum.playlistmaker.root.getDeclination
import com.practicum.playlistmaker.root.timeStringToMillis
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.search.ui.track.TracksAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistFragment : Fragment() {

    private var _binding: FragmentPlaylistScreenBinding? = null
    private val binding get() = _binding!!

    private var trackList = listOf<Track>()
    private val adapter = TracksAdapter()

    private val viewModel: PlaylistViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var userIsNotified = false

        binding.playlistToolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().navigateUp()
                }
            })

        val playlistId = arguments?.getString("playlistId")

        binding.rvTracksPlaylist.adapter = adapter

        viewModel.setPlaylistData(playlistId!!)
        viewModel.getTrackList()

        val bottomSheetBehavior =
            BottomSheetBehavior.from(binding.standardBottomSheetTracks).apply {
                state = BottomSheetBehavior.STATE_COLLAPSED
            }

        viewModel.getPlaylistLiveData().observe(viewLifecycleOwner) {
            setPlaylistInfo(it)
            binding.bottomSheetLimiter.post {
                bottomSheetBehavior.peekHeight = calculateBottomSheetHeight()
            }
        }

        viewModel.getTracksLiveData().observe(viewLifecycleOwner) {
            trackList = it
            adapter.trackList =
                if (it.isNotEmpty()) trackList as ArrayList<Track> else arrayListOf()
            if (it.isEmpty() and !userIsNotified) {
                Toast.makeText(
                    requireActivity(),
                    requireActivity().getString(R.string.no_tracks_notification),
                    Toast.LENGTH_SHORT
                ).show()
                userIsNotified = true
            }
            setTracksDurationAndQuantity(trackList)
            adapter.notifyDataSetChanged()
        }

        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                binding.overlay.alpha = (slideOffset + 1)
            }
        })

        val moreBottomSheetBehavior =
            BottomSheetBehavior.from(binding.standardBottomSheetMore).apply {
                state = BottomSheetBehavior.STATE_HIDDEN

            }

        binding.shareButton.setOnClickListener {
            sharePlaylist()
        }

        binding.moreButton.setOnClickListener {
            moreBottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        }

        moreBottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {

            override fun onStateChanged(bottomSheet: View, newState: Int) {

                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                        binding.overlay.isVisible = false
                    }

                    else -> {
                        binding.overlay.isVisible = true
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                binding.overlay.alpha = slideOffset + 1
            }
        })

        adapter.onClickListener = TracksAdapter.OnTrackClickListener { track ->

            val gsonTrack = Gson().toJson(track)
            val bundle = Bundle()
            bundle.putString("trackForPlayer", gsonTrack)

            findNavController().navigate(R.id.action_playlistFragment_to_playerFragment, bundle)
        }

        adapter.onHoldListener = TracksAdapter.OnTrackHoldListener { track ->
            MaterialAlertDialogBuilder(requireContext()).setTitle(getString(R.string.dialog_track_delete_title))
                .setMessage(getString(R.string.dialog_delete_track_confirmation_message))
                .setNeutralButton(getString(R.string.dialog_confirm_cancel)) { dialog, which ->

                }.setPositiveButton(getString(R.string.dialog_confirm_deletion)) { dialog, which ->
                    viewModel.deleteTrack(track)
                }.show()
        }

        binding.share.setOnClickListener {
            moreBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            sharePlaylist()
        }

        binding.delete.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext()).setTitle(getString(R.string.dialog_playlist_delete_title))
                .setMessage("${getString(R.string.dialog_delete_playlist_confirmation_message)} ${binding.playlistTitle.text}?")
                .setNegativeButton(getString(R.string.dialog_cancel_playlist_deletion)) { dialog, which ->

                }
                .setPositiveButton(getString(R.string.dialog_confirm_playlist_deletion)) { dialog, which ->
                    viewModel.deletePlaylist()
                    findNavController().navigateUp()
                }.show()
        }

        binding.edit.setOnClickListener {
            val bundle = Bundle()
            bundle.putString(
                "playlistIdtoEdit", viewModel.getPlaylistLiveData().value!!.id.toString()
            )
            findNavController().navigate(
                R.id.action_playlistFragment_to_playlistCreationFragment, bundle
            )
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setPlaylistInfo(playlist: Playlist) {
        Glide.with(this).load(playlist.picture).fitCenter()
            .placeholder(R.drawable.placeholder_playlist).into(binding.playlistImage)
        with(binding) {
            playlistTitle.text = playlist.title
            playlistDescription.isVisible = playlist.description.isNotBlank()
            playlistDescription.text = playlist.description

            if (playlist.picture != "") {
                Glide.with(requireActivity()).load(playlist.picture).transform(
                    CenterCrop(),
                    RoundedCorners(requireActivity().dpToPx(2.0F)),
                ).placeholder(R.drawable.placeholder_playlist).into(playlistImageBottomsheet)
            }

            playlistTitleBottomsheet.text = playlist.title

        }
    }

    @SuppressLint("SetTextI18n")
    private fun setTracksDurationAndQuantity(trackList: List<Track>) {

        if (trackList.isNotEmpty()) {

            var count = 0L

            for (i in trackList) {
                count += requireActivity().timeStringToMillis(i.trackTimeMillis)
            }

            val minutes = count / 60000
            val minutesFormatted = when {
                minutes % 10 == 1L && minutes % 100 != 11L -> "$minutes минута"
                minutes % 10 in 2..4 && minutes % 100 !in 12..14 -> "$minutes минуты"
                else -> "$minutes минут"
            }

            binding.playlistDuration.text = minutesFormatted
        } else binding.playlistDuration.text = "0 минут"

        with(binding) {

            playlistTrackCount.text =
                "${trackList.size} ${requireActivity().getDeclination(trackList.size)}"

            trackCountBottomsheet.text =
                "${trackList.size} ${requireActivity().getDeclination(trackList.size)}"

        }

        Log.i("playlist111", "${trackList.size}")
    }

    private fun sharePlaylist() {
        if (trackList.isEmpty()) {
            Toast.makeText(
                requireActivity(),
                requireActivity().getString(R.string.no_tracks_to_share),
                Toast.LENGTH_SHORT
            ).show()
        } else {
            val shareMessage = viewModel.getIntentMessage()
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, shareMessage)
            }
            startActivity(shareIntent)
        }
    }

    private fun calculateBottomSheetHeight(): Int {
        val location = IntArray(2)
        binding.bottomSheetLimiter.getLocationOnScreen(location)
        val limiterY = location[1]

        val screenHeight = resources.displayMetrics.heightPixels

        val height = screenHeight - limiterY
        return height
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}