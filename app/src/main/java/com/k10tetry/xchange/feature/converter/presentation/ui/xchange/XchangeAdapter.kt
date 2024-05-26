package com.k10tetry.xchange.feature.converter.presentation.ui.xchange

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.k10tetry.xchange.databinding.GridItemCurrencyRatesBinding
import com.k10tetry.xchange.databinding.ListItemCurrencyRatesBinding
import com.k10tetry.xchange.feature.converter.presentation.utils.formatAmount
import javax.inject.Inject

class XchangeAdapter @Inject constructor() :
    RecyclerView.Adapter<XchangeAdapter.XchangeHolder>() {

    var countryRateList = emptyList<Pair<String, String>>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var isGridLayout = true
        set(value) {
            field = value
        }

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): XchangeHolder {
        return when {
            viewType == 0 -> {
                XchangeViewXchangeHolder(
                    GridItemCurrencyRatesBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false
                    )
                )
            }

            else -> {
                XchangeViewXchangeHolderLinear(
                    ListItemCurrencyRatesBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false
                    )
                )
            }
        }
    }

    override fun onBindViewHolder(xchangeHolder: XchangeHolder, position: Int) {
        when (xchangeHolder) {
            is XchangeViewXchangeHolder -> xchangeHolder.onBind(countryRateList[position])
            is XchangeViewXchangeHolderLinear -> xchangeHolder.onBind(countryRateList[position])
        }
    }

    override fun getItemCount(): Int {
        return countryRateList.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (isGridLayout) 0 else 1
    }

    abstract class XchangeHolder(view: View) : RecyclerView.ViewHolder(view)

    inner class XchangeViewXchangeHolder(private val binding: GridItemCurrencyRatesBinding) :
        XchangeHolder(binding.root) {

        fun onBind(countryRate: Pair<String, String>) {
            binding.textViewName.text = countryRate.first
            binding.textViewStatus.text = countryRate.second.formatAmount()
        }

    }

    inner class XchangeViewXchangeHolderLinear(private val binding: ListItemCurrencyRatesBinding) :
        XchangeHolder(binding.root) {

        fun onBind(countryRate: Pair<String, String>) {
            binding.textViewName.text = countryRate.first
            binding.textViewStatus.text = countryRate.second.formatAmount()
        }

    }

}