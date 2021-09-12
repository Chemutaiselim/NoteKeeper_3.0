package com.pamela.projo.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

class NoteListFragment : Fragment() {

    private var _binding: FragmentNoteListBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentNoteListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_NoteListFragment_to_NoteFragment)
        }

        binding.listNotes.adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_list_item_1,
            DataManager.notes
        )
        binding.listNotes.setOnItemClickListener { _, _, p3, _ ->
            val action = NoteListFragmentDirections.actionNoteListFragmentToNoteFragment(p3)
            activity?.findNavController(R.id.nav_host_fragment_content_main)?.navigate(action)
        }
    }
    override fun onResume() {
        super.onResume()
        binding.listNotes.adapter.areAllItemsEnabled()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}