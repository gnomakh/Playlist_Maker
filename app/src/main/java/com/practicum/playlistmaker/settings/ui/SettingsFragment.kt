package com.practicum.playlistmaker.settings.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.practicum.playlistmaker.databinding.FragmentSettingsBinding
import com.practicum.playlistmaker.settings.domain.model.IntentState
import com.practicum.playlistmaker.settings.ui.ViewModel.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : Fragment() {

    private val viewModel: SettingsViewModel by viewModel()

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.themeSwitch.setOnCheckedChangeListener(null)
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.themeSwitch.isChecked = viewModel.getSwitchState()

        binding.themeSwitch.setOnCheckedChangeListener { switch, isChecked ->
            viewModel.switchTheme(isChecked)
        }

        binding.shareButton.setOnClickListener {
            startIfNonNull(IntentState.SHARE)
        }

        binding.supportButton.setOnClickListener {
            startIfNonNull(IntentState.SEND_EMAIL)
        }

        binding.agreementButton.setOnClickListener {
            startIfNonNull(IntentState.OPEN_TERMS)
        }
    }

    private fun startIfNonNull(intentType: IntentState) {
        viewModel.setIntentType(intentType)
        startActivity(viewModel.getIntentState().value!!)
    }
}