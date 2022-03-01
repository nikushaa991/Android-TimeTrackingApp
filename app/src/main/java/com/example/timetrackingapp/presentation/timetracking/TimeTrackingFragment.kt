package com.example.timetrackingapp.presentation.timetracking

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.timetrackingapp.databinding.TimeTrackingFragmentBinding
import com.example.timetrackingapp.shared.UNKNOWN_ERROR
import com.example.timetrackingapp.shared.helpers.EmailHelper
import com.example.timetrackingapp.shared.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TimeTrackingFragment : Fragment() {

    private var _binding: TimeTrackingFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TimeTrackingViewModel.Viewmodel by activityViewModels()


    private var adapter = TimeTrackingAdapter()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = TimeTrackingFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupPage()
        addListeners()
        observeData()
        viewModel.input.loadTasks()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupPage() {
        binding.recycler.layoutManager = LinearLayoutManager(activity)
        binding.recycler.adapter = adapter
    }

    private fun addListeners() {
        with(binding) {
            buttonAdd.setOnClickListener { onAddClicked() }
            buttonShare.setOnClickListener { onShareClicked() }
        }
    }

    private fun onAddClicked() {
        AddTaskDialogFragment.show(parentFragmentManager)
    }

    private fun onShareClicked() {
        EmailHelper.sendEmail(requireContext(), viewModel.input.getTasks())
    }

    private fun observeData() {
        viewModel.tasks.observe(viewLifecycleOwner) { tasks ->
            adapter.submit(tasks)
        }

        viewModel.loading.observe(viewLifecycleOwner) { isLoading ->
            adapter.setLoading(isLoading)
        }

        viewModel.errors.observe(viewLifecycleOwner) { error ->
            showToast(requireContext(), error?.message ?: UNKNOWN_ERROR)
        }
    }
}