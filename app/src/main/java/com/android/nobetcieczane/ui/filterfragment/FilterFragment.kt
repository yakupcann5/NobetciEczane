package com.android.nobetcieczane.ui.filterfragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.android.nobetcieczane.DistrictCityModel
import com.android.nobetcieczane.Districts
import com.android.nobetcieczane.R
import com.android.nobetcieczane.databinding.FragmentFilterBinding
import com.android.nobetcieczane.util.Helper
import com.android.nobetcieczane.util.Tools
import dagger.hilt.android.AndroidEntryPoint
import java.text.Collator
import java.util.*
import kotlin.collections.ArrayList

@AndroidEntryPoint
class FilterFragment : Fragment(), View.OnClickListener {
    private lateinit var binding: FragmentFilterBinding
    private val viewModel: FilterFragmentViewModel by viewModels()
    private var model: DistrictCityModel? = null
    private var cities: ArrayList<String> = arrayListOf()
    private var districts: ArrayList<Districts> = arrayListOf()
    private var districtList: ArrayList<String> = arrayListOf()
    private lateinit var citiesAdapter: ArrayAdapter<String>
    private lateinit var districtsAdapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFilterBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initViews()
        getData()
    }

    private fun initViews() {
        binding.filterCloseButton.setOnClickListener(this)
        binding.filterSaveButton.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.city_spinner -> {
                Toast.makeText(this.requireContext(), "deneme", Toast.LENGTH_SHORT).show()
            }
            R.id.districts_spinner -> {
                Toast.makeText(this.requireContext(), "deneme", Toast.LENGTH_SHORT).show()
            }
            R.id.filter_close_button -> {
                findNavController().navigate(R.id.action_filterFragment_to_listFragment2)
            }
            R.id.filter_save_button -> {
                viewModel.setFilterDistrict(binding.districtsSpinner.selectedItem.toString().lowercase())
                viewModel.setDisc(binding.districtsSpinner.selectedItem.toString())
                findNavController().navigate(R.id.action_filterFragment_to_mapsFragment2)
            }
        }
    }

    private fun getData() {
        model = Helper().getJsonDataFromAssets(this.requireContext(), "il.json")
        model?.data?.forEach {
            cities.add(
                it.text
            )
        }
        val coll: Collator = Collator.getInstance(Locale.getDefault())
        coll.strength = Collator.PRIMARY
        Collections.sort(cities, coll)
        citiesAdapter = ArrayAdapter(
            this.requireContext(),
            com.google.android.material.R.layout.support_simple_spinner_dropdown_item,
            cities
        )
        binding.citySpinner.adapter = citiesAdapter

        var selectedPositionCity = citiesAdapter.getPosition(viewModel.getCity())
        if (selectedPositionCity < 0 ) selectedPositionCity = 0
        binding.citySpinner.setSelection(selectedPositionCity)

        districts = model?.data?.get(selectedPositionCity)?.districts as ArrayList<Districts>
        districts.forEach {
            districtList.add(
                it.text
            )
        }
        Collections.sort(districtList, coll)
        districtsAdapter = ArrayAdapter(
            requireContext(),
            com.google.android.material.R.layout.support_simple_spinner_dropdown_item,
            districtList
        )
        binding.districtsSpinner.adapter = districtsAdapter
        binding.citySpinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                districtList.clear()
                model?.data?.forEach {
                    if (it.text == parent?.selectedItem.toString()) {
                        it.districts.forEach { district ->
                            districtList.add(
                                district.text
                            )
                        }
                        viewModel.setFilterCity(parent?.selectedItem.toString().lowercase())
                        viewModel.setCity(parent?.selectedItem.toString())
                    }
                }
                Collections.sort(districtList, coll)
                districtsAdapter =
                    ArrayAdapter<String>(
                        requireContext(),
                        com.google.android.material.R.layout.support_simple_spinner_dropdown_item,
                        districtList
                    )
                districtsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.districtsSpinner.adapter = districtsAdapter

                val selectedPositionDistrict = districtsAdapter.getPosition(viewModel.getDisc())
                binding.districtsSpinner.setSelection(selectedPositionDistrict)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }
        binding.districtsSpinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

        }
    }
}