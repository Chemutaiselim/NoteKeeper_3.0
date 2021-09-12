package com.pamela.projo.ui

import android.R
import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import com.pamela.projo.data.*


class NoteFragment : Fragment() {
        private var notePosition = POSITION_NOT_SET

        private var _binding: FragmentNoteBinding? = null

        // This property is only valid between onCreateView and
        // onDestroyView.
        private val binding get() = _binding!!

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View {

            _binding = FragmentNoteBinding.inflate(inflater, container, false)
            setHasOptionsMenu(true)
            return binding.root

        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

            val adapterCourses = ArrayAdapter(
                requireContext(),
                R.layout.simple_spinner_item,
                dataManager.courses.values.toList()
            )
            // Specify the layout to use when the list of choices appears
            adapterCourses.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            binding.spinnerCourses.adapter = adapterCourses

            notePosition = savedInstanceState?.getInt(NOTE_POSITION, POSITION_NOT_SET)
                ?: arguments?.let { NoteFragmentArgs.fromBundle(it).myArg }!!

            if (notePosition != POSITION_NOT_SET)
                displayNote()
            else {
                dataManager.notes.add(NoteInfo())
                notePosition = dataManager.notes.lastIndex
            }
        }

        override fun onSaveInstanceState(outState: Bundle) {
            super.onSaveInstanceState(outState)
            outState.putInt(NOTE_POSITION, notePosition)
        }

        private fun displayNote() {
            val note = dataManager.notes[notePosition]
            binding.noteTitle.setText(note.title)
            binding.noteText.setText(note.text)

            val coursePosition = dataManager.courses.values.indexOf(note.course)
            binding.spinnerCourses.setSelection(coursePosition)
        }

        override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
            // Inflate the menu; this adds items to the action bar if it is present.
            inflater.inflate(R.menu.menu_main, menu)
        }

        override fun onOptionsItemSelected(item: MenuItem): Boolean {
            // Handle action bar item clicks here. The action bar will
            // automatically handle clicks on the Home/Up button, so long
            // as you specify a parent activity in AndroidManifest.xml.
            return when (item.itemId) {
                R.id.action_settings -> true
                R.id.action_next -> {
                    if (notePosition < dataManager.notes.lastIndex)
                        moveNext()
                    true
                }

                else -> super.onOptionsItemSelected(item)
            }
        }

        override fun onPrepareOptionsMenu(menu: Menu) {
            if (notePosition >= dataManager.notes.lastIndex) {
                val itemMenu = menu.findItem(R.id.action_next)
                if (itemMenu != null) {
                    itemMenu.icon =
                        AppCompatResources.getDrawable(
                            requireContext().applicationContext,
                            R.drawable.ic_baseline_block_24
                        )
                }
            }
            return super.onPrepareOptionsMenu(menu)
        }

        private fun saveNote() {
            val note = dataManager.notes[notePosition]
            note.title = binding.noteTitle.text.toString()
            note.text = binding.noteText.text.toString()
            note.course = binding.spinnerCourses.selectedItem as CourseInfo?
        }


        private fun moveNext() {
            ++notePosition
            displayNote()
            requireActivity().invalidateOptionsMenu()
        }

        override fun onPause() {
            super.onPause()
            saveNote()
        }

        override fun onDestroyView() {
            super.onDestroyView()
            _binding = null
        }
    }
