package com.example.timetrackingapp.presentation.timetracking

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import com.example.timetrackingapp.R
import com.example.timetrackingapp.databinding.DialogFragmentAddTaskBinding
import com.example.timetrackingapp.shared.secondsToTimeText
import com.example.timetrackingapp.shared.showToast
import com.example.timetrackingapp.shared.toggleVisibility

class AddTaskDialogFragment : DialogFragment() {

    private var _binding: DialogFragmentAddTaskBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TimeTrackingViewModel.Viewmodel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogFragmentAddTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeData()
        addListeners()
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        viewModel.input.stopTimer()
    }

    private fun observeData() {
        viewModel.output.timer.observe(viewLifecycleOwner) { time ->
            binding.timer.text = time.secondsToTimeText()
        }
    }

    private fun addListeners() {
        binding.buttonStart.setOnClickListener { onStartClicked() }
        binding.buttonEnd.setOnClickListener { onEndClicked() }
    }

    private fun onStartClicked() {
        if (binding.name.text.isNullOrEmpty() || binding.desc.text.isNullOrEmpty()) {
            showEmptyTextError()
            return
        }
        toggleButtonVisibility()

        viewModel.input.startTimer()
    }

    private fun toggleButtonVisibility() {
        binding.buttonStart.toggleVisibility(false)
        binding.buttonEnd.toggleVisibility(true)
    }

    private fun onEndClicked() {
        viewModel.input.addTask(
            binding.name.text.toString(),
            binding.desc.text.toString(),
        )
        dismiss()
    }

    private fun showEmptyTextError() {
        showToast(requireContext(), R.string.empty_text_error)
    }

    companion object {
        fun show(fragmentManager: FragmentManager) {
            AddTaskDialogFragment().show(fragmentManager, null)
        }
    }
}