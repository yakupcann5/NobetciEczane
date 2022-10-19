package com.yakupcan.nobetcieczane.ui.push

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.yakupcan.nobetcieczane.R
import com.yakupcan.nobetcieczane.common.RequestState
import com.yakupcan.nobetcieczane.databinding.FragmentPushBinding
import com.yakupcan.nobetcieczane.domain.model.Notification
import com.yakupcan.nobetcieczane.domain.model.PushModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PushFragment : Fragment(), View.OnClickListener {
    private val viewModel : PushFragmentViewModel by viewModels()
    lateinit var binding : FragmentPushBinding
    private var serverKey = ""
    private var lastToken = ""

    override fun onCreateView(
        inflater : LayoutInflater,
        container : ViewGroup?,
        savedInstanceState : Bundle?
    ) : View? {
        binding = FragmentPushBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view : View, savedInstanceState : Bundle?) {
        setView()
        setObservers()
    }

    private fun setObservers() {
        viewModel.serverKey.observe(viewLifecycleOwner) {
            serverKey = it
        }

        viewModel.lastToken.observe(viewLifecycleOwner) {
            lastToken = it
        }
    }

    private fun setView() {
        binding.sendAllDevices.setOnClickListener(this)
        binding.sendOneDevices.setOnClickListener(this)
        binding.settingsButton.setOnClickListener(this)
    }

    override fun onClick(p0 : View?) {
        when (p0?.id) {
            R.id.sendAllDevices -> {
                sendAllDevices("/topics/all")
            }

            R.id.sendOneDevices -> {
                sendOneDevices()
            }

            R.id.settingsButton -> {
                showSettings()
            }
        }
    }

    private fun sendAllDevices(to : String) {
        if (!checkInput()) {
            showMessage("Please fill the inputs!")
        } else {
            val pushModel = PushModel()
            pushModel.to = to
            pushModel.priority = "high"
            pushModel.notification = Notification(
                binding.titleEditText.text.toString(),
                binding.bodyEditText.text.toString()
            )
            if (serverKey.isEmpty()) {
                Toast.makeText(
                    requireContext(),
                    "Please fill the server key input!",
                    Toast.LENGTH_SHORT
                ).show()
                return
            }
            viewModel.pushToAllDevices(pushModel, serverKey)
            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.pushState.collect { result ->
                    when (result) {
                        is RequestState.Success -> {
                            result.data.let {
                                Toast.makeText(
                                    requireContext(),
                                    "Push to all devices is successfully",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                        is RequestState.Error -> {
                            Toast.makeText(
                                requireContext(),
                                "Push to all devices is unsuccessfully",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        else -> {}
                    }
                }
            }
        }
    }

    private fun showMessage(s : String) {
        Toast.makeText(context, s, Toast.LENGTH_SHORT).show()
    }

    private fun checkInput() : Boolean {
        return !(binding.bodyEditText.text.isNullOrBlank() || binding.titleEditText.text.isNullOrBlank())
    }

    private fun sendOneDevices() {
        if (!checkInput()) {
            showMessage("Please fill the inputs!")
        } else {
            showTokenDialog()
        }
    }

    private fun showTokenDialog() {
        lateinit var saveAndSendButton : TextView
        lateinit var tokenEditText : EditText
        lateinit var closeButton : ImageView

        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.token_dialog)
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.setCancelable(false)

        saveAndSendButton = dialog.findViewById(R.id.saveTokenButton)
        tokenEditText = dialog.findViewById(R.id.tokenEditText)
        closeButton = dialog.findViewById(R.id.closeButton)
        tokenEditText.setText(lastToken)

        saveAndSendButton.setOnClickListener {
            if (tokenEditText.text.isNullOrBlank())
                showMessage("Please fill the inputs!")
            else {
                viewModel.savePushToken(tokenEditText.text.toString())
                sendAllDevices(tokenEditText.text.toString())
                dialog.dismiss()
            }
        }

        closeButton.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun showSettings() {
        lateinit var saveButton : TextView
        lateinit var closeButton : ImageView
        lateinit var serverKeyEditText : EditText

        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.push_settings_dialog)
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.setCancelable(false)
        saveButton = dialog.findViewById(R.id.settingsSaveButton)
        closeButton = dialog.findViewById(R.id.settingsCloseButton)
        serverKeyEditText = dialog.findViewById(R.id.serverKey)
        serverKeyEditText.setText(serverKey)
        saveButton.setOnClickListener {
            if (serverKeyEditText.text.isNullOrBlank())
                showMessage("Please fill the inputs!")
            else {
                viewModel.saveServerKey(serverKeyEditText.text.toString())
                showMessage("Settings saving is successfully.")
                dialog.dismiss()
            }
        }
        closeButton.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }
}
