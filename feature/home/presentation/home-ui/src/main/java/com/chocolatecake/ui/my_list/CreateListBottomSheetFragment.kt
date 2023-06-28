package com.chocolatecake.ui.my_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.chocolatecake.ui.home.BR
import com.chocolatecake.ui.home.R
import com.chocolatecake.ui.home.databinding.BottomSheetCreateListBinding
import com.chocolatecake.viewmodel.myList.MyListViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CreateListBottomSheetFragment(private val createButton: CreateListener) :
    BottomSheetDialogFragment() {
    private lateinit var binding: BottomSheetCreateListBinding
    val viewModel by activityViewModels<MyListViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.bottom_sheet_create_list, container, false)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            setVariable(BR.viewModel, viewModel)
            return root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        binding.materialButtonCreate.setOnClickListener {
            if (binding.textInputEditTextListName.text == null || binding.textInputEditTextListName.text.toString().trim() == "") {
                createButton.failCreated("Entry failed")
                dismiss()
            } else {
                createButton.onClickCreate(binding.textInputEditTextListName.text.toString())
            }
        }

        binding.textViewClose.setOnClickListener {
            dismiss()
        }

    }
}

interface CreateListener {
    fun onClickCreate(listName: String)
    fun failCreated(message: String)
}